package servidor;


import modeloDominio.EstadoPartida;
import modeloDominio.FaseChinchon;
import modeloDominio.baraja.Baraja;
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
	private final List<Jugador> jugadores;



	//Factoría y constructores
	private Partida() {
		this.estado=EstadoPartida.ESPERANDO;
		this.jugadores=new ArrayList<>();
		this.fase=null;
		this.anfitrion=null;
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

	//Gestión partida


	public boolean juega(Jugador jugador){
		return this.turno==jugador;
	}

	public boolean expulsarJugador(String jugador){
		return this.getJugadores().remove(jugador);//No implemntado con strig
	}//por implementr
	public boolean robotizar(String jugador){
		return this.getJugadores().remove(jugador);
	}//por implementr//No implemntado con strig

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

	public String nombreAnfitrion(){
		 return this.anfitrion.getNombre();
	}
	public boolean iniciarPartida(String nombre) {
		 if(this.nombreAnfitrion().compareTo(nombre)==0){
			 this.estado=EstadoPartida.ENCURSO;
			 this.repartirMano();
			 return true;
		 }
		 return false;
	}

	public void inicioRonda(){
		this.repartirMano();
		this.fase=FaseChinchon.ABIERTO;
	}

	public Jugador siguienteTurno() {
		int turno=this.jugadores.indexOf(this.turno);
		turno%=this.jugadores.size();
		this.turno= this.jugadores.get(turno);
		return this.turno;
	}

	public void repartirMano() {
		 this.baraja.barajar();
		for(int i=0;i<7;i++)
			for (Jugador jugador: jugadores
			) {
				jugador.darCarta(this.baraja.tomarCarta());
			}
	}

	public Mano getMano(String jugador){
		for (Jugador jugador1:this.jugadores
			 ) {
			if(jugador1.getNombre().compareTo(jugador)==0)return jugador1.verMano();
		}
		return null;
	}

	public String toString(){
		return this.nombre;
	}

	public void ordenarMano(String nombre){
		 Jugador jugador=this.buscarJugador(nombre);
		 if(jugador!=null)jugador.verMano().ordenar();
	}

	private Jugador buscarJugador(String nombre){
		for (Jugador jugador:this.jugadores
			 ) {
			if(jugador.getNombre().compareTo(nombre)==0)return jugador;
		}
		return null;
	}

}
