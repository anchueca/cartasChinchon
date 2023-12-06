package cliente;

/*
Este hilo es el encargado de esperar y recibir los mensajes del servidor para actualizar el cliente
 */
public class Actualizador extends Thread {

    //ES NECESARIO UN CONTROL PARA QUE NO SE INTERPONGA EN UNA SOLICITUD/RESPUESTA DEL CLIENTE
    private final AccionesConsola partida;
    private boolean pausado;

    public Actualizador(AccionesConsola partida) {
        this.partida = partida;
        this.pausado = false;
    }

    public void run() {//bloqueante
        try {
            while (!Thread.currentThread().isInterrupted()) {
                Thread.sleep(10000);
                synchronized (this) {
                    if (pausado) this.wait();
                    this.partida.actualizarPartida();//Compruebo si se han producido cambios
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
