package cliente;


import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Scanner;

import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import modeloDominio.ProcesadorMensajes;

public class Cliente {

	private Socket s;
	
	public static void main(String[] args) {
		new Cliente().inicio();
	}
	
	public void inicio() {
		System.out.println("Inicio cleinte");
		try {
			System.out.println("Iniciando conexión");
			this.s=new Socket("localhost",55555);
			System.out.println("conexion establecida");
			this.bienvenida();
			
			System.out.println("Cerrando conexión");
            s.close();
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	/*
	 *  Procesa la respusta de bienvenida
	 */
	public void bienvenida() {
		// Descargar el XML desde el servidor
		System.out.println("Esperando mensaje de bienvenida");
		Document xml=ProcesadorMensajes.recibirXml(s);
		System.out.println("recibido");
		// Procesar XML
		Element rootElement=xml.getDocumentElement();
        String texto=rootElement.getAttribute("texto"); 

        // Menú interactivo
        NodeList hijos = xml.getDocumentElement().getChildNodes();
        Scanner scanner = new Scanner(System.in);
        
        // Creo el menu
        int opcion = 1;

        // Recorro cada uno de los nodos
        for (int i = 0; i < hijos.getLength(); i++) {
            Node nodo = hijos.item(i);
            // Añado el texto correspondiente
            if (nodo instanceof Element) {
                texto+=opcion + ". " + nodo.getNodeName();
                if(nodo.getAttributes().getNamedItem("disponible").getNodeValue().compareTo("1")!=0)texto+=" (No disponible)";
                texto+="\n";
                opcion++;
            }
        }
        texto+="0. Salir\nSelecciona una opción:\n";
        // Mostrar reiteradamente el menu
        while (true) {
        	System.out.println(texto);
            
            opcion = scanner.nextInt();

            if (opcion == 0) {
                break;
            } else if (opcion > 0) {
                Node selectedNode = hijos.item(opcion - 1);
                if (selectedNode instanceof Element) {
                	texto+="Contenido de '" + selectedNode.getNodeName() + "': " + selectedNode.getTextContent()+"\n";
                }
            } else {
                texto+="Opción no válida. Por favor, elige una opción válida.\n";
            }
        }
        scanner.close();
    }
}
