package cliente;

/*
Este hilo es el encargado de esperar y recibir los mensajes del servidor para actualizar el cliente
Se encarga de llamar al método actalizarPartida de la partida para ver si ha habido alguna actualización.
 */
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
                Thread.sleep(500);
                synchronized (this) {
                    if (pausado) this.wait();
                    this.partida.recibirMensaje();//Compruebo si se han producido cambios
                }

            }
        } catch (InterruptedException e) {
            if (this.partida.enPartida()) throw new RuntimeException(e);
        }
    }

    /*
    Detiene el hilo y queda a la espera de ser reanudado. Esto se emplea cuando se abandona una partida y no
    tener que crear otro hilo
     */
    public synchronized void pausar() {
        this.pausado = true;
    }

    /*
    Reanuda el hilo
     */
    public synchronized void reanudar() {
        this.pausado = false;
        this.notify();
    }
}
