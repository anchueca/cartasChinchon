package servidor.usuarios;

import modeloDominio.AccionesChinchonI;
import modeloDominio.baraja.Carta;
import modeloDominio.baraja.Mano;

public abstract class Jugador implements AccionesChinchonI {
	private String nombre;
	protected Mano mano;
	private int puntuacion;
	private boolean actualizado;
	protected Jugador(String nombre){
		this.nombre=nombre;
		this.mano=new Mano();
		this.puntuacion=0;
		this.actualizado=false;
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
	/*
	Devuelve falso si ha habido cambios (llamada a actualizado()) desde la última vez que se llamó la método.
	 */
	public boolean estaActualizado(){
		if(!this.actualizado){
			this.actualizado=true;
			return false;
		}
		return true;
	}
	/*
	Cada vez que se modifique un valor por parte del servior se llamará a este método
	 */
	public void actualizado(){this.actualizado=false;}
}
