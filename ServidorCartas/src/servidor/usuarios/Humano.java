package servidor.usuarios;

import modeloDominio.Codigos;
import modeloDominio.EstadoPartida;
import modeloDominio.FaseChinchon;
import modeloDominio.baraja.Carta;
import modeloDominio.excepciones.NumeroParametrosExcepcion;
import modeloDominio.excepciones.ReinicioEnComunicacionExcepcion;
import servidor.Partida;

import java.io.IOException;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Semaphore;

import static modeloDominio.ProcesadorMensajes.getProcesadorMensajes;

public class Humano extends Jugador {

    private Socket s;
    private ExecutorService hilo = Executors.newSingleThreadExecutor();
    private boolean salida;
    private final Semaphore semaforo=new Semaphore(1);
    //Lo uso para guardar la solicitud recibida cuando durante el envío de un mensaje veo que el cliente ya habñia
    //iniciado una comunicación
    private String buffer;

    public Humano(String nombre, Partida partida, Socket s) {
        super(nombre, partida);
        this.s = s;
        this.salida=false;
        this.buffer=null;
    }

    public void receptorHumano() throws IOException {
        String mensaje;
        //Cuando la partida se abandone será null
        while (!this.salida) {
            try {
                //Doy un pequeño margen para que el hilo de los mensajes pueda trabajar y así no secuestro la
                //comunicación
                if(buffer==null)
                    while(!getProcesadorMensajes().libreComunicacion(this.s)
                        || semaforo.availablePermits()==0)
                    Thread.sleep(200);
                //tomo la comunicación
                getProcesadorMensajes().abrirComunicacion(this.s);
            mensaje = buffer==null?getProcesadorMensajes().recibirString(this.s):buffer;
            if (mensaje == null) {//Si es nulo es porque se ha cerrado el socket o se ha producido algún error
                this.salirForzado();
                //Vuelve a Servidor donde se gestionará el cierre
                return;
            }
            //Proceso la cadena recibida
            this.procesarInstrccion(mensaje);

        }catch (InterruptedException | ClassCastException e) {
                salida=true;
                System.out.println("Problema en la comunicación. Método interrumpido");
            } catch (ReinicioEnComunicacionExcepcion ignored) {
                //Ya que quiero que lo ignore
            } finally {
                if (!getProcesadorMensajes().libreComunicacion(this.s))
                    getProcesadorMensajes().cerrarComunicacion(this.s);
            }
        }
    }

    public void recibirTurno(){
        super.recibirTurno();
        this.recibirMensaje("Es tu turno");
    }
    /*
    Recibe y procesa los mensajes recibidos del cliente. Estoy repitiendo código:((
     */
    private void procesarInstrccion(String instruccion) {
        String[] palabras = instruccion.split("\\s+");
        try {
            switch (palabras[0]) {
                case "salir": {
                    this.salir();
                    break;
                }
                case "ayuda": {
                    //"Mostrando ayuda " + (this.acciones.enPartida() ? "inicio" : "juego");
                    break;
                }
                case "mover": {
                    if (palabras.length != 3) throw new NumeroParametrosExcepcion();
                    this.moverMano(Integer.parseInt(palabras[1]), Integer.parseInt(palabras[2]));
                    break;
                }
                case "echar": {
                    if (palabras.length != 2) throw new NumeroParametrosExcepcion();
                    int i = Integer.parseInt(palabras[1]);
                    this.echarCarta(i);
                    break;
                }
                case "cerrar": {
                    if (palabras.length != 2) throw new NumeroParametrosExcepcion();
                    int i = Integer.parseInt(palabras[1]);
                    this.cerrar(i);
                    break;
                }
                case "cogerCubierta": {
                    this.cogerCartaCubierta();
                    break;
                }
                case "cogerDescubierta": {
                    this.cogerCartaDecubierta();
                    break;
                }
                case "cartaDescubierta": {
                    this.mandarCartaDescubierta();
                    break;
                }
                case "ordenar": {
                    this.ordenarMano();
                    break;
                }
                case "empezar": {
                    this.empezar();
                    break;
                }
                case "jugadores": {
                    this.mandarListaJugadores();
                    break;
                }
                case "estado": {
                    this.mandarEstado();
                    break;
                }
                case "puntuaciones": {
                    this.mandarPuntuaciones();
                    break;
                }
                case "turno": {
                    this.turno();
                    break;
                }
                case "anfitrion": {
                    this.anfitrion();
                    break;
                }
                case "verMano": {
                    this.mandarMano();
                    break;
                }
                case "chat": {
                    this.enviarChat();
                    break;
                }
                case "crearIA": {
                    if (palabras.length > 3) throw new NumeroParametrosExcepcion();
                    if (palabras.length ==2) this.crearIA(palabras[1]);
                    else this.crearIA("");
                    break;
                }
                default:
                    getProcesadorMensajes().enviarObjeto(Codigos.INEXISTENTE, this.s);
            }
        } catch (NumeroParametrosExcepcion e) {
            getProcesadorMensajes().enviarObjeto(Codigos.MAL, this.s);
        }
    }
/*
Permite conversar con otros jugadores
 */
    private void enviarChat() {
        try {
            getProcesadorMensajes().enviarObjeto(Codigos.BIEN, this.s);
            List<Jugador> jugadores=new ArrayList<>(this.getPartida().getJugadores());
            //Elimino al jugador de la lista para no volver a recibirlo
            //jugadores.remove(this);
            this.getPartida().enviarMensaje(this.getNombre()+": "+ getProcesadorMensajes().recibirString(this.s),jugadores);
        } catch (ReinicioEnComunicacionExcepcion ignored) {
        }
    }

