package servidor.usuarios;

import modeloDominio.baraja.Mano;

public abstract class Jugador implements AsistenteI,JugadorI {
	private String nombre;
	protected Mano mano;
}
