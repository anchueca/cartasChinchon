package servidor.usuarios;

import modeloDominio.baraja.Carta;
import servidor.Partida;

import java.net.Socket;

public class Humano extends Jugador {

    private Socket s;
    public Humano(String nombre, Partida partida,Socket s) {
        super(nombre,partida);this.s=s;
    }

    @Override
    public void darTurno() {

    }

    @Override
    public boolean cogerCartaCubierta() {
        return false;
    }

    @Override
    public boolean cogerCartaDecubierta() {
        return false;
    }

    @Override
    public boolean echarCarta(Carta carta) {
        return false;
    }

    @Override
    public boolean cerrar(Carta carta) {
        return false;
    }

    @Override
    public boolean meterCarta(Carta carta) {
        return false;
    }

    @Override
    public boolean moverMano(int i, int j) {
        return false;
    }

    @Override
    public boolean verPartidaActualizada() {
        return false;
    }


}
