package servidor.usuarios;

import modeloDominio.EstadoPartida;
import modeloDominio.VerChinchonI;

import java.util.List;

public interface AsistenteI extends VerChinchonI {
    boolean notificarPartidaActualizada();
    //boolean notificarTurno();
    EstadoPartida notificarEstadoPartida();
    List<Jugador> notificarJugadores();
    boolean mensajePendiente();
    String recibirMensaje();

}

