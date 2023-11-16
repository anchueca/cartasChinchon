package servidor.usuarios;

import modeloDominio.EstadoPartida;
import modeloDominio.baraja.Carta;

import java.net.Socket;
import java.util.List;
import java.util.Map;

public class HumanoChinchon extends Humano implements JugadorChinchonI{
    public HumanoChinchon(Socket s, String nombre) {
        super(s, nombre);
    }

    @Override
    public boolean notificarPartidaActualizada() {
        return false;
    }

    @Override
    public EstadoPartida notificarEstadoPartida() {
        return null;
    }

    @Override
    public List<Jugador> notificarJugadores() {
        return null;
    }

    @Override
    public Carta notificarCartaDescubierta() {
        return null;
    }

    @Override
    public boolean notificarCerrado() {
        return false;
    }

    @Override
    public Map<Jugador, Integer> notificarPuntuaciones() {
        return null;
    }
}
