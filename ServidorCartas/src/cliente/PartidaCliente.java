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
    private boolean salida;
    static Servidor server=new Servidor();//Para pruebas
    public PartidaCliente(String nombrePartida,String nombreJugador) {
        this.nombreJugador=nombreJugador;
        this.nombrePartida=nombrePartida;
        this.salida=false;
    }

    public boolean enFuncionamiento(){return this.salida;}
    public String getNombrePartida(){
        return this.nombrePartida;
    }
    public String getNombreJuagador(){
        return this.nombreJugador;
    }

    public boolean verPartidaActualizada() {
        return PartidaCliente.server.partidaActualizada(this.nombrePartida,this.nombreJugador);
    }

    public boolean verTurno() {
        return false;
    }

    public Mano verMano() {
        return PartidaCliente.server.verMano(this.nombrePartida,this.nombreJugador);
    }

    public EstadoPartida verEstadoPartida() {
        return PartidaCliente.server.estadoPartida(this.nombrePartida);
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
    public boolean salir(){
        this.salida= PartidaCliente.server.abandonarPartida(this.nombrePartida,this.nombreJugador);
        return salida;
    }

    public void ordenar(){
        Cliente.server.ordenarMano(this.nombrePartida,this.nombreJugador);
    }

}
