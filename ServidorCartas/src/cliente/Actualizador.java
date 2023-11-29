package cliente;

public class Actualizador extends Thread{

    private AccionesConsola partida;

    public Actualizador(AccionesConsola partida){
        this.partida=partida;
    }

    public void run(){//bloqueante
        try {
            while (!Thread.currentThread().isInterrupted()) {
                Thread.sleep(1000);
                this.partida.actualizarPartida();//Compruebo si se han producido cambios
            }
        } catch (InterruptedException e) {
            if(this.partida.enPartida()) new RuntimeException(e);
        }
    }
}
