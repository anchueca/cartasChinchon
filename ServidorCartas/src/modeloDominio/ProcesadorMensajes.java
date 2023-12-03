package modeloDominio;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;

public class ProcesadorMensajes {
    /*
    Cacheo de los socket. Por ahora no limpia. No debería haber problemas de concurrencia.
    Cada acceso buscará y añadirá su propio socket. El patrón singleton solo podría dar problemas la primera vez que se accede,
    pero solo se emplea un único hilo al comienzo de la conexión. No obstante, desconozco si acceder a un dato durante una inserción
    podría dar problemas.

    Realmente este sistema solo tiene sentido en el lado del servidor. El cliente solo tiene un socket aunque en el futuro, si el
    cliente padece de ludopatía podría habilitarse jugar varias partidas a la vez (con distintos sockets).
     */

    private static final Map<Socket,Object[]> streams=new HashMap<>();//new ConcurrentHashMap<>();//Quizá convenga
    //Si está lo uso, sino lo creo. Es como un singleton
    private static ObjectOutputStream getObjectOutputStream(Socket s) throws IOException {
        Object[] streams;
        if((streams=ProcesadorMensajes.streams.get(s))==null){
            ProcesadorMensajes.streams.put(s,
                    new Object[]{new ObjectOutputStream(s.getOutputStream()), new ObjectInputStream(s.getInputStream())});
            System.out.println("Elemenot cacheado: "+s);
        }else return (ObjectOutputStream) streams[0];
        return ProcesadorMensajes.getObjectOutputStream(s);
    }
    //Si está lo uso, sino lo creo. Es como un singleton
    private static ObjectInputStream getObjectInputStream(Socket s) throws IOException {
        Object[] streams;
        if((streams=ProcesadorMensajes.streams.get(s))==null){
            ProcesadorMensajes.streams.put(s,
                    new Object[]{new ObjectOutputStream(s.getOutputStream()), new ObjectInputStream(s.getInputStream())});
            System.out.println("Elemenot cacheado: "+s);
        }else return (ObjectInputStream) streams[1];
        return ProcesadorMensajes.getObjectInputStream(s);
    }
    public static boolean enviarObjeto(Object objeto, Socket s) {
        // Escribir el documento XML en el OutputStream
        boolean i = false;
        try {
            System.out.println("///////////////////////////////////");
            System.out.println("Mandando objeto a " + s);
            System.out.println("Elemento enviado: "+objeto);

            ObjectOutputStream out = ProcesadorMensajes.getObjectOutputStream(s);
            out.writeObject(objeto);
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

    public static String recibirString(Socket s){
        return (String) ProcesadorMensajes.recibirObjeto(s);
    }

    public static Object recibirObjeto(Socket s) {
        try {
            System.out.println("///////////////////////////////////");
            System.out.println("Esperando objeto de " + s);
            ObjectInputStream in = ProcesadorMensajes.getObjectInputStream(s);
            Object objeto=in.readObject();
            System.out.println("Elemento recibido: "+objeto);

            return objeto;

        } catch (IOException e) {
            // TODO Auto-generated catch block
            return null;
        } catch (ClassNotFoundException e) {
            System.out.println("Clase no encontrada");
            return null;
        }
        finally {
            System.out.println("///////////////////////////////////");
        }
    }


    public static Codigos recibirCodigo(Socket s){
        Object objeto = ProcesadorMensajes.recibirObjeto(s);
        if(objeto instanceof Codigos)return (Codigos) objeto;
        return null;
    }

    public static boolean enEspera(Socket s) {
        try {
            return s.getInputStream().available() != 0;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

}
