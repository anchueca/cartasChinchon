package cliente;

public class Actualizador extends Thread {

    private final AccionesConsola partida;
    private boolean pausado;

    public Actualizador(AccionesConsola partida) {
        this.partida = partida;
        this.pausado = false;
    }

    public void run() {//bloqueante
        try {
            while (!Thread.currentThread().isInterrupted()) {
                Thread.sleep(1000);
                synchronized (this) {
                    if (pausado) this.wait();
                    this.partida.actualizarPartida();//Compruebo si se han producido cambios
                }

            }
        } catch (InterruptedException e) {
            if (this.partida.enPartida()) new RuntimeException(e);
        }
    }

    public synchronized void pausar() {
        this.pausado = true;
    }

    public synchronized void reanudar() {
        this.pausado = false;
        this.notify();
    }
}
