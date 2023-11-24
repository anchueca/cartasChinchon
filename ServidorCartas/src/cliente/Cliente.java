package cliente;


import java.io.IOException;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

import servidor.Servidor;

/*
Clase encargada del inicio del programa y entre partidas (el menú)
 */
public class Cliente{

    static Servidor server=new Servidor();//Para pruebas

	private Socket s;
    private Consola interfaz;
    private PartidaCliente partida;

    private boolean salida;
    //Inicio programa
	public static void main(String[] args) {
		new Cliente();
	}
/*
Inicio conexión
 */
    public Cliente(){
        this.salida=false;
        this.partida=null;
        System.out.println("Inicio cleinte");
        //try {
            System.out.println("Iniciando conexión");
            //this.s=new Socket("localhost",55555);
            System.out.println("conexion establecida");
            this.interfaz=new Consola(this);
            interfaz.start();
        try {
            this.inicio();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        /*} catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }*/
    }
    /*
    Gestión del progrma
     */
	public void inicio() throws InterruptedException {

        //Inicio juego
        while (!this.salida);

        //Cierre cliente
        this.interfaz.interrupt();
        System.out.println("Cerrando conexión");
        try{
            this.s.close();
            System.out.println("Conexión cerrada");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public String salir(){
        this.salida=true;
        return "Saliendo....";
    }

    public void crearPartida(String nombre){
        this.partida=new PartidaCliente();
    }

    public void unirsePartida(String nombre){
        this.partida=new PartidaCliente();
    }

    public List<String> listaPartidas(){
        return this.server.getPartidas();
    }
}
