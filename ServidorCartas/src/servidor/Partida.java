package servidor;


import cliente.NumeroParametrosExcepcion;
import modeloDominio.EstadoPartida;
import modeloDominio.baraja.Baraja;
import modeloDominio.baraja.Carta;
import modeloDominio.baraja.Mano;
import modeloDominio.baraja.Tamano;
import servidor.usuarios.Asistente;
import servidor.usuarios.AsistenteI;
import servidor.usuarios.Jugador;
import org.w3c.dom.Document;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static java.lang.Integer.parseInt;

public class Partida{
	/*
	Propiedades estáticas
	 */
	private static final int MAXEspectadores=128;
	private static final int MAXPartidas=32;
	private static final Map<String,Partida> partidas=new HashMap<>();
	private static int PartidasActivas=0;


	public static Partida buscarPartida(String nombre) {
		return Partida.partidas.get(nombre);
	}

	/*
        Partida particular
         */
	private String nombre;//identificador partida
	public String getNombre() {
		return nombre;
	}
	public EstadoPartida getEstado() {
		return estado;
	}
	public void setEstado(EstadoPartida estado) {
		this.estado = estado;
	}
	private EstadoPartida estado;

	private final Map<Jugador,Integer> puntuaciones;
	private Baraja baraja;
	private Jugador turno;
	private final List<Jugador> jugadores;
	private final List<AsistenteI> asistentes;

	//Factoría y constructies
	private Partida() {
		this.estado=EstadoPartida.ESPERANDO;
		this.puntuaciones=new HashMap<>();
		this.jugadores=new ArrayList<>();
		this.asistentes=new ArrayList<>();
	}
	public static Partida PartidaFactoria(Document xml){
		return new Partida();
	}
	protected Partida(String nombrePartida,Tamano barajaTamano) {
		this();
		this.nombre=nombrePartida;
		this.baraja=Baraja.barajaFactoria(barajaTamano);
	}

	//Funcionamiento partida
	private void bucleJuego(){
		//Comienzo juego
		while (this.estado!=EstadoPartida.FINALIZADO){
			//Compruebo si hay mensajes pendientes
			this.atenderClientes();
			//Acciones partida

		}
		//Fin partida
	}

	//Gestión partida
	public boolean degradarJugador(Jugador jugador){
		return this.getJugadores().remove(jugador);
	}//por implementr
	public boolean expulsarJugador(Jugador jugador){
		return this.getJugadores().remove(jugador);
	}//por implementr
	public boolean robotizar(Jugador jugador){
		return this.getJugadores().remove(jugador);
	}//por implementr

	public boolean nuevoEspectador(AsistenteI asistente) {
		this.getEspectadores().add(asistente);
		return true;
	}

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

	public List<AsistenteI> getEspectadores() {
		return new ArrayList<>(this.asistentes);
	}

	public void iniciarPartida() {

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

	protected void atenderClientes() {
		for (AsistenteI asistente: this.asistentes
		) {
			//if(asistente.mensajePendiente())asistente.pr
		}
		for (Jugador jugador: this.jugadores
		) {

		}
	}


}
