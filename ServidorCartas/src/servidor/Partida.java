package servidor;


import servidor.usuarios.Asistente;
import modeloDominio.EstadoPartida;
import modeloDominio.Juego;
import servidor.usuarios.AsistenteI;
import servidor.usuarios.Jugador;
import org.w3c.dom.Document;
import servidor.usuarios.JugadorI;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public abstract class Partida{
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
	private final String nombre;//identificador partida
	public String getNombre() {
		return nombre;
	}
	public abstract List<JugadorI> getJugadores();
	public abstract List<AsistenteI> getEspectadores();
	public EstadoPartida getEstado() {
		return estado;
	}
	public void setEstado(EstadoPartida estado) {
		this.estado = estado;
	}
	private EstadoPartida estado;
	protected Partida() {
		this.nombre = "";
	}

	//Factoría y constructies
	public static Partida PartidaFactoria(Document xml, Juego juego){
		switch (juego){
			case CHINCHON : return new PartidaChinchon(xml);
			default: return null;
		}
	}
	protected Partida(Document XML) {
		this.nombre = "prueba";
	}
	//Funcionamiento partida
	public abstract void iniciarPartida();//Realiza los preparativos y pasa al  estado encurso
	protected abstract JugadorI siguienteTurno();//Devuelve el siguiente jugador
	protected abstract void repartirMano();//Reparte las cartas y demás preparativos justo antes de empezar
	protected abstract void atenderClientes();//Consulta si hay peticiones de los clientes y las procesa
	private void bucleJuego(){
		//Comienzo juego
		while (this.estado==EstadoPartida.FINALIZADO){
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

	public boolean nuevoJugador(JugadorI jugador) {
		if(this.estado==EstadoPartida.ESPERANDO){
			this.getJugadores().add(jugador);
			return true;
		}
		return false;
	}
}