    /*
    Manda al cliente el nombre del jugador que ostenta el turno actual
     */
    protected Jugador turno(){
        Jugador jugador=super.turno();
        if (jugador!=null){
            getProcesadorMensajes().enviarObjeto(Codigos.BIEN, this.s);
            getProcesadorMensajes().enviarObjeto(jugador.getNombre(), this.s);
        }
        else getProcesadorMensajes().enviarObjeto(Codigos.MAL, this.s);
        return jugador;
    }
    protected Jugador anfitrion(){
        Jugador jugador=super.anfitrion();
        if (jugador!=null){
            getProcesadorMensajes().enviarObjeto(Codigos.BIEN, this.s);
            getProcesadorMensajes().enviarObjeto(jugador.getNombre(), this.s);
        }
        else getProcesadorMensajes().enviarObjeto(Codigos.MAL, this.s);
        return jugador;
    }
    private boolean salir() {
        if (this.getPartida().expulsarJugador(this)) {
            getProcesadorMensajes().enviarObjeto(Codigos.BIEN, this.s);
            this.salida=true;
            return true;
        }
        getProcesadorMensajes().enviarObjeto(Codigos.MAL, this.s);
        return false;
    }

    /*
    Gestiona la desconexión abrupta del cliente
     */
    private void salirForzado() {
        if(!this.salir()){
            this.salida=true;
        }
    }

    private void mandarMano() {
        getProcesadorMensajes().enviarObjeto(Codigos.BIEN, this.s);
        getProcesadorMensajes().reset(s);
        getProcesadorMensajes().enviarObjeto(this.getMano(), this.s);
    }

    private void mandarListaJugadores() {
        getProcesadorMensajes().enviarObjeto(Codigos.BIEN, this.s);
        List<Jugador> listaJ=this.getPartida().getJugadores();
        List<String> lista=new ArrayList<>(listaJ.size());
        for (Jugador jugador:listaJ
             ) {
            lista.add(jugador.getNombre());
        }
        getProcesadorMensajes().enviarObjeto(lista, this.s);
    }

    private void mandarPuntuaciones() {
        getProcesadorMensajes().enviarObjeto(Codigos.BIEN, this.s);
        getProcesadorMensajes().enviarObjeto(this.puntuaciones(), this.s);
    }

    private void mandarEstado() {
        getProcesadorMensajes().enviarObjeto(Codigos.BIEN, this.s);
        getProcesadorMensajes().enviarObjeto(this.getPartida().getEstado(), this.s);
    }

    private void mandarCartaDescubierta() {
        if (this.getPartida().getEstado() == EstadoPartida.ENCURSO && this.getPartida().getFase() == FaseChinchon.ABIERTO) {
            getProcesadorMensajes().enviarObjeto(Codigos.BIEN, this.s);
            getProcesadorMensajes().enviarObjeto(this.getPartida().getDescubierta(), this.s);
        } else getProcesadorMensajes().enviarObjeto(Codigos.MAL, this.s);
    }

    protected boolean ordenarMano() {
        if (super.ordenarMano()){
            getProcesadorMensajes().enviarObjeto(Codigos.BIEN, this.s);
            return true;
        }
        else getProcesadorMensajes().enviarObjeto(Codigos.MAL, this.s);
        return false;
    }

