package servidor;

import modeloDominio.Codigos;
import modeloDominio.ProcesadorMensajes;
import modeloDominio.baraja.Tamano;
import modeloDominio.excepciones.NumeroParametrosExcepcion;
import servidor.usuarios.Humano;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Servidor extends Thread {

    /*
Propiedades estáticas
 */

    //PENDIENTE GESTIONAR PARTIDAS VACÍAS. EN ESPECIAL LAS NUEVAS
    private static final int MAXPartidas = 32;
    private static final Map<String, Partida> partidas = new HashMap<>();
    private static int PartidasActivas = 0;
    private final Socket s;

    public Servidor(Socket s) {
        this.s = s;
    }

    ////////ENTRADA SERVIDOR////////

    public static void main(String[] args) {
        try (ServerSocket serverSocket = new ServerSocket(55555)) {
            ExecutorService executor = Executors.newCachedThreadPool();
            while (!Thread.currentThread().isInterrupted()) {
                try {
                    //Crea una instancia de la clase que atiende al cliente
                    executor.execute(new Servidor(serverSocket.accept()));
                    System.out.println("Conexión recibida");
                } catch (IOException e) {
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

    public void run() {
        String mensaje;
        try {
            //Recibo una cadena del cliente
            //Si es nulo es porque se ha cerrado el socket o se ha producido algún error
            while ((mensaje = ProcesadorMensajes.getProcesadorMensajes().recibirString(this.s)) != null)
                //Proceso la cadena recibida. Si se une a una partida el hilo queda "capturado" hasta que lo "suelte"
                //Al atender una petición no permito otra comnunicación
                synchronized (this.s) {
                    this.procesarInstrccion(mensaje);
                }

            this.s.close();
            System.out.println("Socket cerrado en el cleinte. Cierrde de conexión.");
        } catch (IOException e) {
            System.out.println("No se ha podido cerrar el socket correctamente.");
            e.printStackTrace();
        }

    }

    /*
    Proceso los mensajes que llegan al servidor
    */
    private void procesarInstrccion(String instruccion) throws IOException {
        String[] palabras = instruccion.split("\\s+");
        try {
            switch (palabras[0]) {
                case "entrar": {
                    if (palabras.length != 3) throw new NumeroParametrosExcepcion();
                    this.entrarPartida(palabras[1], palabras[2]);
                    break;
                }
                case "crear": {
                    if (palabras.length != 3) throw new NumeroParametrosExcepcion();
                    this.crearPartida(palabras[1], palabras[2]);
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
                    ProcesadorMensajes.getProcesadorMensajes().enviarObjeto(Codigos.INEXISTENTE, this.s);
            }
        } catch (NumeroParametrosExcepcion e) {
            ProcesadorMensajes.getProcesadorMensajes().enviarObjeto(Codigos.MAL, this.s);
        }
    }

    ///////////GESTIÓN//////////////

    /*
   Devuelve la lista de nombre de partidas
    */

    public void getPartidas() {
        ProcesadorMensajes.getProcesadorMensajes().enviarObjeto(Codigos.BIEN, this.s);
        ProcesadorMensajes.getProcesadorMensajes().enviarObjeto(new ArrayList<>(Servidor.partidas.keySet()), this.s);
    }

    /*
    Crea una nueva partida vacía
     */

    public void crearPartida(String nombre, String tamano) {
        try {
            if (Servidor.PartidasActivas < Servidor.MAXPartidas && this.buscarPartida(nombre) == null) {
                Servidor.partidas.put(nombre, new Partida(nombre, Tamano.valueOf(tamano.toUpperCase())));
                Servidor.PartidasActivas++;
                ProcesadorMensajes.getProcesadorMensajes().enviarObjeto(Codigos.BIEN, this.s);
                return;
            }
            ProcesadorMensajes.getProcesadorMensajes().enviarObjeto(Codigos.MAL, this.s);
        } catch (IllegalArgumentException e) {
            ProcesadorMensajes.getProcesadorMensajes().enviarObjeto(Codigos.MAL, this.s);
        }

    }

    /*
    Se une a partida y "captura" el hilo.
     */
    public void entrarPartida(String nombre, String jugador) throws IOException {
        Partida partida = this.buscarPartida(nombre);
        Humano humano;
        if (partida == null || (humano = partida.nuevoJugador(jugador, this.s)) == null) {
            ProcesadorMensajes.getProcesadorMensajes().enviarObjeto(Codigos.MAL, this.s);
            return;
        }
        ProcesadorMensajes.getProcesadorMensajes().enviarObjeto(Codigos.BIEN, this.s);
        //El objeto Humano "captura" el hilo y se encarga de procesar los mensajes
        humano.receptorHumano();
    }


    public Partida buscarPartida(String nombre) {
        return Servidor.partidas.get(nombre);
    }

}