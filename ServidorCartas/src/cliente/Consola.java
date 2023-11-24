package cliente;

import modeloDominio.baraja.Carta;
import modeloDominio.baraja.Mano;
import servidor.Partida;

import java.util.Scanner;

import static java.lang.Integer.parseInt;
/*
Interfaz de usuario del chinchón por consola
 */
public class Consola extends Thread{
    private Cliente cliente;
    private PartidaCliente partida;
    public Consola(Cliente cliente,PartidaCliente partida) {
        this(cliente);
        this.partida=partida;
    }
    public Consola(Cliente cliente) {
        this.cliente = cliente;
        this.partida=null;
    }
    /*
    Inicio de la partida que contiene el bucle de juego
     */
    public void run(){
        Scanner in=new Scanner(System.in);
        //this.interfaz
        System.out.println("Juego chinchon");
        System.out.println("escribe ayuda para ver los comandos");
        //Bucle de juego
        while (!this.isInterrupted()){
            //Si ha habido cambios actualizo si estoy en partida
            if(this.partida!=null && this.partida.verPartidaActualizada()){
                this.pintarPartida();
            }
            //Compruebo entrada del usuario
            if(in.hasNext()){
                System.out.println(this.procesarInstrccion(in.nextLine()));
                //this.pintarPartida();
            }
        }
        System.out.println("Ceeradno programa....");
    }
/*
Se encarga de dibujar en consola la partida
 */
    private void pintarPartida(){
        System.out.print("\n\n\nEstado de la partida: "+this.cliente.toString());
        for (Carta carta: this.partida.verMano()
             ) {
            System.out.print(" "+carta);
        }
        System.out.println("Descubierta: "+this.partida.verCartaDescubierta());
    }
    /*
    Proesa los comandos
     */
    public String procesarInstrccion(String instruccion) {
        String[] palabras = instruccion.split("\\s+");
        try {
            if(this.partida==null){
                if (palabras[0].equals("crear")) {
                    if (palabras.length == 2) {
                        this.cliente.unirsePartida(palabras[1]);
                    } else throw new NumeroParametrosExcepcion();
                } else if (palabras[0].equals("entrar")) {
                    if (palabras.length == 2) {
                        this.cliente.crearPartida(palabras[1]);
                    } else throw new NumeroParametrosExcepcion();
                } else if (palabras[0].equals("salir")) {//abandonar programa
                    return this.cliente.salir();
                }else if (palabras[0].equals("ayuda")) {
                    return "Mostrando ayuda inicio";
                }else {
                    return ("Comando no reconocido.");
                }
            }
            else {
                if (palabras[0].equals("mover")) {
                    if (palabras.length == 3) {
                        this.partida.moverMano(parseInt(palabras[1]), parseInt(palabras[2]));
                    } else throw new NumeroParametrosExcepcion();
                } else if (palabras[0].equals("echar")) {
                    if (palabras.length == 2) {
                        int i = parseInt(palabras[1]);
                        Mano mano = this.partida.verMano();
                        if (this.partida.echarCarta(mano.verCarta(i))) mano.tomarCarta(i);
                        else return "Jugada no válida";
                    } else throw new NumeroParametrosExcepcion();
                } else if (palabras[0].equals("cerrar")) {
                    if (palabras.length == 2) {
                        int i = parseInt(palabras[1]);
                        Mano mano = this.partida.verMano();
                        if (this.partida.cerrar(mano.verCarta(i))) mano.tomarCarta(i);
                        else return ("Jugada no válida");
                    } else throw new NumeroParametrosExcepcion();
                } else if (palabras[0].equals("coger")) {
                    boolean jugadaValida = false;
                    if (palabras.length == 1) jugadaValida = this.partida.cogerCartaCubierta();
                    if (palabras.length == 2) {
                        if (palabras[1].compareTo("descubierta") == 0) {
                            jugadaValida = this.partida.cogerCartaDecubierta();

                        } else if (palabras[1].compareTo("cubierta") == 0) {
                            jugadaValida = this.partida.cogerCartaCubierta();
                        }
                    } else throw new NumeroParametrosExcepcion();
                    if (!jugadaValida) return ("Jugada no válida");
                } else if (palabras[0].equals("ordenar")) {
                    this.partida.verMano().ordenar();
                } else if (palabras[0].equals("salir")) {//abandonar partida
                    return "Abandonando partida...";
                } else if (palabras[0].equals("empezar")) {
                    this.partida.empezarPartida();
                }else if (palabras[0].equals("ayuda")) {
                    return "Mostrando ayuda juego";
                } else {
                    return ("Comando no reconocido.");
                }
            }
        } catch (NumeroParametrosExcepcion ex) {
            return("Número de parámetros incorrecto.");
        }catch (NumberFormatException e){
            return("Parámetros incorrectos");
        }
        return "OK";
    }

    public void setPartida(PartidaCliente partida){
        System.out.println("Cargando partida");
        this.partida=partida;
    }

}
