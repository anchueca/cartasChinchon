package servidor.usuarios;

import modeloDominio.AccionesChinchonI;
import modeloDominio.VerChinchonI;
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
}
