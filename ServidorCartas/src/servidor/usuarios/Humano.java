package servidor.usuarios;

import java.net.Socket;

public abstract class Humano extends Jugador {
	private Socket s;
	
	protected Humano(Socket s,String nombre){
		this.s=s;
	}
}
