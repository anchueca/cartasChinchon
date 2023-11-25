package cliente;

import modeloDominio.AccionesChinchonI;
import modeloDominio.EstadoPartida;
import modeloDominio.VerChinchonI;
import modeloDominio.baraja.Carta;
import modeloDominio.baraja.Mano;
import servidor.Servidor;
import servidor.usuarios.Jugador;

import java.util.List;
import java.util.Map;

/*
Clase de la partida general del cliente
 */
public class PartidaCliente implements AccionesChinchonI, VerChinchonI {
    
    private boolean partidaActualizada;
    private final String nombreJugador;
    private final String nombrePartida;

    static Servidor server=new Servidor();//Para pruebas

    public PartidaCliente(String nombrePartida,String nombreJugador) {
        this.partidaActualizada=false;
        this.nombreJugador=nombreJugador;
        this.nombrePartida=nombrePartida;
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

    public void actualizarPartida(){
        //this.server
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


    public List<String> listaJugadores(){
        return PartidaCliente.server.listaJugadores(this.nombrePartida);
    }

}
