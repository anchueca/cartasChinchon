package cliente;

import modeloDominio.*;
import modeloDominio.baraja.Carta;
import modeloDominio.baraja.Mano;
import servidor.usuarios.Jugador;
import org.w3c.dom.Document;

import java.io.IOException;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/*
Implementaciṕn de la partida propia del chinchón
 */
public class PartidaClienteChinchon extends PartidaCliente implements PresentacionChinchon{
    private Map<Jugador,Integer> jugadores;
    private Carta cartaDescubierta;
    public PartidaClienteChinchon(Socket s) {
        super(s);
        this.jugadores= HashMap.newHashMap(2);
        this.cartaDescubierta=null;
    }

    /*
    Recibir y procesar mensajes del servidor
     */
    protected void atenderServidor() throws IOException {
        if(!ProcesadorMensajes.enEspera(this.s))return;
        Document xml= (Document) ProcesadorMensajes.recibirObjeto(this.s);
    }
    protected void bienvenidaPartida() {

    }
    private Carta getCartaDescubierta() {
        return cartaDescubierta;
    }
    protected List<Jugador> getJugadores() {
        return new ArrayList<Jugador>(this.jugadores.keySet());
    }

   // Peticiones(mensajes) al servidores
    public Carta cogerCartaCubierta() {
        return null;
    }

    @Override
    public Carta cogerCartaDecubierta() {
        return null;
    }

    public Carta cogerCartaDescubierta() {
        return null;
    }
    public boolean echarCarta(Carta carta) {
        return false;
    }
    public boolean cerrar(Carta carta) {
        return false;
    }

    @Override
    public boolean meterCarta(Carta carta) {
        return false;
    }

    //Servicios a interfaz
    public Carta verCartaDescubierta() {
        return this.getCartaDescubierta();
    }
    public boolean verCerrado() {
        return false;
    }
    public boolean verPartidaActualizada() {
        return false;
    }
    public boolean verTurno() {
        return this.isTurno();
    }
    public Mano verMano() {
        return this.getMano();
    }
    public EstadoPartida verEstadoPartida() {
        return this.estadoPartida();
    }
    public List<Jugador> verJugadores(){
        return new ArrayList<Jugador>(this.jugadores.keySet());
    }
    public Map<Jugador,Integer> verPuntuaciones(){
        return this.jugadores;
    }
}
