package modeloDominio;

import java.net.Socket;

public class Humano extends Jugador {
	private Socket s;
	
	public Humano(Socket s,String nombre){
		super(nombre);
		this.s=s;
	}
}
