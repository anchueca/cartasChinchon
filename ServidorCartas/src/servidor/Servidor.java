package servidor;

import modeloDominio.EstadoPartida;
import modeloDominio.baraja.Carta;
import modeloDominio.baraja.Mano;
import modeloDominio.baraja.Tamano;

import javax.ws.rs.*;
import javax.ws.rs.core.Application;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@ApplicationPath("/SuperJuego")
//@Path("/Chinchon")
public class Servidor extends Application {

    	/*
	Propiedades estáticas
	 */
	private static final int MAXPartidas=32;
	private static final Map<String,Partida> partidas=new HashMap<>();
	private static int PartidasActivas=0;


    ///////////GESTIÓN//////////////

    /*
   Devuelve la lista de  nombre de partidas
    */
    @GET
    @Path("partidas")
    public List<String> getPartidas(){
        return new ArrayList<>(Servidor.partidas.keySet());
    }
    /*
    Crea una nueva partida vacía
     */
    @PUT
    @Path("partida/{nombre}")
    public boolean crearPartida(String nombre){
        if(Servidor.PartidasActivas<Servidor.MAXPartidas && this.buscarPartida(nombre)==null){
            Servidor.partidas.put(nombre,new Partida(nombre, Tamano.NORMAL));
            Servidor.PartidasActivas++;
            return true;
        }
        return false;
    }
    /*
    Se une a aprtida
     */
    @POST
    @Path("partida/{nombre}/{jugador}")
    public boolean entrarPartida(String nombre,String jugador){
        Partida partida=this.buscarPartida(nombre);
        if(partida==null)return false;
        partida.nuevoHumano(jugador);
        return true;
    }

    public boolean abandonarPartida(String nombre,String nombreJugador){
        Partida partida=this.buscarPartida(nombre);
        if(partida==null) return false;
        else{
            if(partida.expulsarJugador(nombreJugador) && partida.numJugadores()==0){//reviar condicion ¿Necesario?
                Servidor.partidas.remove(nombre);
                Servidor.PartidasActivas--;
                return true;
            }
            return false;
        }
    }

	public Partida buscarPartida(String nombre) {
		return Servidor.partidas.get(nombre);
	}

    /*public static void main(String[] args) {
        try {
            // Configurar el servidor Jetty
            Server server = new Server(8080);

            // Configurar el contexto de servlet
            ServletContextHandler context = new ServletContextHandler(ServletContextHandler.SESSIONS);
            context.setContextPath("/");

            // Configurar el servlet de Jersey
            ServletHolder jerseyServlet = context.addServlet(ServletContainer.class, "/api/*");
            jerseyServlet.setInitOrder(0);
            jerseyServlet.setInitParameter("jersey.config.server.provider.packages", "paquete.donde.esta.tus.recursos");

            // Añadir el contexto al servidor
            server.setHandler(context);

            // Iniciar el servidor Jetty
            server.start();
            server.join();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }*/

    //////////////////////Información partida//////////////////


    public List<String> listaJugadores(String nombre){
        Partida partida=this.buscarPartida(nombre);
        if(partida==null);
        else{
            return partida.getJugadoresS();
        }
        return null;
    }
    @GET
    //@Path("partida/{partida]")
    public EstadoPartida estadoPartida(String nombre){
        Partida partida=this.buscarPartida(nombre);
        if(partida==null);
        else{
            return partida.getEstado();
        }
        return null;
    }
    public boolean partidaActualizada(String nombre,String jugador){//por implementar
        Partida partida=this.buscarPartida(nombre);
        if(partida==null)return false;
        else{
            return true;//partida.;
        }

    }
    public Mano verMano(String nombre, String jugador){
        Partida partida=this.buscarPartida(nombre);
        if(partida==null)return null;
        else{
            return partida.getMano(jugador);
        }
    }
    public Carta verCartaDescubierta(String nombre){
        Partida partida=this.buscarPartida(nombre);
        if(partida==null)return null;
        else{
            return partida.getDescubierta();
        }
    }


    /////////////ACCIONES////////////////

    //@POST
    //@Path("partida/{partida]/{jugador}/{jugada}")
    public boolean jugada(String partida,String jugador,String jugada){
        return false;
    }



    public boolean iniciarPartida(String nombre,String nombreJugador){
        Partida partida=this.buscarPartida(nombre);
        if(partida==null)return false;
        else{
            return partida.iniciarPartida(nombreJugador);
        }
    }

    public void ordenarMano(String nombre,String jugador){
        Partida partida=this.buscarPartida(nombre);
        if(partida==null)return;
        else{
            partida.ordenarMano(jugador);
        }
    }

//    @GET
//    @PATH("partida/{partida}/{jugador}/{mano}")
}