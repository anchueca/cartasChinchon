package servidor.usuarios;

import modeloDominio.AccionesChinchonI;
import modeloDominio.baraja.Mano;

public abstract class Jugador implements AsistenteI, AccionesChinchonI {
	private String nombre;
	protected Mano mano;
}
