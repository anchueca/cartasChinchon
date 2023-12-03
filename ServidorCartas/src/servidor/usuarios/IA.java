package servidor.usuarios;

import modeloDominio.baraja.Carta;
import modeloDominio.baraja.Mano;
import servidor.Partida;

public class IA extends Jugador {

    public IA(String nombre, Partida partida) {
        super(nombre,partida);
        // TODO Auto-generated constructor stub
    }
    /*
    Para convertir jugador (humano) en IA
     */
    public IA(Jugador jugador){
        super(jugador.getNombre(),jugador.getPartida(), jugador.verMano(),jugador.verPuntuacion());
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

    @Override
    public Mano verMano() {
        return null;
    }


    public String toString() {
        return "IA: " + super.toString();
    }
}