    private void empezar() {
        if ((this.getPartida().getEstado() == EstadoPartida.ESPERANDO ||
                this.getPartida().getFase() == FaseChinchon.ESPERANDO) && this.getPartida().getAnfitrion()==this) {
            getProcesadorMensajes().enviarObjeto(Codigos.BIEN, this.s);
            this.getPartida().iniciarPartida();
        } else getProcesadorMensajes().enviarObjeto(Codigos.MAL, this.s);
    }

    protected Carta cogerCartaCubierta() {
        Carta carta=super.cogerCartaCubierta();
        if (carta!=null) {
            getProcesadorMensajes().enviarObjeto(Codigos.BIEN, this.s);
            getProcesadorMensajes().enviarObjeto(carta, this.s);
        }
        getProcesadorMensajes().enviarObjeto(Codigos.MAL, this.s);
        return carta;
    }

    protected Carta cogerCartaDecubierta() {
        Carta carta=super.cogerCartaDecubierta();
        if (carta!=null) {
                getProcesadorMensajes().enviarObjeto(Codigos.BIEN, this.s);
                getProcesadorMensajes().enviarObjeto(carta, this.s);
        } else getProcesadorMensajes().enviarObjeto(Codigos.MAL, this.s);
        return carta;
    }

    protected boolean echarCarta(int carta) {
        if (super.echarCarta(carta)) {
            getProcesadorMensajes().enviarObjeto(Codigos.BIEN, this.s);
            return true;
            }
        getProcesadorMensajes().enviarObjeto(Codigos.MAL, this.s);
        return false;
    }

    /*protected boolean meterCarta(int carta,int destino){
        if (super.meterCarta(carta,destino)) {
            getProcesadorMensajes().enviarObjeto(Codigos.BIEN, this.s);
            return true;
        }
        getProcesadorMensajes().enviarObjeto(Codigos.MAL, this.s);
        return false;
    }*/

    protected boolean cerrar(int carta) {
        if (super.cerrar(carta)) {
            getProcesadorMensajes().enviarObjeto(Codigos.BIEN, this.s);
            return true;
        }
        getProcesadorMensajes().enviarObjeto(Codigos.MAL, this.s);
        return false;
    }
    protected boolean moverMano(int i, int j) {
        if (super.moverMano(i, j)) {
            getProcesadorMensajes().enviarObjeto(Codigos.BIEN, this.s);
            return true;
        }
        getProcesadorMensajes().enviarObjeto(Codigos.MAL, this.s);
        return false;
    }

    public void recibirMensaje(String mensaje){
        this.hilo.execute(new Runnable() {
            @Override
            public void run() {
                try {
                    System.out.println("Quiero mandar un mensaje");
                    semaforo.acquire();
                    System.out.println("Permisos: "+semaforo.availablePermits()+" Solicito inicio para mandar mensaje: "+mensaje);
                    getProcesadorMensajes().abrirComunicacion(s);
                    System.out.println("Mensaje enviado a: "+getNombre());
                    //Aviso que voy a enviar un mensaje de texto
                    getProcesadorMensajes().enviarObjeto(Codigos.MENSAJE,s);
                    //Considero la posibilidad de que el cliente hubiera iniciado una comunicación
                    Object objeto=getProcesadorMensajes().recibirObjeto(s);
                    //Si es un String se ha producido una colisión
                    if(objeto instanceof String){
                        System.out.println("Colisión. Guardando en el buffer");
                        //Meto el String en el buffer
                        buffer= (String) objeto;
                        //Leo el código que debería llegar a continuación
                        objeto=getProcesadorMensajes().recibirCodigo(s);
                    }
                    //Si el cliente me notifica que va bien lo mando
                    if(objeto==Codigos.BIEN){
                        getProcesadorMensajes().enviarObjeto(mensaje,s);
                    }
                    else System.out.println("Mensaje rechazado por "+getNombre());
                } catch (InterruptedException ignored) {

                } catch (ReinicioEnComunicacionExcepcion e) {

                } finally {
                    if(!getProcesadorMensajes().libreComunicacion(s))
                        getProcesadorMensajes().cerrarComunicacion(s);
                    semaforo.release();
                }
            }
        });

    }

    protected boolean crearIA(String nombre){
        if (super.crearIA(nombre)) {
            getProcesadorMensajes().enviarObjeto(Codigos.BIEN, this.s);
            return true;
        }
        getProcesadorMensajes().enviarObjeto(Codigos.MAL, this.s);
        return false;
    }

}
