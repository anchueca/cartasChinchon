package servidor;

import modeloDominio.baraja.Tamano;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

//@ApplicationPath("/SuperJuego")
//@Path("/Chinchon")
public class Servidor {

    	/*
	Propiedades estáticas
	 */
	private static final int MAXPartidas=32;
	private static final Map<String,Partida> partidas=new HashMap<>();
	private static int PartidasActivas=0;


	public Partida buscarPartida(String nombre) {
		return Servidor.partidas.get(nombre);
	}

    public static void main(String[] args){

    }

    public Servidor(){

    }

    /*
    Devuelve la lista de  nombre de partidas
     */
    //@GET
    //@Path("partidas")
    public List<String> getPartidas(){
        return new ArrayList<>(Servidor.partidas.keySet());
    }
    /*
    Crea una nueva partida vacía
     */
    //@PUT
    //@Path("partida/{nombre}")
    public void crearPartida(String nombre){
        if(Servidor.PartidasActivas<Servidor.MAXPartidas){
            Servidor.partidas.put(nombre,new Partida(nombre, Tamano.NORMAL));
            Servidor.PartidasActivas++;
        }
    }
    /*
    Se une a aprtida
     */
    //@POST
    //@Path("partida/{nombre}/{jugador}")
    public void entrarPartida(String nombre,String jugador){
        Partida partida=this.buscarPartida(nombre);
        if(partida==null);
        else{
            partida.nuevoHumano(jugador);
        }
    }

    public void abandonarPartida(String nombre,String nombreJugador){
        Partida partida=this.buscarPartida(nombre);
        if(partida==null);
        else{
            partida.expulsarJugador(nombreJugador);
        }
    }
    /*
    Juagada
     */
    //@Post
    //@Path("Partida/{partida]/{jugador}{jugada}")
    public boolean jugada(String partida,String jugador,String jugada){
        return false;
    }

    public List<String> listaJugadores(String nombre){
        Partida partida=this.buscarPartida(nombre);
        if(partida==null);
        else{
            return partida.getJugadoresS();
        }
        return null;
    }

//    @GET
//    @PATH("partida/{partida}/{jugador}/{mano}")
}