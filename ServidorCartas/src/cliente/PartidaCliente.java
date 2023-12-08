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

    private RecibeMensajesI receptor;
    private final Socket s;
    private final String nombreJugador;
    private final String nombrePartida;
    private boolean salida;

    public PartidaCliente(String nombrePartida, String nombreJugador, Socket s) {
        this.s = s;
        this.nombreJugador = nombreJugador;
        this.nombrePartida = nombrePartida;
        this.salida = false;
        this.receptor=null;
    }
    public PartidaCliente(String nombrePartida, String nombreJugador, Socket s,RecibeMensajesI receptor) {
        this.s = s;
        this.nombreJugador = nombreJugador;
        this.nombrePartida = nombrePartida;
        this.salida = false;
        this.receptor=receptor;
    }

    /////////////CONSULTAS////////////////////
    public boolean enFuncionamiento() {
        return this.salida;
    }

    public String getNombrePartida() {
        return this.nombrePartida;
    }

    public String getNombreJuagador() {
        return this.nombreJugador;
    }
    public List<String> listaJugadores() {
        try {
            getProcesadorMensajes().abrirConexion(this.s);

        getProcesadorMensajes().enviarObjeto("jugadores", this.s);
            List<String> lista = null;
            Codigos codigo= (Codigos) RecibeObjetos.getRecibeObjetos().recibirObjeto();
            if (codigo == Codigos.BIEN)
                lista = (List<String>) RecibeObjetos.getRecibeObjetos().recibirObjeto();

            if (lista != null) return lista;
            return new ArrayList<>();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }finally{
            getProcesadorMensajes().cerrarConexion(this.s);
        }
    }

    public String verTurno() {
        try {
            getProcesadorMensajes().abrirConexion(this.s);
            getProcesadorMensajes().enviarObjeto("turno", this.s);
            Codigos codigo=(Codigos) RecibeObjetos.getRecibeObjetos().recibirObjeto();
            if (codigo== Codigos.BIEN)return  getProcesadorMensajes().recibirString(this.s);
            return null;
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }finally{
            getProcesadorMensajes().cerrarConexion(this.s);
        }
    }

    public Mano verMano() {
        try {
            getProcesadorMensajes().abrirConexion(this.s);
            getProcesadorMensajes().enviarObjeto("verMano", this.s);
            Codigos codigo=(Codigos) RecibeObjetos.getRecibeObjetos().recibirObjeto();
            if (codigo == Codigos.BIEN)
                return (Mano) RecibeObjetos.getRecibeObjetos().recibirObjeto();
            return null;
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }finally{
            getProcesadorMensajes().cerrarConexion(this.s);
        }
    }

    public EstadoPartida verEstadoPartida() {
        try {
            getProcesadorMensajes().abrirConexion(this.s);
            getProcesadorMensajes().enviarObjeto("estado", this.s);
            Codigos codigo=(Codigos) RecibeObjetos.getRecibeObjetos().recibirObjeto();
            if (codigo == Codigos.BIEN)
                return (EstadoPartida) RecibeObjetos.getRecibeObjetos().recibirObjeto();
            return null;
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }finally{
            getProcesadorMensajes().cerrarConexion(this.s);
        }
    }
    public FaseChinchon verFasePartida() {
        try {
            getProcesadorMensajes().abrirConexion(this.s);
            getProcesadorMensajes().enviarObjeto("fase", this.s);
            Codigos codigo=(Codigos) RecibeObjetos.getRecibeObjetos().recibirObjeto();
            if (codigo == Codigos.BIEN)
                return (FaseChinchon) RecibeObjetos.getRecibeObjetos().recibirObjeto();
            return null;
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }finally{
            getProcesadorMensajes().cerrarConexion(this.s);
        }
    }

    public Carta verCartaDescubierta() {
        try {
            getProcesadorMensajes().abrirConexion(this.s);
            getProcesadorMensajes().enviarObjeto("cartaDescubierta", this.s);
            Codigos codigo=(Codigos) RecibeObjetos.getRecibeObjetos().recibirObjeto();
            if (codigo == Codigos.BIEN)
                return (Carta) RecibeObjetos.getRecibeObjetos().recibirObjeto();
            return null;
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }finally{
            getProcesadorMensajes().cerrarConexion(this.s);
        }
    }

    public boolean verCerrado() {
        return false;
    }

    public Map<String, Integer> verPuntuaciones() {
        try {
            getProcesadorMensajes().abrirConexion(this.s);

            getProcesadorMensajes().enviarObjeto("puntuaciones", this.s);
            Codigos codigo=(Codigos) RecibeObjetos.getRecibeObjetos().recibirObjeto();
            if (codigo == Codigos.BIEN)
                return (Map<String, Integer>) RecibeObjetos.getRecibeObjetos().recibirObjeto();
            return null;
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }finally{
            getProcesadorMensajes().cerrarConexion(this.s);
        }
    }
