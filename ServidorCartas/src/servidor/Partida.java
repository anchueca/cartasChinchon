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

import javax.sound.midi.SysexMessage;
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
        IA ia=new IA(jugador.isEmpty()?"IA "+this.numIA:jugador,this);
        if(this.nuevoJugador(ia)){
            this.numIA++;
            this.enviarMensaje(ia+" añadido a la partida");
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
            for(int i=0,j=this.numJugadores();i<j;i++){
                //Recorro los jugadores
                this.anfitrion = this.jugadores.get(i);
                //Si el nuevo anfitrion no es el propio jugador o una IA salgo
                if (this.anfitrion != jugador && !(this.anfitrion instanceof IA))break;
                //Si no pongo null
                this.anfitrion=null;
            }
        }
        //Si no lo echo
        if (this.estado == EstadoPartida.ESPERANDO || this.estado == EstadoPartida.FINALIZADO){
            this.jugadores.remove(jugador);
            this.enviarMensaje(jugador.getNombre()+" abandona la partida");
        }
        //Miro si queda algún jugador humano
        boolean salida=true;
        for(Jugador jugador1:this.jugadores)if(jugador1 instanceof Humano && jugador1!=jugador){
            salida=false;
            break;
        }
        //Si no hay nadie elimino la partida
        if(salida){
            System.out.println("Eliminando partida");
            this.hilo.shutdown();
            Servidor.finPartida(this);
            return true;
        }
        //Notifico el cambio si hay jugadores
        if(this.anfitrion!=null)this.enviarMensaje(this.anfitrion.getNombre()+" es el nuevo anfitrión");
        //Si la partida está empezada una IA tomo su control
        return this.estado != EstadoPartida.ENCURSO || this.robotizar(jugador);
    }

    public boolean robotizar(Jugador jugador) {
        this.jugadores.set(this.jugadores.indexOf(jugador), new IA(jugador));
        this.enviarMensaje("Una IA ha tomado el control de "+jugador.getNombre()+" la hora de las máquinas ha llegado");
        return true;
    }

/////////GESTIÓN PARTIDA////////////////

    /*
    Inicia la partida con la primera ronda
     */
    public void iniciarPartida() {
        this.estado = EstadoPartida.ENCURSO;
        this.enviarMensaje("La partida comienza");
        //Determino aleatoriamente el primer jugador. Será el siguiente por la implementación de inicioRonda
        this.turno=this.jugadores.get(Math.abs(new Random().nextInt()%this.numJugadores()));
        this.inicioRonda();
    }
/*
Inicia una nueva ronda
 */
    public void inicioRonda() {
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
        this.fase = FaseChinchon.ABIERTO;
        this.enviarMensaje("Nueva ronda");
        //Comienoz juego
        this.siguienteTurno();
    }
    /*
    Asigna el turno siguiente y lo notifica al jugador. Si es uns IA jugará
     */
    public void siguienteTurno() {
        int turno = this.jugadores.indexOf(this.turno);
        turno++;
        turno %= this.jugadores.size();
        this.turno = this.jugadores.get(turno);
        //Le doy el turno
        this.enviarMensaje("Te toca",this.turno);
        this.turno.recibirTurno();
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
        this.enviarMensaje(mensaje, new ArrayList<>(lista));
    }
    public void enviarMensaje(String mensaje, Collection<Jugador> jugadores){
        for (Jugador jugador: jugadores
        ) {
            jugador.recibirMensaje(mensaje);
        }
        /*Runnable hilo=new Runnable() {
            @Override
            public void run() {
                for (Jugador jugador: jugadores
                ) {
                    jugador.recibirMensaje(mensaje);
                }
            }
        };*/
        //this.hilo.execute(hilo);
    }


}
