package cliente;

import modeloDominio.Mano;

import java.io.IOException;
import java.net.Socket;

/*
Clase de la partida general del cliente
 */
public abstract class PartidaCliente {
    private Mano mano;
    private boolean salida;
    protected Socket s;
    private boolean turno;

    protected PartidaCliente(Socket s) {
        this.s = s;
        salida=false;
    }
    /*
    Bucle de recepción de mensajes del servidor.
     */
    public void bucleJuego(){
        try{
            while(!salida){
                this.atenderServidor();
                wait(1);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    public abstract void atenderServidor() throws IOException;

    public Mano getMano(){
        return this.mano;
    }

    public abstract void bienvenidaPartida();

    public boolean partidaENCUrso(){
        return this.salida;
    }
}
