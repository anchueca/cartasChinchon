package cliente.interfaz;

import cliente.ProcesadorComandos;

import java.util.Scanner;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class ConsolaSimple {

    private ProcesadorComandos procesadorComandos;
    private boolean salida;

    public void main(String[] args){
        Scanner in=new Scanner(System.in);
        //this.interfaz
        System.out.println("Juego chinchon");
        System.out.println("escribe ayuda para ver los comandos");

        ExecutorService executor= Executors.newSingleThreadExecutor();
        Future<String> entrada= executor.submit(() -> {return in.nextLine();});
        //Bucle de juego
        /*while (!this.salida){
            //Si ha habido cambios actualizo si estoy en partida
            if(this.acciones.enPartida() && this.acciones.partidaActualizada()){
                if(this.acciones.estadoPartida()== EstadoPartida.ENCURSO)this.acciones.pintarPartida();
            }
            //Compruebo entrada del usuario
            if(entrada.isDone()){
                try {
                    System.out.println(this.procesadorComandos.procesarInstrccion(entrada.get()));
                    if(this.partida!=null)System.out.println(this.pintarPartida());
                    entrada= executor.submit(() -> {return in.nextLine();});
                } catch (InterruptedException | ExecutionException e) {
                    System.out.println("Error en captura");
                }
            }
            try {
                Thread.sleep(10);
            } catch (InterruptedException e) {

            }
        }*/
        //in.close();
        System.out.println("Ceeradno programa....");
    }
}
