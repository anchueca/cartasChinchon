package modeloDominio;

import modeloDominio.EstadoPartida;
import modeloDominio.baraja.Carta;
import servidor.usuarios.Jugador;
import modeloDominio.baraja.Mano;

import java.util.List;
import java.util.Map;

/*
Acciones generales de presentación y de consulta
 */
public interface VerChinchonI {
    boolean verPartidaActualizada();
    boolean verTurno();
    Mano verMano();
    EstadoPartida verEstadoPartida();
    List<Jugador> verJugadores();
    boolean empezarPartida();
    Carta verCartaDescubierta();
    boolean verCerrado();
    Map<Jugador,Integer> verPuntuaciones();
}
