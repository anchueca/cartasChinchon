package servidor;

import cliente.excepciones.NumeroParametrosExcepcion;
import modeloDominio.Codigos;
import modeloDominio.EstadoPartida;
import modeloDominio.ProcesadorMensajes;
import modeloDominio.baraja.Carta;
import modeloDominio.baraja.Mano;
import modeloDominio.baraja.Tamano;

import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

//@ApplicationPath("/SuperJuego")
//@Path("/Chinchon")
public class Servidor extends Thread{

    /*
Propiedades estáticas
 */
    private static final int MAXPartidas = 32;
    private static final Map<String, Partida> partidas = new HashMap<>();
    private static int PartidasActivas = 0;
    private final Socket s;

    public Servidor(Socket s) {
        this.s=s;

    }

    public static void main(String[] args) {
        try (ServerSocket serverSocket=new ServerSocket(55555);){
            ExecutorService executor= Executors.newCachedThreadPool();
            while(!Thread.currentThread().isInterrupted()){
                try{
                    executor.execute(new Servidor(serverSocket.accept()));
                }catch (IOException e){
                    e.printStackTrace();
                }
            }
            executor.shutdown();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void run(){
        while(true){//Hay que meter algo para cuando se salga de la partida se vuelva aquí
            try {
                if (!this.procesarInstrccion(ProcesadorMensajes.recibirString(this.s)))
                    ProcesadorMensajes.enviarCodigo(this.s, Codigos.MAL);
            } catch (NumeroParametrosExcepcion e) {
                ProcesadorMensajes.enviarCodigo(this.s, Codigos.MAL);
            }
        }
    }

    /*Proceso los mensajes que llegan al servidor*/
    public boolean procesarInstrccion(String instruccion) throws NumeroParametrosExcepcion {
        String[] palabras = instruccion.split("\\s+");
            switch (palabras[0]) {
                case "entrar": {
                    if (palabras.length != 3) throw new NumeroParametrosExcepcion();
                    this.entrarPartida(palabras[1], palabras[2]);
                    break;
                }
                case "crear": {
                    if (palabras.length != 2) throw new NumeroParametrosExcepcion();
                    this.crearPartida(palabras[1]);
                    break;
                }
                case "salir": {

                    break;
                }
                case "ayuda": {
                    //"Mostrando ayuda " + (this.acciones.enPartida() ? "inicio" : "juego");
                    break;
                }
                case "partidas": {
                    this.getPartidas();
                    break;
                }
                default:
                    return false;
            }
        return true;

    }

    ///////////GESTIÓN//////////////

    /*
   Devuelve la lista de  nombre de partidas
    */

    public boolean getPartidas() {
        ProcesadorMensajes.enviarCodigo(this.s,Codigos.BIEN);
        ProcesadorMensajes.enviarObjeto(new ArrayList<>(Servidor.partidas.keySet()),this.s);
        return true;
    }

    /*
    Crea una nueva partida vacía
     */

    public boolean crearPartida(String nombre) {
        if (Servidor.PartidasActivas < Servidor.MAXPartidas && this.buscarPartida(nombre) == null) {
            ProcesadorMensajes.enviarCodigo(this.s,Codigos.BIEN);
            Servidor.partidas.put(nombre, new Partida(nombre, Tamano.NORMAL));
            Servidor.PartidasActivas++;
            return true;
        }
        ProcesadorMensajes.enviarCodigo(this.s,Codigos.MAL);
        return false;
    }

    /*
    Se une a aprtida
     */
    @POST
    @Path("partida/{nombre}/{jugador}")
    public boolean entrarPartida(String nombre, String jugador) {
        Partida partida = this.buscarPartida(nombre);
        if (partida == null) {
            ProcesadorMensajes.enviarCodigo(this.s, Codigos.MAL);
            return false;
        }
        partida.nuevoHumano(jugador,this.s);
        ProcesadorMensajes.enviarCodigo(this.s, Codigos.BIEN);
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
            if (partida.expulsarJugador(nombreJugador) && partida.numJugadores() == 0) {//reviar condicion ¿Necesario?
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

    @GET
    //@Path("partida/{partida]")
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

    //@POST
    //@Path("partida/{partida]/{jugador}/{jugada}")
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

//    @GET
//    @PATH("partida/{partida}/{jugador}/{mano}")
}