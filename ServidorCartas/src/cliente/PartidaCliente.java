package cliente;

import modeloDominio.AccionesChinchonI;
import modeloDominio.EstadoPartida;
import modeloDominio.ProcesadorMensajes;
import modeloDominio.VerChinchonI;
import modeloDominio.baraja.Carta;
import org.w3c.dom.Document;
import modeloDominio.baraja.Mano;
import servidor.usuarios.Jugador;

import java.io.IOException;
import java.net.Socket;
import java.util.List;
import java.util.Map;

/*
Clase de la partida general del cliente
 */
public class PartidaCliente implements AccionesChinchonI, VerChinchonI {
    private Mano mano;
    private EstadoPartida estado;
    private boolean partidaActualizada;
    private boolean turno;

    public boolean isTurno() {
        return turno;
    }
    private void setTurno(boolean turno) {
        this.turno = turno;
    }
    public PartidaCliente() {
        this.turno=false;
        this.partidaActualizada=false;
        this.estado=EstadoPartida.ESPERANDO;
    }
    public EstadoPartida getEstado(){
        return this.estado;
    }
    /*
    Bucle de recepción de mensajes del servidor.
     */
    /*protected void bucleJuego(){
        try{
            while(true){
                this.atenderServidor();
                wait(1);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }*/

    @Override
    public boolean verPartidaActualizada() {
        return false;
    }

    @Override
    public boolean verTurno() {
        return false;
    }

    @Override
    public Mano verMano() {
        return null;
    }

    @Override
    public EstadoPartida verEstadoPartida() {
        return null;
    }

    @Override
    public List<Jugador> verJugadores() {
        return null;
    }

    public boolean empezarPartida(){
        return true;
    }

    @Override
    public Carta verCartaDescubierta() {
        return null;
    }

    @Override
    public boolean verCerrado() {
        return false;
    }

    @Override
    public Map<Jugador, Integer> verPuntuaciones() {
        return null;
    }

    public boolean getPartidaActualizada() {
        return this.partidaActualizada;
    }


    public boolean cogerCartaCubierta() {
        return false;
    }

    public boolean cogerCartaDecubierta() {
        return false;
    }

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



}
