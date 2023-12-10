package cliente;

import modeloDominio.Codigos;
import modeloDominio.ProcesadorMensajes;
import modeloDominio.excepciones.ReinicioEnComunicacionExcepcion;

import java.net.Socket;
import java.util.concurrent.Semaphore;

public class RecibeObjetos extends Thread{
    private static RecibeObjetos recibeObjetos=null;
    private RecibeMensajesI receptor;
    private Socket s;
    private Object objeto;
    private final Semaphore disponible=new Semaphore(0);
    private final Semaphore paraCoger =new Semaphore(0);

    public static synchronized RecibeObjetos getRecibeObjetos(Socket s){
        if(RecibeObjetos.recibeObjetos!=null){
            RecibeObjetos.recibeObjetos.interrupt();
        }
        RecibeObjetos.recibeObjetos=new RecibeObjetos(s);
        RecibeObjetos.recibeObjetos.setDaemon(true);
        RecibeObjetos.recibeObjetos.start();
        System.out.println("RecibeObjetos iniciado");
        return RecibeObjetos.recibeObjetos;
    }
    public static RecibeObjetos getRecibeObjetos(){return RecibeObjetos.recibeObjetos;}
    private RecibeObjetos(Socket s){
        this.s=s;
        this.objeto=null;
        this.receptor=null;
    }

    public void run(){
        while(!Thread.currentThread().isInterrupted() && !this.s.isClosed()){
            //Espero un mensaje
            try {
                this.objeto=ProcesadorMensajes.getProcesadorMensajes().recibirObjeto(this.s);
            } catch (ReinicioEnComunicacionExcepcion ignored) {
            }
            System.out.println("El recogedor ha recibido: "+this.objeto);
            //Si es un Codigos.MENSAJE es porque ha llegado una "interrupcion" del servidor
            if(this.objeto== Codigos.MENSAJE){
                //Proceso el mensaje
                if(this.receptor!=null)this.receptor.recibirMensaje((Codigos) this.objeto);
                else{
                    ProcesadorMensajes.getProcesadorMensajes().enviarObjeto(Codigos.MAL,this.s);
                }
                //Ahora continuo el bucle como si el mensaje no hubiera llegado
            }
            else{
                //Hay uno disponble
                this.disponible.release();
                //Espero a que lo cojan
                try {
                    this.paraCoger.acquire();
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
        }
    }

    public synchronized void setReceptor(RecibeMensajesI receptor){
        this.receptor=receptor;
        //Limbio el buffer
        try {
            if(objeto != null && receptor==null) {
                this.objeto=null;
                this.disponible.acquire();
            }
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
    public synchronized Object recibirObjeto() throws ReinicioEnComunicacionExcepcion {
        try {
            //Podré cogerlo si hay algo
            this.disponible.acquire();
        } catch (InterruptedException e) {
            return null;
        }
        Object objeto=this.objeto;
        this.objeto=null;
        //Como ya he cogido indico al hilo que espere otro
        this.paraCoger.release();
        System.out.println("Objeto recogido del RecibeObjetos: "+objeto);
        if(objeto==Codigos.REINICIO)throw new ReinicioEnComunicacionExcepcion();
        return objeto;
    }
}
