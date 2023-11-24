package servidor.usuarios;

import modeloDominio.EstadoPartida;
import modeloDominio.baraja.Carta;
import modeloDominio.baraja.Mano;

import java.net.Socket;
import java.util.List;
import java.util.Map;

public class Asistente implements AsistenteI{
	private String nombre;
	private Socket s;
	public Asistente(String nombre,Socket s) {
		this.nombre=nombre;
		this.s=s;
	}

	@Override
	public boolean notificarPartidaActualizada() {
		return false;
	}

	@Override
	public EstadoPartida notificarEstadoPartida() {
		return null;
	}

	@Override
	public List<Jugador> notificarJugadores() {
		return null;
	}

	@Override
	public boolean mensajePendiente() {
		return false;
	}

	@Override
	public String recibirMensaje() {
		return null;
	}

	@Override
	public boolean verPartidaActualizada() {
		return false;
	}

	@Override
	public boolean verTurno() {
		return false;
	}

	@Override
	public Mano verMano() {
		return null;
	}

	@Override
	public EstadoPartida verEstadoPartida() {
		return null;
	}

	@Override
	public List<Jugador> verJugadores() {
		return null;
	}

	@Override
	public boolean empezarPartida() {
		return false;
	}

	@Override
	public Carta verCartaDescubierta() {
		return null;
	}

	@Override
	public boolean verCerrado() {
		return false;
	}

	@Override
	public Map<Jugador, Integer> verPuntuaciones() {
		return null;
	}
}
