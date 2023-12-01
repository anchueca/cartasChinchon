package servidor;


import modeloDominio.EstadoPartida;
import modeloDominio.FaseChinchon;
import modeloDominio.baraja.Baraja;
import modeloDominio.baraja.Carta;
import modeloDominio.baraja.Mano;
import modeloDominio.baraja.Tamano;
import org.w3c.dom.Document;
import servidor.usuarios.Humano;
import servidor.usuarios.IA;
import servidor.usuarios.Jugador;

import java.util.ArrayList;
import java.util.List;

public class Partida {

	//Atributos

	private String nombre;//identificador partida
	private EstadoPartida estado;
	private FaseChinchon fase;
	private Jugador anfitrion;
	private Baraja baraja;
	private Jugador turno;
	private Baraja descubierta;
	private final List<Jugador> jugadores;


	//Factoría y constructores
	private Partida() {
		this.estado=EstadoPartida.ESPERANDO;
		this.jugadores=new ArrayList<>();
		this.fase=null;
		this.anfitrion=null;
		this.descubierta=new Baraja();
	}
	public static Partida PartidaFactoria(Document xml){
		return new Partida();
	}
	protected Partida(String nombrePartida,Tamano barajaTamano) {
		this();
		this.nombre=nombrePartida;
		this.baraja=Baraja.barajaFactoria(barajaTamano);
	}

/*
 * Getters y setters
 */

 	public String getNombre() {
		return nombre;
	}
	public EstadoPartida getEstado() {
		return estado;
	}
	public void setEstado(EstadoPartida estado) {
		this.estado = estado;
	}
	public Jugador getTurno(){
		return this.turno;
	}
	public String nombreAnfitrion(){
		return this.anfitrion.getNombre();
	}
	public int numJugadores(){return this.jugadores.size();}
	private List<Jugador> getJugadores() {
		return this.jugadores;
	}

	public List<String> getJugadoresS() {
		List<String> lista= new ArrayList<>();
		for (Jugador j:this.jugadores
		) {
			lista.add(j.getNombre());
			System.out.println(j.getNombre());
		}
		return lista;
	}
	public boolean juega(Jugador jugador){
		return this.turno==jugador;
	}


	//////JUGADORES////////
	public boolean nuevoHumano(String jugador) {
		if(this.estado==EstadoPartida.ESPERANDO && !this.getJugadoresS().contains(jugador)){
			Humano humano=new Humano(jugador);
			if(this.getJugadores().isEmpty())this.anfitrion=humano;
			this.jugadores.add(humano);
			return true;
		}
		return false;
	}
	public boolean nuevoIA(String jugador) {
		if(this.estado==EstadoPartida.ESPERANDO){
			this.getJugadores().add(new IA(jugador));
			return true;
		}
		return false;
	}
	public boolean expulsarJugador(String jugador){
		 Jugador jugador1=this.buscarJugador(jugador);
		 if(jugador1==null)return false;
		 if(this.estado==EstadoPartida.ESPERANDO || this.estado==EstadoPartida.FINALIZADO)return this.getJugadores().remove(jugador1);
		 return this.robotizar(jugador1);
	}
	public boolean robotizar(Jugador jugador){
		this.getJugadores().set(this.getJugadores().indexOf(jugador),new IA(jugador.getNombre()));
		return true;
	}


/////////GESTIÓN PARTIDA////////////////

	private void iniciarPartida(){
		this.estado=EstadoPartida.ENCURSO;
		this.inicioRonda();
	}

	public void inicioRonda(){
		this.repartirMano();
		this.fase=FaseChinchon.ABIERTO;
	}

	/*
	Determina el siguiente en el turno
	 */
	public void siguienteTurno() {
		int turno=this.jugadores.indexOf(this.turno);
		turno%=this.jugadores.size();
		this.turno= this.jugadores.get(turno);
	}
/*
Reparte la baraja entre todos los juadores y la decubierta
 */
	public void repartirMano() {
		//recojo las deescubiertas
		if(this.descubierta.numCartas()!=0)this.baraja.meterCarta(this.descubierta);
		//recojo la de los jugadores
		Carta carta;
		for(Jugador jugador:this.getJugadores())while((carta=jugador.tomarCarta(0))!=null)this.baraja.meterCarta(carta);

		 this.baraja.barajar();
		 //reparto
		for(int i=0;i<7;i++)
			for (Jugador jugador: jugadores
			) {
				jugador.darCarta(this.baraja.tomarCarta());
			}
		this.descubierta.meterCarta(this.baraja.tomarCarta());
	}

	public String toString(){
		return this.nombre;
	}


	private Jugador buscarJugador(String nombre){
		for (Jugador jugador:this.jugadores
			 ) {
			if(jugador.getNombre().compareTo(nombre)==0)return jugador;
		}
		return null;
	}

	//////////Accioens//////////////////
	public boolean iniciarPartida(String nombre) {
		if(this.nombreAnfitrion().compareTo(nombre)==0){
			this.iniciarPartida();
			return true;
		}
		return false;
	}
	public Mano getMano(String jugador){
		for (Jugador jugador1:this.jugadores
		) {
			if(jugador1.getNombre().compareTo(jugador)==0)return jugador1.verMano();
		}
		return null;
	}
	public Carta getDescubierta(){
		return this.descubierta.verCarta();
	}
	public void ordenarMano(String nombre){
		Jugador jugador=this.buscarJugador(nombre);
		if(jugador!=null)jugador.verMano().ordenar();
	}


}
