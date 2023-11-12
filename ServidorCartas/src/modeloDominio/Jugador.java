package modeloDominio;

public abstract class Jugador extends Asistente{
	
	protected Carta[] mano;
	
	protected Jugador(String nombre) {
		super(nombre);
	}
	
}
