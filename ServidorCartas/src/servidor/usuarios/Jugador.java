package servidor.usuarios;

import modeloDominio.AccionesChinchonI;
import modeloDominio.baraja.Carta;
import modeloDominio.baraja.Mano;

public abstract class Jugador implements AccionesChinchonI {
	private String nombre;
	protected Mano mano;
	private int puntuacion;
	protected Jugador(String nombre){
		this.nombre=nombre;
		this.mano=new Mano();
		this.puntuacion=0;
	}

	public String getNombre(){
		return this.nombre;
	}

	public String toString(){
		return this.nombre;
	}

	public void darCarta(Carta carta){
		this.mano.añadirCarta(carta);
	}
	public Carta tomarCarta(int carta){
		return this.tomarCarta(carta);
	}
	public int verPuntuacion(){
		return this.puntuacion;
	}

	public Mano verMano(){
		return this.mano;
	}
}
