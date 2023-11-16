package servidor.usuarios;

import modeloDominio.EstadoPartida;

import java.util.List;

public interface AsistenteI {
    boolean notificarPartidaActualizada();
    //boolean notificarTurno();
    EstadoPartida notificarEstadoPartida();
    List<Jugador> notificarJugadores();
    boolean mensajePendiente();
    //recibirMensaje();
}

