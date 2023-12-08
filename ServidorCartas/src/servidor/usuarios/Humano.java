package servidor.usuarios;

import modeloDominio.Codigos;
import modeloDominio.EstadoPartida;
import modeloDominio.FaseChinchon;
import modeloDominio.ProcesadorMensajes;
import modeloDominio.baraja.Carta;
import modeloDominio.excepciones.NumeroParametrosExcepcion;
import servidor.Partida;

import java.io.IOException;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class Humano extends Jugador {

    private Socket s;
    private boolean salida;
    /*
    Uso el semáforo para controlar las entradas
     */
    public Humano(String nombre, Partida partida, Socket s) {
        super(nombre, partida);
        this.s = s;
        this.salida=false;
    }

    public void receptorHumano() throws IOException {
        String mensaje;
        //Cuando la partida se abandone será null
        while (!this.salida) {
            try {
                //tomo la comunicación
                ProcesadorMensajes.getProcesadorMensajes().abrirConexion(this.s);
            mensaje = ProcesadorMensajes.getProcesadorMensajes().recibirString(this.s);
            if (mensaje == null) {//Si es nulo es porque se ha cerrado el socket o se ha producido algún error
                this.salirForzado();
                //Vuelve a Servidor donde se gestionará el cierre
                return;
            }
            //Proceso la cadena recibida
            this.procesarInstrccion(mensaje);

        }catch (InterruptedException e) {
                salida=true;
                System.out.println("Problema en la comunicación. Método interrumpido");
            }finally {
                if (!ProcesadorMensajes.getProcesadorMensajes().libreConexcion(this.s))
                    ProcesadorMensajes.getProcesadorMensajes().cerrarConexion(this.s);
            }
        }
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
                case "verMano": {
                    this.mandarMano();
                    break;
                }
                case "crearIA": {
                    if (palabras.length > 3) throw new NumeroParametrosExcepcion();
                    if (palabras.length ==2) this.crearIA(palabras[1]);
                    else this.crearIA("");
                    break;
                }
                default:
                    ProcesadorMensajes.getProcesadorMensajes().enviarObjeto(Codigos.INEXISTENTE, this.s);
            }
        } catch (NumeroParametrosExcepcion e) {
            ProcesadorMensajes.getProcesadorMensajes().enviarObjeto(Codigos.MAL, this.s);
        }
    }

    /*
    Manda al cliente el nombre del jugador que ostenta el turno actual
     */
    protected void turno(){
        if (super.ordenarMano()){
            ProcesadorMensajes.getProcesadorMensajes().enviarObjeto(Codigos.BIEN, this.s);
            ProcesadorMensajes.getProcesadorMensajes().enviarObjeto(this.getPartida().getTurno().getNombre(), this.s);
        }
        else ProcesadorMensajes.getProcesadorMensajes().enviarObjeto(Codigos.MAL, this.s);
    }
    private boolean salir() {
        if (this.getPartida().expulsarJugador(this)) {
            ProcesadorMensajes.getProcesadorMensajes().enviarObjeto(Codigos.BIEN, this.s);
            this.salida=true;
            return true;
        }
        ProcesadorMensajes.getProcesadorMensajes().enviarObjeto(Codigos.MAL, this.s);
        return false;
    }

    /*
    Gestiona la desconexión abrupta del cliente
     */
    private void salirForzado() {
        if(!this.salir()){

        }
    }

    private void mandarMano() {
        ProcesadorMensajes.getProcesadorMensajes().enviarObjeto(Codigos.BIEN, this.s);
        ProcesadorMensajes.getProcesadorMensajes().reset(s);
        ProcesadorMensajes.getProcesadorMensajes().enviarObjeto(this.getMano(), this.s);
    }

    private void mandarListaJugadores() {
        ProcesadorMensajes.getProcesadorMensajes().enviarObjeto(Codigos.BIEN, this.s);
        List<Jugador> listaJ=this.getPartida().getJugadores();
        List<String> lista=new ArrayList<>(listaJ.size());
        for (Jugador jugador:listaJ
             ) {
            lista.add(jugador.getNombre());
        }
        ProcesadorMensajes.getProcesadorMensajes().enviarObjeto(lista, this.s);
    }

    private void mandarPuntuaciones() {
        ProcesadorMensajes.getProcesadorMensajes().enviarObjeto(Codigos.BIEN, this.s);
        ProcesadorMensajes.getProcesadorMensajes().enviarObjeto(this.puntuaciones(), this.s);
    }

    private void mandarEstado() {
        ProcesadorMensajes.getProcesadorMensajes().enviarObjeto(Codigos.BIEN, this.s);
        ProcesadorMensajes.getProcesadorMensajes().enviarObjeto(this.getPartida().getEstado(), this.s);
    }

    private void mandarCartaDescubierta() {
        if (this.getPartida().getEstado() == EstadoPartida.ENCURSO && this.getPartida().getFase() == FaseChinchon.ABIERTO) {
            ProcesadorMensajes.getProcesadorMensajes().enviarObjeto(Codigos.BIEN, this.s);
            ProcesadorMensajes.getProcesadorMensajes().enviarObjeto(this.getPartida().getDescubierta(), this.s);
        } else ProcesadorMensajes.getProcesadorMensajes().enviarObjeto(Codigos.MAL, this.s);
    }

    protected boolean ordenarMano() {
        if (super.ordenarMano()){
            ProcesadorMensajes.getProcesadorMensajes().enviarObjeto(Codigos.BIEN, this.s);
            return true;
        }
        else ProcesadorMensajes.getProcesadorMensajes().enviarObjeto(Codigos.MAL, this.s);
        return false;
    }

    private void empezar() {
        if ((this.getPartida().getEstado() == EstadoPartida.ESPERANDO || this.getPartida().getFase() == FaseChinchon.ESPERANDO)
                && this.getPartida().getAnfitrion()==this) {
            ProcesadorMensajes.getProcesadorMensajes().enviarObjeto(Codigos.BIEN, this.s);
            this.getPartida().iniciarPartida();
        } else ProcesadorMensajes.getProcesadorMensajes().enviarObjeto(Codigos.MAL, this.s);
    }

    protected Carta cogerCartaCubierta() {
        Carta carta=super.cogerCartaCubierta();
        if (carta!=null) {
            ProcesadorMensajes.getProcesadorMensajes().enviarObjeto(Codigos.BIEN, this.s);
            ProcesadorMensajes.getProcesadorMensajes().enviarObjeto(carta, this.s);
        }
        ProcesadorMensajes.getProcesadorMensajes().enviarObjeto(Codigos.MAL, this.s);
        return carta;
    }

    protected Carta cogerCartaDecubierta() {
        Carta carta=super.cogerCartaDecubierta();
        if (carta!=null) {
                ProcesadorMensajes.getProcesadorMensajes().enviarObjeto(Codigos.BIEN, this.s);
                ProcesadorMensajes.getProcesadorMensajes().enviarObjeto(carta, this.s);
        } else ProcesadorMensajes.getProcesadorMensajes().enviarObjeto(Codigos.MAL, this.s);
        return carta;
    }

    protected boolean echarCarta(int carta) {
        if (super.echarCarta(carta)) {
            ProcesadorMensajes.getProcesadorMensajes().enviarObjeto(Codigos.BIEN, this.s);
            return true;
            }
        ProcesadorMensajes.getProcesadorMensajes().enviarObjeto(Codigos.MAL, this.s);
        return false;
    }

    protected boolean cerrar(int carta) {
        if (super.cerrar(carta)) {
            ProcesadorMensajes.getProcesadorMensajes().enviarObjeto(Codigos.BIEN, this.s);
            return true;
        }
        ProcesadorMensajes.getProcesadorMensajes().enviarObjeto(Codigos.MAL, this.s);
        return false;
    }
    protected boolean moverMano(int i, int j) {
        if (super.moverMano(i, j)) {
            ProcesadorMensajes.getProcesadorMensajes().enviarObjeto(Codigos.BIEN, this.s);
            return true;
        }
        ProcesadorMensajes.getProcesadorMensajes().enviarObjeto(Codigos.MAL, this.s);
        return false;
    }

    public void recibirMensaje(String mensaje){
        try {
            ProcesadorMensajes.getProcesadorMensajes().abrirConexion(this.s);
            System.out.println("Mensaje enviado a: "+this.getNombre());
        //Aviso que voy a enviar un mensaje de texto
        ProcesadorMensajes.getProcesadorMensajes().enviarObjeto(Codigos.MENSAJE,this.s);
        //Si el cliente me notifica que va bien lo mando
        if(ProcesadorMensajes.getProcesadorMensajes().recibirCodigo(this.s)==Codigos.BIEN){
            ProcesadorMensajes.getProcesadorMensajes().enviarObjeto(mensaje,this.s);
        }
        } catch (InterruptedException e) {
            return;
        }finally {
                if(!ProcesadorMensajes.getProcesadorMensajes().libreConexcion(this.s))
                    ProcesadorMensajes.getProcesadorMensajes().cerrarConexion(this.s);
        }
    }

    protected boolean crearIA(String nombre){
        if (super.crearIA(nombre)) {
            ProcesadorMensajes.getProcesadorMensajes().enviarObjeto(Codigos.BIEN, this.s);
            return true;
        }
        ProcesadorMensajes.getProcesadorMensajes().enviarObjeto(Codigos.MAL, this.s);
        return false;
    }

}
