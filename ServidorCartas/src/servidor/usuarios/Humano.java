package servidor.usuarios;

import modeloDominio.EstadoPartida;
import modeloDominio.baraja.Carta;
import modeloDominio.baraja.Mano;

import java.util.List;
import java.util.Map;

public class Humano extends Jugador {

	public Humano(String nombre){
		super(nombre);
	}

	@Override
	public boolean cogerCartaCubierta() {
		return false;
	}

	@Override
	public boolean cogerCartaDecubierta() {
		return false;
	}

	@Override
	public boolean echarCarta(Carta carta) {
		return false;
	}

	@Override
	public boolean cerrar(Carta carta) {
		return false;
	}

	@Override
	public boolean meterCarta(Carta carta) {
		return false;
	}

	@Override
	public boolean moverMano(int i, int j) {
		return false;
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
	public Map<String, Integer> verPuntuaciones() {
		return null;
	}
}
