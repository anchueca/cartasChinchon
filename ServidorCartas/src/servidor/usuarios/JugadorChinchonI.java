package servidor.usuarios;

import modeloDominio.baraja.Carta;

import java.util.Map;

public interface JugadorChinchonI extends JugadorI{
    Carta notificarCartaDescubierta();
    boolean notificarCerrado();
    Map<Jugador,Integer> notificarPuntuaciones();
}
