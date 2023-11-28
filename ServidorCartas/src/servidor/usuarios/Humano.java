package servidor.usuarios;

import modeloDominio.baraja.Carta;

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





}