//////////////////ACCIOENS//////////////////////////
    public boolean empezarPartida() {
        try {
            getProcesadorMensajes().abrirConexion(this.s);

            getProcesadorMensajes().enviarObjeto("empezar", this.s);
            Codigos codigo=(Codigos) RecibeObjetos.getRecibeObjetos().recibirObjeto();
            return codigo == Codigos.BIEN;
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }finally{
            getProcesadorMensajes().cerrarConexion(this.s);
        }
    }

    public Carta cogerCartaCubierta() {
        try {
            getProcesadorMensajes().abrirConexion(this.s);

            getProcesadorMensajes().enviarObjeto("cogerDescubierta", this.s);
            Codigos codigo=(Codigos) RecibeObjetos.getRecibeObjetos().recibirObjeto();
            if(codigo == Codigos.BIEN)
                return (Carta) RecibeObjetos.getRecibeObjetos().recibirObjeto();
            return null;
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }finally{
            getProcesadorMensajes().cerrarConexion(this.s);
        }
    }

    public Carta cogerCartaDecubierta() {
        try {
            getProcesadorMensajes().abrirConexion(this.s);

            getProcesadorMensajes().enviarObjeto("cogerCubierta", this.s);
            Codigos codigo=(Codigos) RecibeObjetos.getRecibeObjetos().recibirObjeto();
            if(codigo == Codigos.BIEN)
                return (Carta) RecibeObjetos.getRecibeObjetos().recibirObjeto();
            return null;
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }finally{
            getProcesadorMensajes().cerrarConexion(this.s);
        }
    }

    public Codigos echarCarta(int carta) {
        try {
            getProcesadorMensajes().abrirConexion(this.s);

            getProcesadorMensajes().enviarObjeto("echar "+carta, this.s);
            Codigos codigo=(Codigos) RecibeObjetos.getRecibeObjetos().recibirObjeto();
            return codigo;
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }finally{
            getProcesadorMensajes().cerrarConexion(this.s);
        }
    }

    public Codigos cerrar(int carta) {
        try {
            getProcesadorMensajes().abrirConexion(this.s);

            getProcesadorMensajes().enviarObjeto("cerrar "+carta, this.s);
            Codigos codigo=(Codigos) RecibeObjetos.getRecibeObjetos().recibirObjeto();
            return codigo;
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }finally{
            getProcesadorMensajes().cerrarConexion(this.s);
        }
    }

    public Codigos meterCarta(int carta,int i) {
        try {
            getProcesadorMensajes().abrirConexion(this.s);

            getProcesadorMensajes().enviarObjeto("meter "+carta+" "+i, this.s);
            Codigos codigo=(Codigos) RecibeObjetos.getRecibeObjetos().recibirObjeto();
            return codigo;
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }finally{
            getProcesadorMensajes().cerrarConexion(this.s);
        }
    }

    public boolean moverMano(int i, int j) {
        try {
            getProcesadorMensajes().abrirConexion(this.s);

            getProcesadorMensajes().enviarObjeto("mover " + i + " " + j, this.s);
            Codigos codigo=(Codigos) RecibeObjetos.getRecibeObjetos().recibirObjeto();
            return codigo == Codigos.BIEN;
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }finally{
            getProcesadorMensajes().cerrarConexion(this.s);
        }
    }

    public boolean salir() {
        try {
            getProcesadorMensajes().abrirConexion(this.s);

            getProcesadorMensajes().enviarObjeto("salir", this.s);
            Codigos codigo=(Codigos) RecibeObjetos.getRecibeObjetos().recibirObjeto();
            return codigo == Codigos.BIEN;
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }finally{
            getProcesadorMensajes().cerrarConexion(this.s);
        }
    }

    public boolean ordenar() {
        try {
            getProcesadorMensajes().abrirConexion(this.s);

            getProcesadorMensajes().enviarObjeto("ordenar", this.s);
            Codigos codigo=(Codigos) RecibeObjetos.getRecibeObjetos().recibirObjeto();
            return codigo == Codigos.BIEN;
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }finally{
            getProcesadorMensajes().cerrarConexion(this.s);
        }
    }

    public boolean crearIA(String nombre){
        try {
            getProcesadorMensajes().abrirConexion(this.s);

            getProcesadorMensajes().enviarObjeto("crearIA "+nombre, this.s);
            Codigos codigo=(Codigos) RecibeObjetos.getRecibeObjetos().recibirObjeto();
            return codigo == Codigos.BIEN;
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }finally{
            getProcesadorMensajes().cerrarConexion(this.s);
        }
    }


    ///////////////MENSAJES///////////////
    public String procesarMensaje(Codigos codigo){
        System.out.println("Mensaje recibido del servidor con código: "+codigo);
        switch (codigo){
            case MENSAJE :
                //Si entra aquí desde el RecibeObjetos no puedo esperar
                if(Thread.currentThread()==RecibeObjetos.getRecibeObjetos())
                    return ProcesadorMensajes.getProcesadorMensajes().recibirString(this.s);
                //Si no entro de forma normal
                return (String) RecibeObjetos.getRecibeObjetos().recibirObjeto();
            default : return null;
        }
    }

    public void enviarChat(String texto) {
        try {
            getProcesadorMensajes().abrirConexion(this.s);
            getProcesadorMensajes().enviarObjeto("chat", this.s);
            Codigos codigo=(Codigos) RecibeObjetos.getRecibeObjetos().recibirObjeto();
            if(codigo == Codigos.BIEN)getProcesadorMensajes().enviarObjeto(texto, this.s);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }finally{
            getProcesadorMensajes().cerrarConexion(this.s);
        }
    }
}
