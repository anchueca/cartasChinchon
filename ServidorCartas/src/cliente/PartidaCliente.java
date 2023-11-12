package cliente;

import modeloDominio.Mano;

import java.io.IOException;
import java.net.Socket;

public abstract class PartidaCliente {
    private Mano mano;
    private PresentacionI interfaz;
    private boolean salida;
    Socket s;

    protected PartidaCliente(Socket s,PresentacionI interfaz) {
        this.s = s;
        this.interfaz=interfaz;
        salida=false;
    }

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

    public PresentacionI getInterfaz(){
        return this.interfaz;
    }

    public Mano getMano(){
        return this.mano;
    }
}
