package cliente;

import modeloDominio.Codigos;
import modeloDominio.EstadoPartida;
import modeloDominio.FaseChinchon;
import modeloDominio.ProcesadorMensajes;
import modeloDominio.baraja.Carta;
import modeloDominio.baraja.Mano;

import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static modeloDominio.ProcesadorMensajes.getProcesadorMensajes;

/*
Clase de la partida general del cliente
 */
public class PartidaCliente {

    private final Socket s;
    private final String nombreJugador;
    private final String nombrePartida;
    private boolean salida;

    public PartidaCliente(String nombrePartida, String nombreJugador, Socket s) {
        this.s = s;
        this.nombreJugador = nombreJugador;
        this.nombrePartida = nombrePartida;
        this.salida = false;
    }

    public boolean enFuncionamiento() {
        return this.salida;
    }

    public String getNombrePartida() {
        return this.nombrePartida;
    }

    public String getNombreJuagador() {
        return this.nombreJugador;
    }

    public String verTurno() {
        synchronized (this.s) {
            getProcesadorMensajes().enviarObjeto("turno", this.s);
            if (getProcesadorMensajes().recibirCodigo(this.s) == Codigos.BIEN)
                return  getProcesadorMensajes().recibirString(this.s);
            return null;
        }
    }

    public Mano verMano() {
        synchronized (this.s) {
            getProcesadorMensajes().enviarObjeto("verMano", this.s);
            if (getProcesadorMensajes().recibirCodigo(this.s) == Codigos.BIEN)
                return (Mano) getProcesadorMensajes().recibirObjeto(this.s);
            return null;
        }
    }

    public EstadoPartida verEstadoPartida() {
        synchronized (this.s) {
            getProcesadorMensajes().enviarObjeto("estado", this.s);
            if (getProcesadorMensajes().recibirCodigo(this.s) == Codigos.BIEN)
                return (EstadoPartida) getProcesadorMensajes().recibirObjeto(this.s);
            return null;
        }
    }
    public FaseChinchon verFasePartida() {
        synchronized (this.s) {
            getProcesadorMensajes().enviarObjeto("fase", this.s);
            if (getProcesadorMensajes().recibirCodigo(this.s) == Codigos.BIEN)
                return (FaseChinchon) getProcesadorMensajes().recibirObjeto(this.s);
            return null;
        }
    }

    public boolean empezarPartida() {
        synchronized (this.s) {
            getProcesadorMensajes().enviarObjeto("empezar", this.s);
            return getProcesadorMensajes().recibirCodigo(this.s) == Codigos.BIEN;
        }
    }

    public Carta verCartaDescubierta() {
        synchronized (this.s) {
            getProcesadorMensajes().enviarObjeto("cartaDescubierta", this.s);
            if (getProcesadorMensajes().recibirCodigo(this.s) == Codigos.BIEN)
                return (Carta) getProcesadorMensajes().recibirObjeto(this.s);
            return null;
        }
    }

    public boolean verCerrado() {
        return false;
    }

    public Map<String, Integer> verPuntuaciones() {
        synchronized (this.s) {
            getProcesadorMensajes().enviarObjeto("puntuaciones", this.s);
            if (getProcesadorMensajes().recibirCodigo(this.s) == Codigos.BIEN)
                return (Map<String, Integer>) getProcesadorMensajes().recibirObjeto(this.s);
            return null;
        }

    }

    /*
    Recibe las actualizaciones del servidor de forma asíncrona. Es solo de escucha del servidor. Por ahora
    la comunicación se inicia con un mensaje de tipo Codigos MENSAJE y luego el texto
     */
    public String actualizarPartida() {
        //Compruebo si hay algo pendiente
        //El proceso se realiza cuando no hay otras comunicaciones
        synchronized (this.s){
            //Si hay algo pendiente lo tomo
            if(ProcesadorMensajes.getProcesadorMensajes().enEspera(this.s)){
                //Debo recoger MENSAJE
                if(ProcesadorMensajes.getProcesadorMensajes().recibirCodigo(this.s)==Codigos.MENSAJE){
                    //Depende lo que fuera podría tener que procesarse aquí
                    return ProcesadorMensajes.getProcesadorMensajes().recibirString(this.s);
                }
                //Aquí podría haber más cosas
            }
            //Si no hay nada, pues nada
            return null;
        }

    }

    public Carta cogerCartaCubierta() {
        synchronized (this.s) {
            getProcesadorMensajes().enviarObjeto("cogerDescubierta", this.s);
            if(getProcesadorMensajes().recibirCodigo(this.s) == Codigos.BIEN)
                return (Carta) getProcesadorMensajes().recibirObjeto(this.s);
            return null;
        }
    }

    public Carta cogerCartaDecubierta() {
        synchronized (this.s) {
            getProcesadorMensajes().enviarObjeto("cogerCubierta", this.s);
            if(getProcesadorMensajes().recibirCodigo(this.s) == Codigos.BIEN)
                return (Carta) getProcesadorMensajes().recibirObjeto(this.s);
            return null;
        }
    }

    public Codigos echarCarta(int carta) {
        synchronized (this.s) {
            getProcesadorMensajes().enviarObjeto("echar "+carta, this.s);
            return getProcesadorMensajes().recibirCodigo(this.s);
        }
    }

    public Codigos cerrar(int carta) {
        synchronized (this.s) {
            getProcesadorMensajes().enviarObjeto("cerrar "+carta, this.s);
            return getProcesadorMensajes().recibirCodigo(this.s);
        }
    }

    public Codigos meterCarta(int carta,int i) {
        synchronized (this.s) {
            getProcesadorMensajes().enviarObjeto("meter "+carta+" "+i, this.s);
            return getProcesadorMensajes().recibirCodigo(this.s);
        }
    }

    public boolean moverMano(int i, int j) {
        synchronized (this.s) {
            getProcesadorMensajes().enviarObjeto("mover " + i + " " + j, this.s);
            return getProcesadorMensajes().recibirCodigo(this.s) == Codigos.BIEN;
        }
    }

    public List<String> listaJugadores() {
        synchronized (this.s) {
            getProcesadorMensajes().enviarObjeto("jugadores", this.s);
            List<String> lista = null;
            if (getProcesadorMensajes().recibirCodigo(this.s) == Codigos.BIEN)
                lista = (List<String>) getProcesadorMensajes().recibirObjeto(this.s);

            if (lista != null) return lista;
            return new ArrayList<>();
        }

    }

    public boolean salir() {
        synchronized (this.s) {
            getProcesadorMensajes().enviarObjeto("salir", this.s);
            return getProcesadorMensajes().recibirCodigo(this.s) == Codigos.BIEN;
        }

    }

    public boolean ordenar() {
        synchronized (this.s) {
            getProcesadorMensajes().enviarObjeto("ordenar", this.s);
            return getProcesadorMensajes().recibirCodigo(this.s) == Codigos.BIEN;
        }

    }

    public boolean crearIA(String nombre){
        synchronized (this.s) {
            getProcesadorMensajes().enviarObjeto("crearIA "+nombre, this.s);
            return getProcesadorMensajes().recibirCodigo(this.s) == Codigos.BIEN;
        }

    }

}
