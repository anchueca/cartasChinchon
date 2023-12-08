package servidor.usuarios;

import modeloDominio.EstadoPartida;
import modeloDominio.FaseChinchon;
import modeloDominio.baraja.Carta;
import modeloDominio.baraja.Mano;
import servidor.Partida;

import java.util.HashMap;
import java.util.Map;

/*
El jugador realiza acciones que comunica a la partida a traves del atrobuto partida
 */
public abstract class Jugador {
    private final String nombre;
    protected Mano mano;
    private int puntuacion;
    private final Partida partida;
    private int turno;

    protected Jugador(String nombre, Partida partida) {
        this.nombre = nombre;
        this.mano = new Mano();
        this.puntuacion = 0;
        this.partida = partida;
    }

    protected Jugador(String nombre, Partida partida, Mano mano, int puntuacion) {
        this.nombre = nombre;
        this.partida = partida;
        this.mano = mano;
        this.puntuacion = puntuacion;
    }

    /////GETTERS Y SETTERS

    protected Partida getPartida() {
        return this.partida;
    }

    public void recibirTurno(){
        this.turno=2;
    }
    public int getTurno(){
        return this.turno;
    }
    public String getNombre() {
        return this.nombre;
    }
    public int getPuntuacion() {
        return this.puntuacion;
    }
    protected Map<String, Integer> puntuaciones() {
        Map<String, Integer> puntuaciones = new HashMap<>();
        for (Jugador jugador : this.partida.getJugadores()
        ) {
            puntuaciones.put(jugador.getNombre(), jugador.getPuntuacion());
        }
        return puntuaciones;
    }

    public Mano getMano() {
        return this.mano;
    }

    ////////BÁSICOS////////////
    public void darCarta(Carta carta) {
        this.mano.añadirCarta(carta);
    }
    public Carta tomarCarta(int carta) {
        return this.mano.tomarCarta(carta);
    }

    /////////ACCIOENS////////////
    protected boolean moverMano(int i, int j) {
        if (this.getPartida().getEstado() == EstadoPartida.ENCURSO){
            this.mano.permutar(i, j);
            return true;
        }
        return false;
    }
    protected boolean ordenarMano(){
    if (this.getPartida().getEstado() == EstadoPartida.ENCURSO) {
        this.getMano().ordenar();
        return true;
    }
    return false;
}
    /*
    Toma la carta descubierta de la mesa y la añade a la mano si la acción es legal
     */
    protected Carta cogerCartaCubierta() {
        Carta carta=null;
        if (this.getPartida().getEstado() == EstadoPartida.ENCURSO && this.turno==2) {
            carta=this.getPartida().cogerCubierta();
            this.mano.añadirCarta(carta);
            this.turno--;
        }
        return carta;
    }
    /*
    Toma una carta de la baraja y la añade a la mano si la acción es legal
     */
    protected Carta cogerCartaDecubierta() {
        Carta carta=null;
        if (this.getPartida().getEstado() == EstadoPartida.ENCURSO && this.turno==2) {
            carta=this.getPartida().cogerDescubierta();
            this.mano.añadirCarta(carta);
            this.turno--;
        }
        return carta;
    }
    /*
    Echa una carta al montón si la acción es legal
     */
    protected boolean echarCarta(int carta) {
        if (this.getPartida().getEstado() == EstadoPartida.ENCURSO && this.turno==1) {
            this.getPartida().echarCarta(this.mano.tomarCarta(carta));
            if(this.partida.getFase()== FaseChinchon.ABIERTO)this.getPartida().siguienteTurno();
            return true;
        }
        return false;
    }
    //HAY QUE COMPROBAR QUE SEA LEGAL MIRAR PUNTOS
    protected boolean cerrar(int carta){
        if (this.getPartida().getEstado() == EstadoPartida.ENCURSO && this.turno==1) {
            this.getPartida().cerrar(this.mano.tomarCarta(carta));
            return true;
        }
        return false;
    }

    //////////OTROS/////////////

    public abstract void recibirMensaje(String mensaje);

    protected boolean crearIA(String nombre){
        if (this.partida.nuevoJugador(nombre)) {
            return true;
        }
        return false;
    }
//DOs jugadores son iguales si tienen el mismo nombre
    @Override
    public boolean equals(Object obj) {
        if(obj instanceof Jugador){
            return this.nombre.compareTo(((Jugador) obj).nombre)==0;
        }
        return false;
    }

    public String toString() {
        return this.nombre;
    }

    //SIN IMPLEMENTAR//////
    protected boolean meterCarta(int carta, int destino) {
        if(this.partida.getFase()==FaseChinchon.CERRADO && this.turno==1){
            //LA JUGADA DEBE SER LEGAL. ESTÁ SIN COMPROBAR
            this.partida.getTurno().getMano().añadirCarta(this.mano.tomarCarta(carta),destino);
            this.partida.siguienteTurno();
            return true;
        }
        return false;
    }

}
