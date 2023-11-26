package cliente;

import modeloDominio.EstadoPartida;
import modeloDominio.baraja.Carta;
import modeloDominio.baraja.Mano;
import servidor.Servidor;
import servidor.usuarios.Jugador;

import java.util.List;
import java.util.Map;

/*
Clase de la partida general del cliente
 */
public class PartidaCliente{
    
    private final String nombreJugador;
    private final String nombrePartida;

    static Servidor server=new Servidor();//Para pruebas

    public PartidaCliente(String nombrePartida,String nombreJugador) {
        this.nombreJugador=nombreJugador;
        this.nombrePartida=nombrePartida;
    }

    public String getNombrePartida(){
        return this.nombrePartida;
    }
    public String getNombreJuagador(){
        return this.nombreJugador;
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

    public boolean verPartidaActualizada() {
        return PartidaCliente.server.partidaActualizada(this.nombrePartida,this.nombreJugador);
    }

    public boolean verTurno() {
        return false;
    }

    public Mano verMano() {
        return null;
    }

    public EstadoPartida verEstadoPartida() {
        return PartidaCliente.server.estadoPartida(this.nombrePartida);
    }

    public List<Jugador> verJugadores() {
        return null;
    }

    public boolean empezarPartida(){
        PartidaCliente.server.iniciarPartida(this.nombrePartida,this.nombreJugador);
        return true;
    }

    public Carta verCartaDescubierta() {
        return null;
    }

    public boolean verCerrado() {
        return false;
    }

    public Map<String, Integer> verPuntuaciones() {
        return null;
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

    public boolean cerrar(Carta carta) {
        return false;
    }

    public boolean meterCarta(Carta carta) {
        return false;
    }

    public boolean moverMano(int i, int j) {
        return false;
    }
    public List<String> listaJugadores(){
        return PartidaCliente.server.listaJugadores(this.nombrePartida);
    }

}
