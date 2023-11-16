package servidor.usuarios;

import modeloDominio.EstadoPartida;

import java.net.Socket;
import java.util.List;

public class AsistenteChinchon extends Asistente implements AsistenteChinchonI{
    public AsistenteChinchon(String nombre, Socket s) {
        super(nombre, s);
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
}
