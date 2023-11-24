package servidor;

import java.util.ArrayList;
import java.util.List;
import javax.ws.*;

@ApplicationPath("/SuperJuego")
@Path("/Chinchon")
public class Servidor {

    public static void main(String[] args){

    }
    /*
    Devuelve la lista de  nombre de partidas
     */
    @GET
    @Path("partidas")
    public List<String> getPartidas(){
        List<String> lista=new ArrayList<>();
        lista.add("pepe");
        lista.add("pepea");
        lista.add("perez");
        return lista;
    }
    /*
    Crea una nueva partida vacía
     */
    @PUT
    @Path("partida/{nombre}")
    public void crearPartida(String nombre){

    }
    /*
    Se une a aprtida
     */
    @POST
    @Path("partida/{nombre}/{jugador}")
    public void entrarPartida(String nombre){

    }
    /*
    Juagada
     */
    @Post
    @Path("Partida/{partida]/{jugador}{jugada}")
    public boolean jugada(String partida,String jugador,String jugada){
        return false;
    }

//    @GET
//    @PATH("partida/{partida}/{jugador}/{mano}")
