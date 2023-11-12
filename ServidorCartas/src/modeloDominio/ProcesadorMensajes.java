package modeloDominio;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.Reader;
import java.net.Socket;
import java.rmi.AccessException;
import java.util.Scanner;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Document;
import org.xml.sax.SAXException;

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
			
			TransformerFactory transformerFactory=TransformerFactory.newInstance();
	        Transformer transformer=transformerFactory.newTransformer();
	        DOMSource source=new DOMSource(xml);

	        OutputStream os=s.getOutputStream();
	        transformer.transform(source,new StreamResult(os));
	        os.flush();
	        //DataOutputStream out=new DataOutputStream(s.getOutputStream());
	        //out.writeBytes("\n");
	        //out.flush();
	        i=true;
	        System.out.println("XML enviado a "+s.toString()+s.toString());
		} catch (TransformerConfigurationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (TransformerException e) {
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
        	
            DocumentBuilderFactory dbf=DocumentBuilderFactory.newInstance();
            DocumentBuilder db=dbf.newDocumentBuilder();
            //Scanner lector=new Scanner(s.getInputStream());
            //BufferedReader reader=new BufferedReader(new InputStreamReader(s.getInputStream()));
            //String xml="";
            //while(reader.ready()) xml+=reader.readLine();
            //System.out.println(xml);
            //Document xmlDocument=db.parse(xml);
            Document xmlDocument=db.parse(s.getInputStream());
            
            System.out.println("XML recibido de "+s.toString());
            
            return xmlDocument;
        } catch (SAXException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		} catch (ParserConfigurationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
    }
	
	public static void xmlError(codigoXML codigo,int valor,Socket s) {
		
	}
}
