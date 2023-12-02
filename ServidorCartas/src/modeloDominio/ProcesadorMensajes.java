package modeloDominio;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class ProcesadorMensajes {
    public static boolean enviarObjeto(Object xml, Socket s) {
        // Escribir el documento XML en el OutputStream
        boolean i = false;
        try {
            System.out.println("Mandando xml a " + s.toString());
            System.out.println(xml.toString());
            //Lo hago con object porque sino el cliente no sabe cuando termina el envío
            //sin cerrar el socket
            ObjectOutputStream out = new ObjectOutputStream(s.getOutputStream());
            out.writeObject(xml);
            i = true;
            System.out.println("XML enviado a " + s);
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } finally {
            return i;
        }
    }

    public static String recibirString(Socket s){
        return (String) ProcesadorMensajes.recibirObjeto(s);
    }

    public static Object recibirObjeto(Socket s) {
        try {
            System.out.println("Esperando xml de " + s.toString());
            ObjectInputStream in = new ObjectInputStream(s.getInputStream());
            Object objeto=in.readObject();
            System.out.println(objeto.toString());
            return objeto;

        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return null;
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    public static boolean enviarCodigo(Socket s,Codigos codigo){
        switch (codigo){
            case BIEN -> ProcesadorMensajes.enviarObjeto(":)",s);
            case MAL -> ProcesadorMensajes.enviarObjeto(":(",s);
        }
        return true;
    }

    public static boolean enEspera(Socket s) {
        try {
            return s.getInputStream().available() != 0;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

}
