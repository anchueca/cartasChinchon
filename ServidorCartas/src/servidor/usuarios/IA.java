package servidor.usuarios;

import modeloDominio.baraja.Mano;
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

    public void recibirTurno() {
        super.recibirTurno();
        //Gestión de la IA
        //Por ahora de inteligencia tiene poco
        this.cogerCartaDecubierta();
        Mano nocasadas = this.getNoCasadas();
        //Si hay dos cartas o menos sin casar intento cerrar (pruebo si pedo cerrar con una y luego con otra).
        //Si hay una siempre podré
        //HAY QUE QUITARLA DE LA MANO ANTES DE MANDARLA (Y METERLA DE NUEVO SI NO ES VÁLIDA)
        if (nocasadas.numCartas() <= 2) if (this.cerrar(nocasadas.verCarta(0))) {
            this.getPartida().cerrar(this.mano.tomarCarta(nocasadas.verCarta(0)));
            return;
        }
        //Pruebo con la siguiente
        else if (this.cerrar(nocasadas.verCarta(1))) {
            {
                this.getPartida().cerrar(this.mano.tomarCarta(nocasadas.verCarta(1)));
                return;
            }
        }
        //Si no puedo echo una carta y el juego sigue
        this.echarCarta(0);
    }

    public String toString() {
        return "IA: " + super.toString();
    }

    public void recibirMensaje(String mensaje) {

    }
}
