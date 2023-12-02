package cliente;


import servidor.Servidor;

import java.net.Socket;

/*
Clase encargada del inicio del programa y entre partidas (el menú)
 */
public class Cliente {

    static Servidor server = new Servidor();//Para pruebas
    private Socket s;

    /*
    Inicio conexión
     */
    public Cliente() {

        System.out.println("Inicio cleinte");
        //try {
        System.out.println("Iniciando conexión");
        //this.s=new Socket("localhost",55555);
        System.out.println("conexion establecida");

        //JFrame con =ConsolaBonita.iniciar();
        //this.inicio();


        /*} catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }*/
    }

    //Inicio programa
    public static void main(String[] args) {
        new Cliente();
    }
    /*
    Gestión del progrma
     */
	/*public void inicio() {
        //Inicio juego


        //Cierre cliente
        //con.dispose();
        //System.out.println("Cerrando conexión");
        try{
            this.s.close();
            System.out.println("Conexión cerrada");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }*/


}
