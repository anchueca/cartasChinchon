package cliente;

import modeloDominio.Carta;
import modeloDominio.Mano;

import java.util.Scanner;

public class ConsolaChinchon extends Thread{
    private PresentacionChinchon partida;
    private Carta descubierta;
    private Mano mano;
    private boolean turno;
    public ConsolaChinchon(PresentacionChinchon partida) {
        this.partida = partida;
        this.mano=this.partida.consultarMano();
        this.descubierta=this.partida.consultarCartaDescubierta();
    }
    public void run(){
        Scanner in=new Scanner(System.in);
        while (true){
            this.pintarPantalla();
            if(in.hasNext())this.procesarInstrccion(in.nextLine());
            if(this.partida.consultarPartidaActualizada()){
                this.descubierta=this.partida.consultarCartaDescubierta();
                this.turno=this.partida.consultarTurno();
            }
        }


    }

    private void pintarPantalla(){

    }
    private void procesarInstrccion(String instruccion){

    }
}
