package modeloDominio;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;

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
            synchronized (this){
                this.streams.put(s,
                        new Object[]{new ObjectOutputStream(s.getOutputStream()), new ObjectInputStream(s.getInputStream())});
            }

            System.out.println("Elemenot cacheado: " + s);
        } else return (ObjectOutputStream) streams[0];
        return this.getObjectOutputStream(s);
    }

    //Si está lo uso, si no lo creo. Es como un singleton
    private ObjectInputStream getObjectInputStream(Socket s) throws IOException {
        Object[] streams;
        if ((streams = this.streams.get(s)) == null) {
            synchronized (this){
                this.streams.put(s,
                        new Object[]{new ObjectOutputStream(s.getOutputStream()), new ObjectInputStream(s.getInputStream())});
            }
            System.out.println("Elemenot cacheado: " + s);
        } else return (ObjectInputStream) streams[1];
        return this.getObjectInputStream(s);
    }

    public boolean enviarObjeto(Object objeto, Socket s) {
        // Escribir el documento XML en el OutputStream
        boolean i = false;
        try {
            System.out.println("///////////////////////////////////");
            System.out.println("Mandando objeto a " + s);
            System.out.println("Elemento enviado: " + objeto);

            ObjectOutputStream out = this.getObjectOutputStream(s);
            synchronized (s){out.writeObject(objeto);}
            i = true;
            System.out.println("Objeto enviado a " + s);
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } finally {
            System.out.println("///////////////////////////////////");
            return i;
        }
    }

    public String recibirString(Socket s) {
        return (String) this.recibirObjeto(s);
    }

    public Object recibirObjeto(Socket s) {
        try {
            System.out.println("///////////////////////////////////");
            System.out.println("Esperando objeto de " + s);
            ObjectInputStream in = this.getObjectInputStream(s);
            Object objeto;
            synchronized (s){objeto = in.readObject();}
            System.out.println("Elemento recibido: " + objeto);

            return objeto;

        } catch (IOException e) {
            // TODO Auto-generated catch block
            return null;
        } catch (ClassNotFoundException e) {
            System.out.println("Clase no encontrada");
            return null;
        } finally {
            System.out.println("///////////////////////////////////");
        }
    }

    public Codigos recibirCodigo(Socket s) {
        Object objeto = this.recibirObjeto(s);
        if (objeto instanceof Codigos) return (Codigos) objeto;
        return null;
    }

    public boolean enEspera(Socket s) {
        try {
            return s.getInputStream().available() != 0;
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
