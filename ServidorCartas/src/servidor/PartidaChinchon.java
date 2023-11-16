package servidor;

import modeloDominio.baraja.Baraja;
import org.w3c.dom.Document;
import servidor.usuarios.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class PartidaChinchon extends Partida {

	private final int TAM_MANO;
	private Map<JugadorChinchonI,Integer> puntuaciones;

	private Baraja baraja;
	private JugadorChinchonI turno;
	private List<JugadorChinchonI> jugadores;
	private List<AsistenteChinchonI> asistentes;

	@Override
	public List<JugadorI> getJugadores() {
		return new ArrayList<>(this.jugadores);
	}

	@Override
	public List<AsistenteI> getEspectadores() {
		return new ArrayList<>(this.asistentes);
	}

	public PartidaChinchon() {
		this.TAM_MANO=40;
	}
	public PartidaChinchon(Document xml){
		this();
	}

	@Override
	public void iniciarPartida() {

	}

	@Override
	public JugadorI siguienteTurno() {
		int turno=this.jugadores.indexOf(this.turno);
		turno%=this.jugadores.size();
		this.turno= this.jugadores.get(turno);
		return this.turno;
	}

	@Override
	public void repartirMano() {
		for(int i=0;i<this.TAM_MANO;i++)
			for (JugadorChinchonI jugador: jugadores
			 ) {
			jugador.darCarta(this.baraja.tomarCarta());
		}
	}

	@Override
	protected void atenderClientes() {
		for (AsistenteChinchonI asistente: this.asistentes
			 ) {

		}
		for (JugadorChinchonI jugador: this.jugadores
		) {

		}
	}

}
