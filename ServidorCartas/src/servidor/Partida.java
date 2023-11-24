package servidor;


import modeloDominio.AccionesChinchonI;
import modeloDominio.EstadoPartida;
import modeloDominio.FaseChinchon;
import modeloDominio.VerChinchonI;
import modeloDominio.baraja.Baraja;
import modeloDominio.baraja.Carta;
import modeloDominio.baraja.Mano;
import modeloDominio.baraja.Tamano;
import servidor.usuarios.Jugador;
import org.w3c.dom.Document;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static java.lang.Integer.parseInt;

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






	public boolean expulsarJugador(Jugador jugador){
		return this.getJugadores().remove(jugador);
	}//por implementr
	public boolean robotizar(Jugador jugador){
		return this.getJugadores().remove(jugador);
	}//por implementr

	public boolean nuevoJugador(Jugador jugador) {
		if(this.estado==EstadoPartida.ESPERANDO){
			this.getJugadores().add(jugador);
			return true;
		}
		return false;
	}
	public List<Jugador> getJugadores() {
		return new ArrayList<>(this.jugadores);
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
