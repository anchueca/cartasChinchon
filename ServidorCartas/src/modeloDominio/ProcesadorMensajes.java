package modeloDominio;

import java.io.*;
import java.net.Socket;

public class ProcesadorMensajes {
	public static boolean enviarObjeto(Object xml, Socket s) {
    	// Escribir el documento XML en el OutputStream
        boolean i=false;
		try {
			System.out.println("Mandando xml a "+s.toString());
			//Lo hago con object porque sino el cliente no sabe cuando termina el envío
			//sin cerrar el socket
			ObjectOutputStream out=new ObjectOutputStream(s.getOutputStream());
	        out.writeObject(xml);
	        i=true;
	        System.out.println("XML enviado a "+s);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		finally {
			return i;
		}
    }
	public static Object recibirObjeto(Socket s) {
        try {
        	System.out.println("Esperando xml de "+s.toString());
			ObjectInputStream in =new ObjectInputStream(s.getInputStream());
            return in.readObject();
            
        } catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		} catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }
    public static boolean enEspera(Socket s){
        try {
            return s.getInputStream().available()!=0;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

	public static void xmlError(codigoXML codigo,int valor,Socket s) {
		
	}
}
