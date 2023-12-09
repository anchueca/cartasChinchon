package cliente;

import modeloDominio.Codigos;
import modeloDominio.EstadoPartida;
import modeloDominio.FaseChinchon;
import modeloDominio.ProcesadorMensajes;
import modeloDominio.baraja.Carta;
import modeloDominio.baraja.Mano;
import modeloDominio.excepciones.ReinicioEnComunicacionExcepcion;

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
    public List<String> listaJugadores()throws ReinicioEnComunicacionExcepcion {
        List<String> lista = new ArrayList<>();
        try {
            getProcesadorMensajes().abrirComunicacion(this.s);
        getProcesadorMensajes().enviarObjeto("jugadores", this.s);
            Codigos codigo= (Codigos) RecibeObjetos.getRecibeObjetos().recibirObjeto();
            if (codigo == Codigos.BIEN)
                lista = (List<String>) RecibeObjetos.getRecibeObjetos().recibirObjeto();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        } catch (ReinicioEnComunicacionExcepcion e) {
        } finally{
            getProcesadorMensajes().cerrarComunicacion(this.s);
        }
        return lista;
    }

    public String verTurno() throws ReinicioEnComunicacionExcepcion{
        try {
            getProcesadorMensajes().abrirComunicacion(this.s);
            getProcesadorMensajes().enviarObjeto("turno", this.s);
            Codigos codigo=(Codigos) RecibeObjetos.getRecibeObjetos().recibirObjeto();
            if (codigo== Codigos.BIEN)return  getProcesadorMensajes().recibirString(this.s);
            return null;
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }finally{
            getProcesadorMensajes().cerrarComunicacion(this.s);
        }
    }
    public String verAnfitrion()throws ReinicioEnComunicacionExcepcion {
        try {
            getProcesadorMensajes().abrirComunicacion(this.s);
            getProcesadorMensajes().enviarObjeto("anfitrion", this.s);
            Codigos codigo=(Codigos) RecibeObjetos.getRecibeObjetos().recibirObjeto();
            if (codigo== Codigos.BIEN)return  getProcesadorMensajes().recibirString(this.s);
            return null;
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }finally{
            getProcesadorMensajes().cerrarComunicacion(this.s);
        }
    }

    public Mano verMano() throws ReinicioEnComunicacionExcepcion{
        try {
            getProcesadorMensajes().abrirComunicacion(this.s);
            getProcesadorMensajes().enviarObjeto("verMano", this.s);
            Codigos codigo=(Codigos) RecibeObjetos.getRecibeObjetos().recibirObjeto();
            if (codigo == Codigos.BIEN)
                return (Mano) RecibeObjetos.getRecibeObjetos().recibirObjeto();
            return null;
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }finally{
            getProcesadorMensajes().cerrarComunicacion(this.s);
        }
    }

    public EstadoPartida verEstadoPartida()throws ReinicioEnComunicacionExcepcion {
        try {
            getProcesadorMensajes().abrirComunicacion(this.s);
            getProcesadorMensajes().enviarObjeto("estado", this.s);
            Codigos codigo=(Codigos) RecibeObjetos.getRecibeObjetos().recibirObjeto();
            if (codigo == Codigos.BIEN)
                return (EstadoPartida) RecibeObjetos.getRecibeObjetos().recibirObjeto();
            getProcesadorMensajes().enviarObjeto(Codigos.MAL, this.s);
            return null;
        } catch (InterruptedException | ClassCastException e) {
            getProcesadorMensajes().enviarObjeto(Codigos.REINICIO, this.s);
            throw new ReinicioEnComunicacionExcepcion();
        }finally{
            getProcesadorMensajes().cerrarComunicacion(this.s);
        }
    }
    public FaseChinchon verFasePartida()throws ReinicioEnComunicacionExcepcion {
        try {
            getProcesadorMensajes().abrirComunicacion(this.s);
            getProcesadorMensajes().enviarObjeto("fase", this.s);
            Codigos codigo=(Codigos) RecibeObjetos.getRecibeObjetos().recibirObjeto();
            if (codigo == Codigos.BIEN)
                return (FaseChinchon) RecibeObjetos.getRecibeObjetos().recibirObjeto();
            return null;
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }finally{
            getProcesadorMensajes().cerrarComunicacion(this.s);
        }
    }

    public Carta verCartaDescubierta()throws ReinicioEnComunicacionExcepcion {
        try {
            getProcesadorMensajes().abrirComunicacion(this.s);
            getProcesadorMensajes().enviarObjeto("cartaDescubierta", this.s);
            Codigos codigo=(Codigos) RecibeObjetos.getRecibeObjetos().recibirObjeto();
            if (codigo == Codigos.BIEN)
                return (Carta) RecibeObjetos.getRecibeObjetos().recibirObjeto();
            return null;
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }finally{
            getProcesadorMensajes().cerrarComunicacion(this.s);
        }
    }

    public boolean verManoCerrada()throws ReinicioEnComunicacionExcepcion {
        return false;
    }

    public Map<String, Integer> verPuntuaciones() throws ReinicioEnComunicacionExcepcion{
        try {
            getProcesadorMensajes().abrirComunicacion(this.s);

            getProcesadorMensajes().enviarObjeto("puntuaciones", this.s);
            Codigos codigo=(Codigos) RecibeObjetos.getRecibeObjetos().recibirObjeto();
            if (codigo == Codigos.BIEN)
                return (Map<String, Integer>) RecibeObjetos.getRecibeObjetos().recibirObjeto();
            return null;
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }finally{
            getProcesadorMensajes().cerrarComunicacion(this.s);
        }
    }
