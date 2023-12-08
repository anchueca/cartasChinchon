package modeloDominio;

import cliente.RecibeObjetos;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Semaphore;

public class ProcesadorMensajes {
    /*

    Esta clase sirve para no tener que estar continuamente creando los ObjectStream y, principalmente, para ahorrarme
    escribir código.

    Cacheo los socket. Por ahora no limpia.

    Cada acceso buscará y añadirá su propio socket. Desconozco si acceder a un dato durante una inserción
    podría dar problemas. En cualquier caso, he empleado bloques synchronized para asegurarme

    Realmente este sistema de cacheo solo tiene sentido en el lado del servidor. El cliente solo tiene un socket aunque en el futuro, si el
    cliente padece de ludopatía podría habilitarse jugar varias partidas a la vez (con distintos sockets).

    Problema de concurrencia por solucionar: si dos hilos usan el mismo socket sus comunicaciones podrían mezclarse. Es
    necesario bloquear los streams asociados al socket DURANTE TODA LA COMUNICACIÓN.

    NOTA: Sincronizar los métodos de esta clase impediría a dos hilos usar sockets diferentes simultáneamente, lo que
    lastraría el rendimiento del servidor en cuanto a comunicaciones, pues solo podría comunicarme con un cliente
    simultáneamente.

    ACTUALIZACIÓN 4/12/2023:He pensado en hacer lock del socket durante toda el transcurso de la comunicación para
    evitar que se entremezclen otros mensajes. Este sistema debe aplicarse tanto en el cliente como en el servidor.
    Si otro hilo quiere usarlo tendrá que esperar para tomar el lock.

    También he añadido el lock en el propio enviar. Así si el socket está cogido y el hilo no respeta el lock
    (si se me ha olvidado pedirlo) también quedaría bloqueado aunque no la comunicación que iniciase.

    ACTUALIZACIÓN 8/12/2023: He repensado la cuestión del lock con sincronización y he optado por una solución mejor:
    usar un semáforo para cada socket y añadir los métodos abrirComunicacion y cerrarComunicacion en ProcesadorMensajes.
    Así, se puede reservar el socket y se abortan los demás intentos de comunicación.
     */

    private static ProcesadorMensajes procesadorMensajes;
    private final Map<Socket, Object[]> streams;

    private ProcesadorMensajes() {
        this.streams = new HashMap<>();
    }

    public static synchronized ProcesadorMensajes getProcesadorMensajes() {
        if (ProcesadorMensajes.procesadorMensajes == null) {
            ProcesadorMensajes.procesadorMensajes = new ProcesadorMensajes();
        }
        return ProcesadorMensajes.procesadorMensajes;
    }

    private ObjectOutputStream getObjectOutputStream(Socket s) throws IOException {
        Object[] streams;
        if ((streams = this.streams.get(s)) == null) {
            this.streams.put(s,this.nuevoSocket(s));
            System.out.println("Elemento cacheado: " + s);
        } else return (ObjectOutputStream) streams[0];
        return this.getObjectOutputStream(s);
    }

    //Si está lo uso, si no lo creo. Es como un singleton
    private ObjectInputStream getObjectInputStream(Socket s) throws IOException {
        Object[] streams;
        if ((streams = this.streams.get(s)) == null) {
            this.streams.put(s,this.nuevoSocket(s));
            System.out.println("Elemento cacheado: " + s);
        } else return (ObjectInputStream) streams[1];
        return this.getObjectInputStream(s);
    }

    private synchronized Object[] nuevoSocket(Socket s) throws IOException {
        return new Object[]{new ObjectOutputStream(s.getOutputStream()),
                new ObjectInputStream(s.getInputStream()),new Semaphore(1)};
    }
    private Semaphore getSemaforo(Socket s){
        return (Semaphore) this.streams.get(s)[2];
    }

    public void abrirConexion(Socket s) throws InterruptedException {
        this.getSemaforo(s).acquire();
        System.out.println("///////////////////////////////////");
        System.out.println("Inicio comunicación");

    }
    public void cerrarConexion(Socket s) {
        this.getSemaforo(s).release();
        System.out.println("Fin comunicación");
        System.out.println("///////////////////////////////////");
    }
    public boolean libreConexcion(Socket s){
        return this.getSemaforo(s).availablePermits()==1;
    }

    public boolean enviarObjeto(Object objeto, Socket s) {
        // Escribir el documento XML en el OutputStream
        boolean i = false;
        try {
            System.out.print("Mandando el elemento "+objeto);

            ObjectOutputStream out = this.getObjectOutputStream(s);
            out.writeObject(objeto);
            i = true;
            System.out.println(". Objeto enviado ");
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } finally {
            return i;
        }
    }

    public String recibirString(Socket s) {
        Object cadena=this.recibirObjeto(s);
        return (cadena instanceof String)?(String) cadena:"";
    }

    public Object recibirObjeto(Socket s) {
        try {
            System.out.println(RecibeObjetos.getRecibeObjetos()==Thread.currentThread()?"RecogeObjetos esperando objeto":"Esperando objeto");
            ObjectInputStream in = this.getObjectInputStream(s);
            Object objeto;
            objeto = in.readObject();
            System.out.println("Elemento recibido: " + objeto);
            return objeto;

        } catch (IOException e) {
            // TODO Auto-generated catch block
            return null;
        } catch (ClassNotFoundException e) {
            System.out.println("Clase no encontrada");
            return null;
        }
    }

    public Codigos recibirCodigo(Socket s) {
        System.out.println("Recibir código.");
        Object objeto = this.recibirObjeto(s);
        if (objeto instanceof Codigos) return (Codigos) objeto;
        return null;
    }

    public boolean enEspera(Socket s) {
        try {
            int pendiente=s.getInputStream().available();
            return  pendiente!= 0;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /*
    El stream cachea los objetos. Si le mando una Mano modificada me mandará la anterior.
     */
    public void reset(Socket s) {
        try {
            synchronized (s){this.getObjectOutputStream(s).reset();}
        } catch (IOException e) {
            System.out.println("Error al resetear el ObjectStream");
            e.printStackTrace();
        }
    }

}
