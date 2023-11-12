package servidor;


import modeloDominio.Asistente;
import modeloDominio.Jugador;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public abstract class Partida{
	private static final int MAXEspectadores=128;
	private static final int MAXPartidas=32;
	private static final Map<String,Partida> partidas=new HashMap<>();
	private static int PartidasActivas=0;
	private final String nombre;
	private List<Jugador> jugadores;
	private List<Asistente> espectadores;
	
	protected Partida() {
		this.nombre = "";
	}
	
	protected Partida(String XML) {
		this.nombre = "";
	}
	
	public boolean nuevoJugador(Jugador jugador) {
		this.jugadores.add(jugador);
		
		return true;
	}
	
	public boolean nuevoEspectador(Asistente asistente) {
		this.espectadores.add(asistente);
		return true;
	}
	
	public static boolean crearPartida(Juego juego,String XML){
		return false;

	}
	
	public static Partida buscarPartida(String nombre) {
		return Partida.partidas.get(nombre);
	}
		
}
