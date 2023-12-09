package cliente;

import cliente.interfaz.ConsolaBonita;
import modeloDominio.Codigos;
import modeloDominio.EstadoPartida;
import modeloDominio.ProcesadorMensajes;
import modeloDominio.baraja.Carta;
import modeloDominio.baraja.Mano;
import modeloDominio.excepciones.ReinicioEnComunicacionExcepcion;

import java.io.IOException;
import java.net.Socket;
import java.util.List;
import java.util.Map;

/*
Implementación de las acciones asociadas a la presentación de consolaBonita
 */

public class EjecutorConsolaBonita implements AccionesConsola,RecibeMensajesI{

    private final Socket s;
    private final ConsolaBonita consolaBonita;
    /////Atributos//////
    private PartidaCliente partida;

    //////Contructores/////////
    public EjecutorConsolaBonita(PartidaCliente partida, ConsolaBonita consolaBonita) throws IOException {
        this.s = new Socket("localhost", 55555);
        RecibeObjetos.getRecibeObjetos(this.s);
        RecibeObjetos.getRecibeObjetos().setReceptor(this);
        this.consolaBonita = consolaBonita;
        this.partida = partida;
    }

    public EjecutorConsolaBonita(ConsolaBonita consolaBonita) throws IOException {
        this(null, consolaBonita);
    }

    ///////Getters y setters//////////
    public boolean enPartida() {
        return this.partida != null;
    }

    public EstadoPartida estadoPartida() {
        try {
            return this.partida.verEstadoPartida();
        } catch (ReinicioEnComunicacionExcepcion e) {
            return this.estadoPartida();
        }
    }

    ////////Accioens de gestión (fuera de partida)///////////////

    /*
    Gestiona la entrada a la partida
     */
    public void unirsePartida(String partida, String jugador) {

        try {
            //Manda mensaje al servidor
            ProcesadorMensajes.getProcesadorMensajes().enviarObjeto("entrar " + partida + " " + jugador, this.s);
            //Si la respuesta del servidor es correcta
            if (RecibeObjetos.getRecibeObjetos().recibirObjeto() == Codigos.BIEN) {
                //Inicia la partida en el cliente
                this.setPartida(new PartidaCliente(partida, jugador, this.s,this));
                //Actualiza la interfaz
                this.consolaBonita.setPartida(partida);
                this.consolaBonita.setJugador(jugador);
                this.consolaBonita.meterSalida("Entando...");
                //this.estado();
            } else this.consolaBonita.meterSalida("No se ha podido entrar");
        } catch (ReinicioEnComunicacionExcepcion e) {
            this.unirsePartida(partida, jugador);
        }
    }

    /*
    Crea una partida en el servidor, pero no se une a ella
     */
    public void crearPartida(String partida, String baraja) {
        try {
            //Por defecto la baraja es la NORMAL
            if (baraja.isEmpty()) baraja = "NORMAL";
            //Envía el mensaje al servidor
            ProcesadorMensajes.getProcesadorMensajes().enviarObjeto("crear " + partida + " " + baraja, this.s);
            //Si la respuesta del servidor es correcta
            if (RecibeObjetos.getRecibeObjetos().recibirObjeto() == Codigos.BIEN)
                this.consolaBonita.meterSalida("Partida creada");
            else this.consolaBonita.meterSalida("No se ha podido crear");
        } catch (ReinicioEnComunicacionExcepcion e) {
            this.crearPartida(partida,baraja);
        }
    }

    /*
    Realiza las operaciones asociadas a entrar a una partida en la interfaz de usuario
     */
    public void setPartida(PartidaCliente partida) {
        String cadena;
        //Caso de salir de la partida
        if (partida == null) {
            this.partida = null;
            this.consolaBonita.setPartida("");
            this.consolaBonita.setJugador("");
            cadena = "Abandonando partida...";
        }
        //Entrada a la partida
        else {
            this.partida = partida;
            this.consolaBonita.setPartida("");
            this.consolaBonita.setJugador("");
            cadena = "Cargando partida";
        }
        //Limpio la pantalla
        this.consolaBonita.limpiarPantalla();
        //Muestro por pantalla el texto
        this.consolaBonita.meterSalida(cadena);
    }

