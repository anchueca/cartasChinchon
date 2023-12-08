package servidor;


import modeloDominio.EstadoPartida;
import modeloDominio.FaseChinchon;
import modeloDominio.baraja.Baraja;
import modeloDominio.baraja.Carta;
import modeloDominio.baraja.Mano;
import modeloDominio.baraja.Tamano;
import servidor.usuarios.Humano;
import servidor.usuarios.IA;
import servidor.usuarios.Jugador;

import java.net.Socket;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Partida {

    //Atributos

    private final ExecutorService hilo =Executors.newSingleThreadExecutor();
    private final List<Jugador> jugadores;
    private int MAXJugadores;
    private final Baraja descubierta;
    private String nombre;//identificador partida
    private EstadoPartida estado;
    private FaseChinchon fase;
    private Jugador anfitrion;
    private Baraja baraja;
    private Jugador turno;
    private int numIA;


    //Factoría y constructores
    private Partida() {
        this.estado = EstadoPartida.ESPERANDO;
        this.jugadores = new ArrayList<>();
        this.fase = null;
        this.anfitrion = null;
        this.descubierta = new Baraja();
        this.numIA=0;
        //No puede haber más jugadores de los suficientes para tomar todas las cartas
        //Al menos 1 es para dejar un pequeño margen de cartas sobrantes
    }

    /*
    Crea la partida sin todavía jugadores. El primer jugador en entrar será el anfitrión
     */
    protected Partida(String nombrePartida, Tamano barajaTamano) {
        this();
        this.nombre = nombrePartida;
        this.baraja = Baraja.barajaFactoria(barajaTamano);
        this.MAXJugadores=this.baraja.numCartas()/7-1;
    }

    /*
     * Getters y setters
     */

    public String getNombre() {
        return nombre;
    }

    public EstadoPartida getEstado() {
        return estado;
    }

    public void setEstado(EstadoPartida estado) {
        this.estado = estado;
    }

    public FaseChinchon getFase() {
        return this.fase;
    }

    public Jugador getTurno() {
        return this.turno;
    }


    public Jugador getAnfitrion() {
        return this.anfitrion;
    }

    public int numJugadores() {
        return this.jugadores.size();
    }

    public List<Jugador> getJugadores() {
        return this.jugadores;
    }

    private List<String> getJugadoresS() {
        List<String> lista = new ArrayList<>();
        for (Jugador j : this.jugadores
        ) {
            lista.add(j.getNombre());
            System.out.println(j.getNombre());
        }
        return lista;
    }
    public Mano getMano(String jugador) {
        for (Jugador jugador1 : this.jugadores
        ) {
            if (jugador1.getNombre().compareTo(jugador) == 0) return jugador1.getMano();
        }
        return null;
    }

    public Carta getDescubierta() {
        return this.descubierta.verCarta();
    }
    public Carta cogerDescubierta(){
        this.enviarMensaje("Carta descubierta cogida");
        return this.baraja.tomarCarta();
    }
    public Carta cogerCubierta(){
        this.enviarMensaje("Carta cubierta cogida");
        return this.descubierta.tomarCarta();
    }
    public void echarCarta(Carta carta){
        this.enviarMensaje("Carta echada");
        this.descubierta.meterCarta(carta);
    }
    public void cerrar(Carta carta){
        //Técnicamente, la carta se echaría boca abajo sobre el montón, pero en el juego no tiene sentido tal cosa.
        //Meto la carta en la baraja porque ahí acabaarán todas al recoger todas las cartas
        this.baraja.meterCarta(carta);
        this.enviarMensaje("Ronda cerrada");
        this.fase=FaseChinchon.CERRADO;
    }

    //////JUGADORES////////

    public Humano nuevoJugador(String jugador, Socket s) {
        Humano humano =new Humano(jugador, this, s);
        if (this.nuevoJugador(humano)) {
            if (this.numJugadores()==1) this.anfitrion = humano;
            this.enviarMensaje(jugador+" entra a la partida");
            return humano;
        }
        return null;
    }
    private boolean nuevoJugador(Jugador jugador){
        if (this.estado == EstadoPartida.ESPERANDO && !this.getJugadoresS().contains(jugador)
                && this.numJugadores()<this.MAXJugadores) {
            this.jugadores.add(jugador);
            return true;
        }
        return false;
    }
    public boolean nuevoJugador(String jugador) {
        if(this.nuevoJugador(new IA(jugador.isEmpty()?"IA "+this.numIA:jugador,this))){
            this.numIA++;
            this.enviarMensaje(jugador+" añadido a la partida");
            return true;
        }
        return false;
    }

    /*
    Para abadonar la partida o expulsar a un jugador
     */
    public boolean expulsarJugador(Jugador jugador) {
        //Miro si es anfitrión
        if (this.anfitrion == jugador) {
            //Pongo como nuevo anfitrión al primeor de la lista
            this.anfitrion = this.jugadores.get(0);
            //Si resulta que es el mismo pongo al siguiente (si hay al menos dos personas)
            //TAL COMO ESTÁ UNA IA PODRÍA SER ANFITRIÓN
            if (this.anfitrion == jugador && this.numJugadores() != 1) this.anfitrion = this.jugadores.get(1);
            this.enviarMensaje(this.anfitrion.getNombre()+" es el nuevo anfitrión");
        }
        //Si no lo echo
        if (this.estado == EstadoPartida.ESPERANDO || this.estado == EstadoPartida.FINALIZADO){
            this.enviarMensaje(jugador.getNombre()+" abandona la partida");
            return this.jugadores.remove(jugador);
        }
        //Si la aprtida está empezada una IA tomo su control
        return this.robotizar(jugador);
    }

    public boolean robotizar(Jugador jugador) {
        this.jugadores.set(this.jugadores.indexOf(jugador), new IA(jugador));
        this.enviarMensaje("Una IA ha tomado el control de "+jugador.getNombre()+" la hora de las máquinas ha llegado");
        return true;
    }

/////////GESTIÓN PARTIDA////////////////

    public void iniciarPartida() {
        this.estado = EstadoPartida.ENCURSO;
        this.fase = FaseChinchon.ABIERTO;
        this.enviarMensaje("La partida comienza");
        //Determino aleatoriamente el primer jugador será el siguiente por la implementación de inicioRonda
        this.turno=this.jugadores.get(Math.abs(new Random().nextInt()%this.numJugadores()));
        this.inicioRonda();
    }

    public void inicioRonda() {
        this.enviarMensaje("Nueva ronda");
        this.repartirMano();
        this.fase = FaseChinchon.ABIERTO;
        this.siguienteTurno();
    }
    /*
    Asigna el turno siguiente
     */
    public void siguienteTurno() {
        int turno = this.jugadores.indexOf(this.turno);
        turno++;
        turno %= this.jugadores.size();
        this.turno = this.jugadores.get(turno);
        //Le doy el turno
        this.turno.recibirTurno();
        this.enviarMensaje("Te toca",this.turno);
    }

    /*
    Reparte la baraja entre todos los juadores y la decubierta
     */
    public void repartirMano() {
        //recojo las deescubiertas
        if (this.descubierta.numCartas() != 0) this.baraja.meterCarta(this.descubierta);
        //recojo la de los jugadores
        Carta carta;
        for (Jugador jugador : this.jugadores)
            while ((carta = jugador.tomarCarta(0)) != null) this.baraja.meterCarta(carta);

        this.baraja.barajar();
        //reparto
        for (int i = 0; i < 7; i++)
            for (Jugador jugador : jugadores
            ) {
                jugador.darCarta(this.baraja.tomarCarta());
            }
        this.descubierta.meterCarta(this.baraja.tomarCarta());
    }

    public String toString() {
        return this.nombre;
    }

    //////////Accioens//////////////////

    public int costeCarta(Carta carta) {
        int i = carta.getNumero();
        if (i < 10) return i;
        else return 10;
    }

    public void enviarMensaje(String mensaje){
        this.enviarMensaje(mensaje,this.jugadores);
    }
    public void enviarMensaje(String mensaje,Jugador jugador){
        List<Jugador> lista=new ArrayList<>();
        lista.add(jugador);
        this.enviarMensaje(mensaje,new ArrayList<Jugador>(lista));
    }
    public void enviarMensaje(String mensaje, Collection<Jugador> jugadores){
        System.out.println("Enviando: "+mensaje);
        this.hilo.execute(new Runnable() {
            @Override
            public void run() {
                for (Jugador jugador: jugadores
                ) {
                    jugador.recibirMensaje(mensaje);
                }
            }
        });
    }


}
