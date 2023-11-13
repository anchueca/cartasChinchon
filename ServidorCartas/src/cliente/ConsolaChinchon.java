package cliente;

import modeloDominio.Carta;
import modeloDominio.Mano;

import java.util.Scanner;

import static java.lang.Integer.parseInt;

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
    /*
    Inicio de la partida que contiene el bucle de juego
     */
    public void run(){
        Scanner in=new Scanner(System.in);
        //Bucle de juego
        while (!this.isInterrupted()){
            //Si ha habido cambios actualizo
            if(this.partida.consultarPartidaActualizada()){
                this.descubierta=this.partida.consultarCartaDescubierta();
                this.turno=this.partida.consultarTurno();
                this.pintarPantalla();
            }
            //Compruebo entrada del usuario
            if(in.hasNext())this.procesarInstrccion(in.nextLine());
        }
    }
/*
Se encarga de dibujar en consola la partida
 */
    private void pintarPantalla(){
        System.out.print("\n\n\n");
        for (Carta carta: this.mano
             ) {
            System.out.print(" "+carta);
        }
    }
    /*
    Interpreta e invoca las intrucciones
     */
    private void procesarInstrccion(String instruccion) {
        String[] palabras = instruccion.split("\\s+");
        try {
            if (palabras[0].equals("mover")) {
                if (palabras.length == 3) {
                        this.mano.permutar(parseInt(palabras[1]), parseInt(palabras[2]));
                } else throw new NumeroParametrosExcepcion();
            } else if (palabras[0].equals("echar")) {
                if (palabras.length == 2) {
                    int i=parseInt(palabras[1]);
                    if(this.partida.echarCarta(this.mano.verCarta(i)))this.mano.tomarCarta(i);
                    else System.out.println("Jugada no válida");
                } else throw new NumeroParametrosExcepcion();
            } else if (palabras[0].equals("cerrar")) {
                if (palabras.length == 2) {
                    int i=parseInt(palabras[1]);
                    if(this.partida.cerrar(this.mano.verCarta(i)))this.mano.tomarCarta(i);
                    else System.out.println("Jugada no válida");
                } else throw new NumeroParametrosExcepcion();
            } else if (palabras[0].equals("coger")) {
                Carta carta=null;
                if (palabras.length == 1)carta=this.partida.cogerCartaCubierta();
                if (palabras.length == 2) {
                    if(palabras[1].compareTo("descubierta")==0){
                        carta=this.partida.cogerCartaDescubierta();

                    }else if(palabras[1].compareTo("cubierta")==0){
                        carta=this.partida.cogerCartaCubierta();
                    }
                } else throw new NumeroParametrosExcepcion();
                if(carta==null)System.out.println("Jugada no válida");
            } else if (palabras[0].equals("ordenar")) {
                this.mano.ordenar();
            } else if (palabras[0].equals("salir")) {
                System.out.println("Saliendo...");
            } else {
                System.out.println("Comando no reconocido.");
            }
        } catch (NumeroParametrosExcepcion ex) {
            System.out.println("Número de parámetros incorrecto.");
        }catch (NumberFormatException e){
            System.out.println("Parámetros incorrectos");
        }
    }
}
