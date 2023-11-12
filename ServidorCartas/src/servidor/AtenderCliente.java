package servidor;


import java.io.IOException;

import java.net.Socket;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;


import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import modeloDominio.Humano;
import modeloDominio.ProcesadorMensajes;
import modeloDominio.codigoXML;

public class AtenderCliente extends Thread {
	private Socket s;
	
	public AtenderCliente(Socket s) {
		this.s=s;
	}

	public void run() {
		//Atiendo cliente
		System.out.println("Conexión aceptada"+this.s.toString());
		this.bienvenida();
		this.dialogoInicial();
	}
	
	private void dialogoInicial() {
        //Recibo respuesta
		int operacion=-1;
		
		Document xml=ProcesadorMensajes.recibirXml(s);
		if(xml.getNodeValue().compareTo("nueva")==0)operacion=0;
		if(xml.getNodeValue().compareTo("unirse")==0)operacion=1;
		
		switch(operacion) {
		case -1 : return;
		case 0 : this.crearPartida();
		break;
		case 1 : this.unirseAPartida();
		break;
		default : ProcesadorMensajes.xmlError(codigoXML.ERROR, 400,this.s);
		}
		
		try {
			this.s.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	private void bienvenida() {
		try {
            DocumentBuilderFactory dbf=DocumentBuilderFactory.newInstance();
            DocumentBuilder db=dbf.newDocumentBuilder();
            Document xml=db.newDocument();

            // Crear el elemento raíz "bienvenida" con el atributo "texto"
            Element raiz=xml.createElement("bienvenida");
            Attr attr=xml.createAttribute("txt");
            attr.setValue("bienvenido al server");
            raiz.setAttributeNode(attr);

            // Crear los elementos "unirse" y "crear"
            Element unirse=xml.createElement("unirse");
            unirse.setAttribute("disponible", "1");
            raiz.appendChild(unirse);

            Element crear=xml.createElement("crear");
            crear.setAttribute("disponible", "1");
            raiz.appendChild(crear);

            // Agregar el elemento raíz al documento
            xml.appendChild(raiz);
            
            // Enviar mensaje
            ProcesadorMensajes.enviarXml(xml, this.s);
            
		} catch (ParserConfigurationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	
	private void crearPartida() {

	}
	
	private void unirseAPartida() {
		
	}
	
	private void nuevoJugador() {

	}
}
