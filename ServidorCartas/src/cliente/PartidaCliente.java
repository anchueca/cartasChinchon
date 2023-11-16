package cliente;

import modeloDominio.EstadoPartida;
import servidor.usuarios.Jugador;
import modeloDominio.baraja.Mano;

import java.io.IOException;
import java.net.Socket;
import java.util.List;

/*
Clase de la partida general del cliente
 */
public abstract class PartidaCliente {
    private Mano mano;
    private EstadoPartida estado;
    protected Socket s;
    private boolean partidaActualizada;
    private boolean turno;
    public boolean isTurno() {
        return turno;
    }
    private void setTurno(boolean turno) {
        this.turno = turno;
    }
    private void setMano(Mano mano) {
        this.mano = mano;
    }
    protected abstract List<Jugador> getJugadores();

    protected Mano getMano(){
        return this.mano;
    }
    protected PartidaCliente(Socket s) {
        this.s = s;
        this.turno=false;
        this.partidaActualizada=false;
        this.estado=EstadoPartida.ESPERANDO;
    }
    /*
    Bucle de recepción de mensajes del servidor.
     */
    protected void bucleJuego(){
        this.estado=EstadoPartida.ENCURSO;
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
        finally {
            this.estado=EstadoPartida.FINALIZADO;
        }
    }
    protected abstract void atenderServidor() throws IOException;
    protected abstract void bienvenidaPartida();
    public EstadoPartida estadoPartida(){
        return this.estado;
    }
    public boolean empezarPartida(){

    }
}
