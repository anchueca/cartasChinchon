package servidor;


import modeloDominio.EstadoPartida;
import modeloDominio.FaseChinchon;
import modeloDominio.baraja.Baraja;
import modeloDominio.baraja.Tamano;
import org.w3c.dom.Document;
import servidor.usuarios.Humano;
import servidor.usuarios.IA;
import servidor.usuarios.Jugador;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Partida {

	//Atributos

	private String nombre;//identificador partida
	private EstadoPartida estado;
	private FaseChinchon fase;
	private Jugador anfitrion;

	private final Map<Jugador,Integer> puntuaciones;
	private Baraja baraja;
	private Jugador turno;
	private final List<Jugador> jugadores;



	//Factoría y constructores
	private Partida() {
		this.estado=EstadoPartida.ESPERANDO;
		this.puntuaciones=new HashMap<>();
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
		if(this.estado==EstadoPartida.ESPERANDO){
			Humano humano=new Humano(jugador);
			if(this.getJugadores().isEmpty())this.anfitrion=humano;
			this.getJugadores().add(humano);
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
		System.out.println("prueba");
		return lista;
	}

	public String nombreAnfitrion(){
		 return this.anfitrion.getNombre();
	}
	public void iniciarPartida() {
		this.estado=EstadoPartida.ENCURSO;
		for (Jugador jugador : this.jugadores) {
			this.puntuaciones.put(jugador,0);
		}
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
		for(int i=0;i<7;i++)
			for (Jugador jugador: jugadores
			) {
				//jugador.darCarta(this.baraja.tomarCarta());
			}
	}

	public String toString(){
		return this.nombre;
	}

}
