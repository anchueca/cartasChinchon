package servidor;

import cliente.excepciones.NumeroParametrosExcepcion;
import modeloDominio.Codigos;
import modeloDominio.EstadoPartida;
import modeloDominio.ProcesadorMensajes;
import modeloDominio.baraja.Carta;
import modeloDominio.baraja.Mano;
import modeloDominio.baraja.Tamano;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Servidor extends Thread{

    /*
Propiedades estáticas
 */

    //PENDIENTE GESTIONAR PARTIDAS VACÍAS. EN ESPECIAL LAS NUEVAS
    private static final int MAXPartidas = 32;
    private static final Map<String, Partida> partidas = new HashMap<>();
    private static int PartidasActivas = 0;
    private final Socket s;
    public Servidor(Socket s) {
        this.s=s;
    }

    ////////ENTRADA SERVIDOR////////

    public static void main(String[] args) {
        try (ServerSocket serverSocket=new ServerSocket(55555);){
            ExecutorService executor= Executors.newCachedThreadPool();
            while(!Thread.currentThread().isInterrupted()){
                try{
                    System.out.println("Conexión recibida");
                    //Crea una instancia de la clase que atiende al cliente
                    executor.execute(new Servidor(serverSocket.accept()));
                }catch (IOException e){
                    e.printStackTrace();
                }
            }
            System.out.println("Servidores suspendido.");
            executor.shutdown();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /////////ENTRADA DEL HILO PARA ATENDER CLIENTE ////////////////

    public void run(){
        String mensaje;
        try{
            while(true){//Hay que meter algo para cuando se salga de la partida se vuelva aquí
                //Recibo una cadena del cliente
                mensaje=ProcesadorMensajes.recibirString(this.s);
                if(mensaje==null){//Si es nulo es porque se ha cerrado el socket o se ha producido algún error
                    this.s.close();
                    System.out.println("Socket cerrado en el cleinte. Cierrde de conexión.");
                    return;
                }
                //Proceso la cadena recibida. Si se une a una partida el hilo queda "capturado" hasta que lo "suelte"
                this.procesarInstrccion(mensaje);
            }
        }catch (IOException e) {
            System.out.println("No se ha podido cerrar el socket correctamente.");
            e.printStackTrace();
        }

    }

    /*Proceso los mensajes que llegan al servidor*/
    public Codigos procesarInstrccion(String instruccion) {
        String[] palabras = instruccion.split("\\s+");
        try{
            switch (palabras[0]) {
                case "entrar": {
                    if (palabras.length != 3) throw new NumeroParametrosExcepcion();
                    this.entrarPartida(palabras[1], palabras[2]);
                    break;
                }
                case "crear": {
                    if (palabras.length != 3) throw new NumeroParametrosExcepcion();
                    this.crearPartida(palabras[1],palabras[2]);
                    break;
                }
                case "salir": {

                    break;
                }
                case "partidas": {
                    this.getPartidas();
                    break;
                }
                default:
                    return Codigos.INEXISTENTE;
            }
            return Codigos.BIEN;
        }catch (NumeroParametrosExcepcion e){
            return Codigos.MAL;
        }
    }

    ///////////GESTIÓN//////////////

    /*
   Devuelve la lista de  nombre de partidas
    */

    public boolean getPartidas() {
        ProcesadorMensajes.enviarObjeto(Codigos.BIEN,this.s);
        ProcesadorMensajes.enviarObjeto(new ArrayList<>(Servidor.partidas.keySet()),this.s);
        return true;
    }

    /*
    Crea una nueva partida vacía
     */

    public boolean crearPartida(String nombre,String tamano) {
        try{
            if (Servidor.PartidasActivas < Servidor.MAXPartidas && this.buscarPartida(nombre) == null) {
                Servidor.partidas.put(nombre, new Partida(nombre, Tamano.valueOf(tamano.toUpperCase())));
                Servidor.PartidasActivas++;
                ProcesadorMensajes.enviarObjeto(Codigos.BIEN,this.s);
                return true;
            }
            ProcesadorMensajes.enviarObjeto(Codigos.MAL,this.s);
            return false;
        }catch (IllegalArgumentException e){
            ProcesadorMensajes.enviarObjeto(Codigos.MAL,this.s);
            return false;
        }

    }

    /*
    Se une a partida y "captura" el hilo.
     */
    public boolean entrarPartida(String nombre, String jugador) {
        Partida partida = this.buscarPartida(nombre);
        if (partida == null) {
            ProcesadorMensajes.enviarObjeto(Codigos.MAL,this.s);
            return false;
        }
        partida.nuevoHumano(jugador,this.s);
        ProcesadorMensajes.enviarObjeto(Codigos.BIEN,this.s);
        return true;
    }


    public Partida buscarPartida(String nombre) {
        return Servidor.partidas.get(nombre);
    }

    //////////////////////Información partida//////////////////

    public boolean abandonarPartida(String nombre, String nombreJugador) {
        Partida partida = this.buscarPartida(nombre);
        if (partida == null) return false;
        else {
            //Si no hay más gente en la sala se elimina
            //HABRÍA QUE IGNORAR LAS IAS
            if (partida.expulsarJugador(nombreJugador) && partida.numJugadores() == 0) {
                Servidor.partidas.remove(nombre);
                Servidor.PartidasActivas--;
                return true;
            }
            return false;
        }
    }


    public List<String> listaJugadores(String nombre) {
        Partida partida = this.buscarPartida(nombre);
        if (partida == null) ;
        else {
            return partida.getJugadoresS();
        }
        return null;
    }

    public EstadoPartida estadoPartida(String nombre) {
        Partida partida = this.buscarPartida(nombre);
        if (partida == null) ;
        else {
            return partida.getEstado();
        }
        return null;
    }

    public boolean partidaActualizada(String nombre, String jugador) {//por implementar
        Partida partida = this.buscarPartida(nombre);
        //partida.;
        return partida != null;

    }

    public Mano verMano(String nombre, String jugador) {
        Partida partida = this.buscarPartida(nombre);
        if (partida == null) return null;
        else {
            return partida.getMano(jugador);
        }
    }

    public Carta verCartaDescubierta(String nombre) {
        Partida partida = this.buscarPartida(nombre);
        if (partida == null) return null;
        else {
            return partida.getDescubierta();
        }
    }


    /////////////ACCIONES////////////////

    public boolean jugada(String partida, String jugador, String jugada) {
        return false;
    }


    public boolean iniciarPartida(String nombre, String nombreJugador) {
        Partida partida = this.buscarPartida(nombre);
        if (partida == null) return false;
        else {
            return partida.iniciarPartida(nombreJugador);
        }
    }

    public void ordenarMano(String nombre, String jugador) {
        Partida partida = this.buscarPartida(nombre);
        if (partida == null) {
        }
        else {
            partida.ordenarMano(jugador);
        }
    }

}