package modeloDominio;

import org.w3c.dom.Document;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.*;
import java.net.Socket;
import java.util.Scanner;

public class ProcesadorMensajes {

	public static boolean enviarXmlSchema(String schema, Socket s) {
		 boolean i=false;
        try {
        	System.out.println("Mandando xml schema a "+s.toString());
            File xmlSchemaFile=new File(schema);
            FileInputStream fis=new FileInputStream(xmlSchemaFile);
            OutputStream os=s.getOutputStream();
            byte[] buffer=new byte[1024];
            int bytesRead;

            while ((bytesRead=fis.read(buffer))!=-1) {
                os.write(buffer,0,bytesRead);
            }
            
            fis.close();
            os.flush();
            i=true;
        } catch (IOException e) {
            e.printStackTrace();
            i=false;
        }
        finally {
        	return i;
        }
    }
	public static boolean enviarXml(Document xml, Socket s) {
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
	
	public static Document recibirXml(Socket s) {
        try {
        	System.out.println("Esperando xml de "+s.toString());
			ObjectInputStream in =new ObjectInputStream(s.getInputStream());
            Document xmlDocument= (Document) in.readObject();

            System.out.println("XML recibido de "+s);
            
            return xmlDocument;
        } catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		} catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }
	
	public static void xmlError(codigoXML codigo,int valor,Socket s) {
		
	}
}
