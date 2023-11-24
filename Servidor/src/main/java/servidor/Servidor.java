import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;

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
    public boolean jugada(String partida,String jugador,string jugada){
        return false;
    }

    @GET
    @PATH("partida/{partida}/{jugador}/{mano}")




	/*public static void main(String[] args) {
		ExecutorService pool=Executors.newCachedThreadPool();
		try {
			ServerSocket s=new ServerSocket(55555);
			Socket cliente;
			System.out.println("Inicio servidor");
			while(true) {
				try {
					cliente=s.accept();
					pool.execute(new AtenderCliente(cliente));
					System.out.println("Solicitus aceptada "+cliente.toString());
				}catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		finally {
			pool.shutdown();
		}
	}*/
}