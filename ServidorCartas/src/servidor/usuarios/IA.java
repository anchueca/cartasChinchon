package servidor.usuarios;

import servidor.Partida;

public class IA extends Jugador {

    public IA(String nombre, Partida partida) {
        super(nombre, partida);
    }

    /*
    Para convertir jugador (humano) en IA
     */
    public IA(Jugador jugador) {
        super(jugador.getNombre(), jugador.getPartida(), jugador.getMano(), jugador.getPuntuacion());
    }
    public void recibirTurno(){
        super.recibirTurno();
        //Gestión de la IA
        //Por ahora de inteligencia tiene poco
        super.cogerCartaDecubierta();
        super.echarCarta(0);
        this.getPartida().siguienteTurno();
    }

    public String toString() {
        return "IA: " + super.toString();
    }

    public void recibirMensaje(String mensaje){

    }
}
