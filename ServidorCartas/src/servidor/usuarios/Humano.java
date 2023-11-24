package servidor.usuarios;

import modeloDominio.EstadoPartida;

import java.net.Socket;
import java.util.List;

public abstract class Humano extends Jugador {
	private Socket s;
	
	protected Humano(Socket s,String nombre){
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
}
