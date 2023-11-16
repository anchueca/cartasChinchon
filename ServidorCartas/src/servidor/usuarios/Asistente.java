package servidor.usuarios;

import java.net.Socket;

public abstract class Asistente implements AsistenteI{
	private String nombre;
	private Socket s;
	public Asistente(String nombre,Socket s) {
		this.nombre=nombre;
		this.s=s;
	}
}
