package cliente;

import modeloDominio.baraja.Carta;
import modeloDominio.baraja.Mano;

import java.util.Scanner;

import static java.lang.Integer.parseInt;
/*
Interfaz de usuario del chinchón por consola
 */
public class ConsolaChinchon extends Thread{
    private PresentacionChinchon partida;
    public ConsolaChinchon(PresentacionChinchon partida) {
        this.partida = partida;
    }
    /*
    Inicio de la partida que contiene el bucle de juego
     */
    public void run(){
        Scanner in=new Scanner(System.in);
        //Bucle de juego
        while (!this.isInterrupted()){
            //Si ha habido cambios actualizo
            if(this.partida.verPartidaActualizada()){
                this.pintarPantalla();
            }
            //Compruebo entrada del usuario
            if(in.hasNext()){
                this.procesarInstrccion(in.nextLine());
                this.pintarPantalla();
            }
        }
    }
/*
Se encarga de dibujar en consola la partida
 */
    private void pintarPantalla(){
        System.out.print("\n\n\nEstado de la partida: "+this.partida.verEstadoPartida());
        for (Carta carta: this.partida.verMano()
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
                        this.partida.verMano().permutar(parseInt(palabras[1]), parseInt(palabras[2]));
                } else throw new NumeroParametrosExcepcion();
            } else if (palabras[0].equals("echar")) {
                if (palabras.length == 2) {
                    int i=parseInt(palabras[1]);
                    Mano mano=this.partida.verMano();
                    if(this.partida.echarCarta(mano.verCarta(i)))mano.tomarCarta(i);
                    else System.out.println("Jugada no válida");
                } else throw new NumeroParametrosExcepcion();
            } else if (palabras[0].equals("cerrar")) {
                if (palabras.length == 2) {
                    int i=parseInt(palabras[1]);
                    Mano mano=this.partida.verMano();
                    if(this.partida.cerrar(mano.verCarta(i)))mano.tomarCarta(i);
                    else System.out.println("Jugada no válida");
                } else throw new NumeroParametrosExcepcion();
            } else if (palabras[0].equals("coger")) {
                Carta carta=null;
                if (palabras.length == 1)carta=this.partida.cogerCartaCubierta();
                if (palabras.length == 2) {
                    if(palabras[1].compareTo("descubierta")==0){
                        carta=this.partida.cogerCartaDecubierta();

                    }else if(palabras[1].compareTo("cubierta")==0){
                        carta=this.partida.cogerCartaCubierta();
                    }
                } else throw new NumeroParametrosExcepcion();
                if(carta==null)System.out.println("Jugada no válida");
            } else if (palabras[0].equals("ordenar")) {
                this.partida.verMano().ordenar();
            } else if (palabras[0].equals("salir")) {
                System.out.println("Saliendo...");
            } else if(palabras[0].equals("empezar")){
                this.partida.empezarPartida();
            } else{
                System.out.println("Comando no reconocido.");
            }
        } catch (NumeroParametrosExcepcion ex) {
            System.out.println("Número de parámetros incorrecto.");
        }catch (NumberFormatException e){
            System.out.println("Parámetros incorrectos");
        }
    }
}