//////////////////ACCIOENS//////////////////////////
    public boolean empezarPartida()throws ReinicioEnComunicacionExcepcion {
        try {
            getProcesadorMensajes().abrirComunicacion(this.s);

            getProcesadorMensajes().enviarObjeto("empezar", this.s);
            Codigos codigo=(Codigos) RecibeObjetos.getRecibeObjetos().recibirObjeto();
            return codigo == Codigos.BIEN;
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }finally{
            getProcesadorMensajes().cerrarComunicacion(this.s);
        }
    }

    public Carta cogerCartaCubierta() throws ReinicioEnComunicacionExcepcion{
        try {
            getProcesadorMensajes().abrirComunicacion(this.s);

            getProcesadorMensajes().enviarObjeto("cogerDescubierta", this.s);
            Codigos codigo=(Codigos) RecibeObjetos.getRecibeObjetos().recibirObjeto();
            if(codigo == Codigos.BIEN)
                return (Carta) RecibeObjetos.getRecibeObjetos().recibirObjeto();
            return null;
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }finally{
            getProcesadorMensajes().cerrarComunicacion(this.s);
        }
    }

    public Carta cogerCartaDecubierta() throws ReinicioEnComunicacionExcepcion{
        try {
            getProcesadorMensajes().abrirComunicacion(this.s);

            getProcesadorMensajes().enviarObjeto("cogerCubierta", this.s);
            Codigos codigo=(Codigos) RecibeObjetos.getRecibeObjetos().recibirObjeto();
            if(codigo == Codigos.BIEN)
                return (Carta) RecibeObjetos.getRecibeObjetos().recibirObjeto();
            return null;
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }finally{
            getProcesadorMensajes().cerrarComunicacion(this.s);
        }
    }

    public Codigos echarCarta(int carta)throws ReinicioEnComunicacionExcepcion {
        try {
            getProcesadorMensajes().abrirComunicacion(this.s);

            getProcesadorMensajes().enviarObjeto("echar "+carta, this.s);
            Codigos codigo=(Codigos) RecibeObjetos.getRecibeObjetos().recibirObjeto();
            return codigo;
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }finally{
            getProcesadorMensajes().cerrarComunicacion(this.s);
        }
    }

    public Codigos cerrar(int carta)throws ReinicioEnComunicacionExcepcion {
        try {
            getProcesadorMensajes().abrirComunicacion(this.s);

            getProcesadorMensajes().enviarObjeto("cerrar "+carta, this.s);
            Codigos codigo=(Codigos) RecibeObjetos.getRecibeObjetos().recibirObjeto();
            return codigo;
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }finally{
            getProcesadorMensajes().cerrarComunicacion(this.s);
        }
    }

    public Codigos meterCarta(int carta,int i)throws ReinicioEnComunicacionExcepcion {
        try {
            getProcesadorMensajes().abrirComunicacion(this.s);

            getProcesadorMensajes().enviarObjeto("meter "+carta+" "+i, this.s);
            Codigos codigo=(Codigos) RecibeObjetos.getRecibeObjetos().recibirObjeto();
            return codigo;
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }finally{
            getProcesadorMensajes().cerrarComunicacion(this.s);
        }
    }

    public boolean moverMano(int i, int j)throws ReinicioEnComunicacionExcepcion {
        try {
            getProcesadorMensajes().abrirComunicacion(this.s);

            getProcesadorMensajes().enviarObjeto("mover " + i + " " + j, this.s);
            Codigos codigo=(Codigos) RecibeObjetos.getRecibeObjetos().recibirObjeto();
            return codigo == Codigos.BIEN;
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }finally{
            getProcesadorMensajes().cerrarComunicacion(this.s);
        }
    }

    public boolean salir()throws ReinicioEnComunicacionExcepcion {
        try {
            getProcesadorMensajes().abrirComunicacion(this.s);

            getProcesadorMensajes().enviarObjeto("salir", this.s);
            Codigos codigo=(Codigos) RecibeObjetos.getRecibeObjetos().recibirObjeto();
            return codigo == Codigos.BIEN;
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }finally{
            getProcesadorMensajes().cerrarComunicacion(this.s);
        }
    }

    public boolean ordenar()throws ReinicioEnComunicacionExcepcion {
        try {
            getProcesadorMensajes().abrirComunicacion(this.s);

            getProcesadorMensajes().enviarObjeto("ordenar", this.s);
            Codigos codigo=(Codigos) RecibeObjetos.getRecibeObjetos().recibirObjeto();
            return codigo == Codigos.BIEN;
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }finally{
            getProcesadorMensajes().cerrarComunicacion(this.s);
        }
    }

    public boolean crearIA(String nombre) throws ReinicioEnComunicacionExcepcion {
        try {
            getProcesadorMensajes().abrirComunicacion(this.s);

            getProcesadorMensajes().enviarObjeto("crearIA "+nombre, this.s);
            Codigos codigo=(Codigos) RecibeObjetos.getRecibeObjetos().recibirObjeto();
            return codigo == Codigos.BIEN;
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }finally{
            getProcesadorMensajes().cerrarComunicacion(this.s);
        }
    }


    ///////////////MENSAJES///////////////
    /*
    El procesamienot de mensajes se realiza de forma diferente al restod el acomunicación (es el servior quien inicia
    la comunicación)
     */
    public String procesarMensaje(Codigos codigo) throws ReinicioEnComunicacionExcepcion {
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
/*
Realiza el envío de un mensaje para el resto de jugadores
 */
    public void enviarChat(String texto) throws ReinicioEnComunicacionExcepcion {
        try {
            getProcesadorMensajes().abrirComunicacion(this.s);
            getProcesadorMensajes().enviarObjeto("chat", this.s);
            Codigos codigo=(Codigos) RecibeObjetos.getRecibeObjetos().recibirObjeto();
            if(codigo == Codigos.BIEN)getProcesadorMensajes().enviarObjeto(texto, this.s);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }finally{
            getProcesadorMensajes().cerrarComunicacion(this.s);
        }
    }
}
