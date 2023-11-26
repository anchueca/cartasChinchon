package servidor.usuarios;

import modeloDominio.AccionesChinchonI;
import modeloDominio.VerChinchonI;
import modeloDominio.baraja.Carta;
import modeloDominio.baraja.Mano;

public abstract class Jugador implements AccionesChinchonI, VerChinchonI {
	private String nombre;
	protected Mano mano;
	protected Jugador(String nombre){
		this.nombre=nombre;
		this.mano=new Mano();
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
}