    /*
    Muetsra por pantalla las partidas del servidor
     */
    public void listaPartidas() {
        try {
            //Envío el mensaje al servidor
            ProcesadorMensajes.getProcesadorMensajes().enviarObjeto("partidas", this.s);
            String n = "Lista partidas:\n";
            //Si la respuesta el correcta
            if (RecibeObjetos.getRecibeObjetos().recibirObjeto() == Codigos.BIEN) {
                //Recojo la lista que envía el servidor
                List<String> lista = (List<String>) RecibeObjetos.getRecibeObjetos().recibirObjeto();
                //Si es nulo es por algún error inesperado
                if(lista==null)n="No se han podido recibir las partidas";
                else for (int i = 0, j = lista.size(); i < j; i++) {
                    n += i + ".- " + lista.get(i) + "\n";
                }
            }
            //Muestro el texto
            this.consolaBonita.meterSalida(n);
        } catch (ReinicioEnComunicacionExcepcion e) {
            this.listaPartidas();
        }

    }
/*
Salida del programa
 */
    public void salir() {
        this.consolaBonita.dispose();
        this.consolaBonita.meterSalida("Saliendo...");
        try {
            this.s.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    ///////////CONSULTA DE DATOS (dentro de la partida)////////////
    /*
    Muestra por pantalla el conjunto de jugadores en la partida
     */
    public void listaJugadores() {
        String cadena = "Juagdores: ";
        try {
            for (String ca : this.partida.listaJugadores()
            ) {
                cadena += ca + " ";
            }
            this.consolaBonita.meterSalida(cadena);
        } catch (ReinicioEnComunicacionExcepcion e) {
            this.listaJugadores();
        }


    }
/*
Muestra un resumen del estado actual de la partida
 */
    public void verResumen() {
        try {
            this.consolaBonita.meterSalida("Nombre partida: "+this.partida.getNombrePartida()+" "
                    +this.partida.verEstadoPartida().toString()+this.partida.verFasePartida() +"\nJugador:"+this.partida.getNombreJuagador());
            this.verTurno();
            this.puntuaciones();
            this.verMano();
        } catch (ReinicioEnComunicacionExcepcion e) {
            this.verResumen();
        }

    }

/*
Muestra por pantalla la información principal relativa a la partida
 */
    public void pintarPartida() {
        //this.consolaBonita.limpiarPantalla();
        String salida = "";
        //Solo tiene sentido una vez empezada la partida
        try {
            if (this.partida.verEstadoPartida() == EstadoPartida.ENCURSO) {
                //pinto la mano
                this.verMano();
                //Muestra la carta descubierta
                Carta descubierta = this.partida.verCartaDescubierta();
                salida += "\nDescubierta: " + (descubierta == null ? "Nada" : descubierta);
            } else salida = "La partida no está en curso";
            this.consolaBonita.meterSalida(salida);
        } catch (ReinicioEnComunicacionExcepcion e) {
            this.pintarPartida();
        }
        //Muestro por pantalla

    }
/*
Muestra por pantalla las cartas de la mano y las parejas y escaleras formadas
 */
    public void verMano() {
        String salida = "";
        //Solo tienne sentido una vez empezada la partida
        try {
            if (this.partida.verEstadoPartida() == EstadoPartida.ENCURSO) {
                salida += "Cartas: ";
                //Obtengo la mano
                Mano mano = this.partida.verMano();
                if (mano != null) for (int i = 0, j = mano.numCartas(); i < j; i++) {
                    salida += "\n" + i + ".-(" + mano.verCarta(i) + ")";
                }
                salida += "\n\nCasadas:";
                //Muestro las cartas casadas
                for (Byte bite : Mano.cartasCasadas(mano)
                ) {
                    salida += "\n" + bite.toString();
                }
            } else salida = "La partida no está en curso";
            this.consolaBonita.meterSalida(salida);
        } catch (ReinicioEnComunicacionExcepcion e) {
            this.verMano();
        }
        //Muestra por pantalla

    }
/*
Muestra por pantalla la tabla de puntuaciones
 */
    public void puntuaciones() {
        Map<String, Integer> mapa = null;
        try {
            mapa = this.partida.verPuntuaciones();
            String ca = "Puntuaciones:";
            for (String nombre : mapa.keySet()
            ) {
                ca += "\n" + nombre + ": " + mapa.get(nombre);
            }
            this.consolaBonita.meterSalida(ca);
        } catch (ReinicioEnComunicacionExcepcion e) {
            this.puntuaciones();
        }

    }

    /*
    Muestra por pantalla de quién es el turno
     */
    public void verTurno(){
        try {
            String turno=this.partida.verTurno();
            if (this.partida.verEstadoPartida()!=EstadoPartida.ENCURSO || turno==null) {
                this.consolaBonita.meterSalida("No es posible consultar el turno");
            } else this.consolaBonita.meterSalida("Turno: r"+(turno.compareTo(this.partida.getNombreJuagador())==0?"Te toca":turno));
        } catch (ReinicioEnComunicacionExcepcion e) {
            this.verTurno();
        }
    }
/*
Envía al resto de jugadores un mensaje. Es parte de un chat primitivo
 */
    @Override
    public void enviarChat(String texto) {
        if(this.partida==null)this.consolaBonita.meterSalida("No hay nadie que te escuche");
        else {
            try {
                this.partida.enviarChat(texto);
            } catch (ReinicioEnComunicacionExcepcion e) {
                this.enviarChat(texto);
            }
        }
    }

    @Override
    public void verAnfitrion() {
        String anfitrion= null;
        try {
            anfitrion = this.partida.verAnfitrion();
            this.consolaBonita.meterSalida("Anfitrion: "+anfitrion==null?"Desconocido":anfitrion);
        } catch (ReinicioEnComunicacionExcepcion e) {
            this.empezar();
        }
    }

    //////////ACCIOENES DE JUEGO/////////////

    /*
    Inicia la partida. Solo puede hacerlo el anfitrión. En caso contrario devolverá mal
     */
    public void empezar() {
        try {
            if (this.partida.empezarPartida()) {
                this.consolaBonita.meterSalida("Partida iniciada correctamente");
                this.verResumen();
            } else this.consolaBonita.meterSalida("No se ha podido iniciar");
        } catch (ReinicioEnComunicacionExcepcion e) {
            this.empezar();
        }
    }

    /*
    Solicita al servidor que ordene la mano
     */
    public void ordenar() {
        try {
            if (this.partida.ordenar()) this.pintarPartida();
            else this.consolaBonita.meterSalida("No se ha podido ordenar");
        } catch (ReinicioEnComunicacionExcepcion e) {
            this.ordenar();
        }
    }
/*
Lanza la carta iésima de la mano
 */
    public void echar(int i) {//por refinar

        try {
            Mano mano = this.partida.verMano();
            if (this.partida.echarCarta(i)==Codigos.BIEN) {
                mano.tomarCarta(i);
                this.consolaBonita.meterSalida("OK");
            } else this.consolaBonita.meterSalida("Jugada no válida");
        } catch (ReinicioEnComunicacionExcepcion e) {
            this.echar(i);
        }
    }
/*
Abandona la partida si el servidor te deja (lo hará)
 */
    public void salirPartida() {
        try {
            if (this.partida.salir()) {
                this.setPartida(null);
                this.consolaBonita.meterSalida("saliendo de la partida");
            } else this.consolaBonita.meterSalida("No es posible abandonar la partida");
        } catch (ReinicioEnComunicacionExcepcion e) {
            this.salirPartida();
        }
    }
/*
Permuta las cartas iésima y jotaésima de la mano
 */
    public void mover(int i, int j) {
        try {
            if (this.partida.moverMano(i, j)) this.consolaBonita.meterSalida("Movimiento efectuado");
            else this.consolaBonita.meterSalida("Movimiento incorrecto");
        } catch (ReinicioEnComunicacionExcepcion e) {
            this.mover(i,j);
        }
    }

    /*
    Cierra la partida con la iésima carta (si es legal)
     */
    public void cerrar(int i) {//SIN IMPLEMENTAR

    }

/*
Toma la carta cubierta. Por supuesto, solo si es legal
 */
    public void coger() {
        this.coger("0");
    }
/*
Gestiona qué carta se echar a partir de la cadena opción: vubierta/descubierta
 */
    public void coger(String opcion) {
        //Compruebo cuál de las opciones es
        try {
            Carta carta;
            if(opcion.compareTo("descubierta") == 0)carta=this.cogerCartaDecubierta();
            else if(opcion.compareTo("cubierta") == 0)carta=this.cogerCartaCubierta();
            else {
                this.consolaBonita.meterSalida("Opción desconocida");
                return;
            }
            if(carta==null)this.consolaBonita.meterSalida("Jugada inválida");
            else this.consolaBonita.meterSalida("Carta obtenida: "+carta);
        }catch (ReinicioEnComunicacionExcepcion e){
            this.coger(opcion);
        }

    }
/*
Toma la carta descubierta
 */
    public Carta cogerCartaDecubierta() throws ReinicioEnComunicacionExcepcion {
        return this.partida.cogerCartaDecubierta();
    }
/*
Toma la carta cubierta
 */
    public Carta cogerCartaCubierta() throws ReinicioEnComunicacionExcepcion {
        return this.partida.cogerCartaCubierta();
    }


    /*
    Recibe los datos sin esperar leer el código de mensaje que lo recibe como parámetro
     */
    public boolean recibirMensaje(Codigos codigo){
        String cadena;
        try {
            //Si es el recogeObjetos no hace falta que reserve la conexión
            if(Thread.currentThread()!=RecibeObjetos.getRecibeObjetos())
                ProcesadorMensajes.getProcesadorMensajes().abrirComunicacion(this.s);
            //Si todavía no hay partida se rechaza el mensaje
            if(this.partida==null)ProcesadorMensajes.getProcesadorMensajes().enviarObjeto(Codigos.MAL,this.s);
            else {
                //Informo al servidor que se ha recibido la petción correctamente
                ProcesadorMensajes.getProcesadorMensajes().enviarObjeto(Codigos.BIEN,this.s);
                //Si la respuesta no es nula la imprimo or pantalla
                if ((cadena=this.partida.procesarMensaje(codigo))!=null) {
                    this.consolaBonita.meterSalida(cadena);
                    return true;
                }
            }
//Si ae produce un error devuelvo mal
        } catch (InterruptedException| ClassCastException e) {
            ProcesadorMensajes.getProcesadorMensajes().enviarObjeto(Codigos.MAL,this.s);
            return false;
        }
        //Finalmente se cierra la conexión
        catch (ReinicioEnComunicacionExcepcion ignored) {

        } finally {
            if(Thread.currentThread()!=RecibeObjetos.getRecibeObjetos())
                ProcesadorMensajes.getProcesadorMensajes().cerrarComunicacion(this.s);
        }
        return false;
    }
    /*
    Crea un jugador IA en la partida. Solo se puede si se es anfitrión
     */
    public void crearIA(String nombre){
        try {
            if (this.partida.crearIA(nombre)) this.consolaBonita.meterSalida("IA creada");
            else this.consolaBonita.meterSalida("No se ha podido crear");
        } catch (ReinicioEnComunicacionExcepcion e) {
            this.crearIA(nombre);
        }
    }

    /////////////OTROS///////////////
    /*
    Salida forzada cuando el servidor no permite abandonar la partida (lo he usado durante el desarrollo)
     */
    public void salirForzado() {
        this.salirPartida();
        if (this.partida != null) {
            this.setPartida(null);
            this.consolaBonita.meterSalida("Salida forzada");
        }
    }

}
