package modeloDominio;

import java.io.Serializable;

public class Carta implements Comparable<Carta>, Serializable {
	private Palo palo;



	private int numero;
	public Carta(int numero, Palo palo) {
		this.palo = palo;
		this.numero=numero;
	}

	public Palo getPalo() {
		return palo;
	}

	public void setPalo(Palo palo) {
		this.palo = palo;
	}

	public int getNumero() {
		return numero;
	}

	public void setNumero(int numero) {
		this.numero = numero;
	}

	@Override
	public int compareTo(Carta o) {
		if(this.getPalo()==o.getPalo()){
			if(this.getNumero()<o.getNumero())return -1;
			if(this.getNumero()>o.getNumero())return 1;
			return 0;
		}
		return this.getPalo().compareTo(o.getPalo());
	}

	public String toString(){
		if(this.getNumero()==1)return "As de "+this.getPalo();
		if(this.getNumero()==10)return "Sota de "+this.getPalo();
		if(this.getNumero()==11)return "Caballo de "+this.getPalo();
		if(this.getNumero()==12)return "Rey de "+this.getPalo();
		return this.getNumero()+"de "+this.getPalo();
	}
}
