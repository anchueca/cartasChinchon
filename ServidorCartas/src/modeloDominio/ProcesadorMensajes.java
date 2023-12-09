package modeloDominio;

import cliente.RecibeObjetos;
import modeloDominio.excepciones.ReinicioEnComunicacionExcepcion;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.Date;
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

    Problema de concurrencia: si dos hilos usan el mismo socket sus comunicaciones podrían mezclarse. Es
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
    usar un semáforo para cada socket y añadir los métodos abrirComunicacion, cerrarComunicacion y libreConexion
     en ProcesadorMensajes. Así, se puede reservar el socket y esperan los demás intentos de comunicación.
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

    public void abrirComunicacion(Socket s) throws InterruptedException {
        System.out.println("Solicito comunicación");
        //Si ya está en uso espera a que se libere
        this.getSemaforo(s).acquire();
        System.out.println("///////////////////////////////////");
        System.out.println("Inicio comunicación");

    }
    public void cerrarComunicacion(Socket s) {
        //Si está sin cerrar se cierra
        if(libreComunicacion(s))return;
        this.getSemaforo(s).release();
        System.out.println("Fin comunicación");
        System.out.println("///////////////////////////////////");
    }
    public boolean libreComunicacion(Socket s){
        return this.getSemaforo(s).availablePermits()==1;
    }

    public boolean enviarObjeto(Object objeto, Socket s) {
        // Escribir el documento XML en el OutputStream
        boolean i = false;
        try {
            System.out.print(new Date()+" Mandando el elemento "+objeto);
            ObjectOutputStream out = this.getObjectOutputStream(s);
            out.writeObject(objeto);
            i = true;
            System.out.println(". Objeto enviado ");
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return i;
    }

    public String recibirString(Socket s) throws ClassCastException, ReinicioEnComunicacionExcepcion {
        return (String) this.recibirObjeto(s);
    }

    public Object recibirObjeto(Socket s) throws ReinicioEnComunicacionExcepcion {
        try {
            System.out.println(RecibeObjetos.getRecibeObjetos()==Thread.currentThread()?new Date()+" RecogeObjetos esperando objeto":new Date()+" Esperando objeto");
            ObjectInputStream in = this.getObjectInputStream(s);
            Object objeto;
            objeto = in.readObject();
            System.out.println("Elemento recibido: " + objeto);
            if(objeto==Codigos.REINICIO)throw new ReinicioEnComunicacionExcepcion();
            return objeto;

        } catch (IOException e) {
            // TODO Auto-generated catch block
            return null;
        } catch (ClassNotFoundException e) {
            System.out.println("Clase no encontrada");
            return null;
        }
    }

    public Codigos recibirCodigo(Socket s) throws ReinicioEnComunicacionExcepcion {
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
