package cliente;

import modeloDominio.EstadoPartida;
import servidor.usuarios.Jugador;
import modeloDominio.baraja.Mano;

import java.util.List;

/*
Acciones generales de presentación
 */
public interface PresentacionI {
    boolean verPartidaActualizada();
    boolean verTurno();
    Mano verMano();
    EstadoPartida verEstadoPartida();
    List<Jugador> verJugadores();
    boolean empezarPartida();
}
