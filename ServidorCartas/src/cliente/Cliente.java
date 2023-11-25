package cliente;


import servidor.Servidor;

import java.net.Socket;
import java.util.List;

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
            this.interfaz.start();
            this.inicio();

        /*} catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }*/
    }
    /*
    Gestión del progrma
     */
	public void inicio() {
        //Inicio juego
        try {
            while (!this.salida) {
                Thread.sleep(1000);
                if(this.partida!=null)this.partida.actualizarPartida();//Compruebo si se han producido cambios
            }
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }


        //Cierre cliente
        this.interfaz.interrupt();
        System.out.println("Cerrando conexión");
        /*try{
            this.s.close();
            System.out.println("Conexión cerrada");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }*/
    }

    public void actualizarPartida(){

    }
    public String salir(){
        this.salida=true;
        return "Saliendo....";
    }

    public void crearPartida(String nombre){
        Cliente.server.crearPartida(nombre);
    }

    public void unirsePartida(String nombre,String nombreJugador){
        Cliente.server.entrarPartida(nombre,nombreJugador);
        this.partida=new PartidaCliente(nombre,nombreJugador);
        this.interfaz.setPartida(this.partida);
    }
    public List<String> listaPartidas(){
        return this.server.getPartidas();
    }

}
