package cliente;

import modeloDominio.EstadoPartida;
import modeloDominio.baraja.Carta;
import modeloDominio.baraja.Mano;

import java.util.List;
import java.util.Map;
import java.util.Scanner;

/*
Interfaz de usuario del chinchón por consola
 */
public class procesadorComandos extends Thread{
    private AccionesConsola acciones;
    public procesadorComandos(Cliente cliente, PartidaCliente partida) {
        this.acciones=new EjecutosConsolaTonta(cliente,partida);

    }
    public procesadorComandos(Cliente cliente) {
        this.acciones=new EjecutosConsolaTonta(cliente);
    }
    /*
    Inicio de la partida que contiene el bucle de juego
     */
    public void run(){
        Scanner in=new Scanner(System.in);
        //this.interfaz
        System.out.println("Juego chinchon");
        System.out.println("escribe ayuda para ver los comandos");

        //ExecutorService executor= Executors.newSingleThreadExecutor();
        //Future<String> entrada= executor.submit(() -> {return in.nextLine();});
        //Bucle de juego
        while (!Thread.currentThread().isInterrupted()){
            //Si ha habido cambios actualizo si estoy en partida
            if(this.acciones.enPartida() && this.acciones.partidaActualizada()){
                if(this.acciones.estadoPartida()== EstadoPartida.ENCURSO)System.out.println(this.pintarPartida());
            }
            //Compruebo entrada del usuario
            /*if(entrada.isDone()){
                try {
                    System.out.println(this.procesarInstrccion(entrada.get()));
                    if(this.partida!=null)System.out.println(this.pintarPartida());
                    entrada= executor.submit(() -> {return in.nextLine();});
                } catch (InterruptedException | ExecutionException e) {
                    System.out.println("Error en captura");
                }
            }*/
            try {
                Thread.sleep(10);
            } catch (InterruptedException e) {

            }
        }
        in.close();
        System.out.println("Ceeradno programa....");
    }
/*
Se encarga de dibujar en consola la partida
 */
private String pintarPartida(){
    String salida="";
    if(this.acciones.estadoPartida()==EstadoPartida.ENCURSO){

        //System.out.print("\n\n\nEstado de la partida: "+this.partida.es);
        Mano mano=this.acciones.getPartida().verMano();
        if(mano!=null)for (Carta carta: mano
        ) {
            salida+=" "+carta;
        }
        Carta descubierta=this.acciones.getPartida().verCartaDescubierta();

        salida+="Descubierta: "+(descubierta==null?"Nada":descubierta);
    }
    return salida;
}
    /*
    Proesa los comandos
     */
    public String procesarInstrccion(String instruccion) {
        String[] palabras = instruccion.split("\\s+");
        try {
            if(!this.acciones.enPartida()){
                switch (palabras[0]) {
                    case "entrar": {
                        if (palabras.length != 3) throw new NumeroParametrosExcepcion();
                        return this.unirsePartida(palabras[1], palabras[2]);
                    }
                    case "crear": {
                        if (palabras.length != 2) throw new NumeroParametrosExcepcion();
                        return this.crearPartida(palabras[1]);
                    }
                    case "salir": {
                        return this.salir();
                    }
                    case "ayuda": {
                        return "Mostrando ayuda " + (this.acciones.enPartida() ? "inicio" : "juego");
                    }
                    case "partidas": {
                        String n = "Lista partidas:\n";
                        List<String> lista = this.acciones.getCliente().listaPartidas();
                        for (int i = 0, j = lista.size(); i < j; i++) {
                            n += i + ".- " + lista.get(i) + "\n";
                        }
                        return n;
                    }
                }
            }else {
                switch (palabras[0]) {
                    case "entrar": {
                        if (palabras.length != 3) throw new NumeroParametrosExcepcion();
                        return this.unirsePartida(palabras[1], palabras[2]);
                    }
                    case "crear": {
                        if (palabras.length != 2) throw new NumeroParametrosExcepcion();
                        return this.crearPartida(palabras[1]);
                    }
                    case "salir": {
                        return this.salirPartida();

                    }
                    case "ayuda": {
                        return "Mostrando ayuda " + (this.acciones.enPartida() ? "inicio" : "juego");
                    }
                    case "partidas": {
                        String n = "Lista partidas:\n";
                        List<String> lista = this.acciones.getCliente().listaPartidas();
                        for (int i = 0, j = lista.size(); i < j; i++) {
                            n += i + ".- " + lista.get(i) + "\n";
                        }
                        return n;
                    }
                    case "mover": {
                        if (palabras.length != 3) throw new NumeroParametrosExcepcion();
                        this.acciones.getPartida().moverMano(Integer.parseInt(palabras[1]), Integer.parseInt(palabras[2]));
                        break;
                    }
                    case "echar": {
                        if (palabras.length != 2) throw new NumeroParametrosExcepcion();
                        int i = Integer.parseInt(palabras[1]);
                        Mano mano = this.acciones.getPartida().verMano();
                        if (this.acciones.getPartida().echarCarta(mano.verCarta(i))) {
                            mano.tomarCarta(i);
                        } else return "Jugada no válida";
                        break;
                    }
                    case "cerrar": {
                        if (palabras.length != 2) throw new NumeroParametrosExcepcion();
                        int i = Integer.parseInt(palabras[1]);
                        Mano mano = this.acciones.getPartida().verMano();
                        if (this.acciones.getPartida().cerrar(mano.verCarta(i))) {
                            mano.tomarCarta(i);
                        } else return ("Jugada no válida");
                        break;
                    }
                    case "coger": {
                        if (palabras.length > 2) throw new NumeroParametrosExcepcion();
                        boolean jugadaValida;
                        if (palabras.length == 1) {
                            jugadaValida = this.acciones.getPartida().cogerCartaCubierta();
                        } else {
                            jugadaValida = palabras[1].compareTo("descubierta") == 0
                                    ? this.acciones.getPartida().cogerCartaDecubierta()
                                    : this.acciones.getPartida().cogerCartaCubierta();
                        }
                        if (!jugadaValida) return ("Jugada no válida");
                        break;
                    }
                    case "ordenar": {
                        this.acciones.getPartida().verMano().ordenar();
                        break;
                    }
                    case "empezar": {
                        this.acciones.getPartida().empezarPartida();
                        break;
                    }
                    case "jugadores": {
                        return this.listaJugadores();
                    }
                    case "estado": {
                        return this.estado();
                    }
                    case "puntuaciones": {
                        return this.puntuaciones();
                    }
                }
                return ("Comando no reconocido.");
            }
        } catch (NumeroParametrosExcepcion ex) {
            return ("Número de parámetros incorrecto.");
        } catch (NumberFormatException e) {
            return ("Parámetros incorrectos");
        }
        return "OK";
    }

    public void setPartida(PartidaCliente partida){
        if(partida!=null)System.out.println("Cargando partida");
        else System.out.println("Abandonando partida...");
        this.acciones.setPartida(partida);
    }

    private String listaJugadores(){
        String cadena="Juagdores: ";
        for (String ca: this.acciones.getPartida().listaJugadores()
             ) {
            cadena+=ca+" ";
        }
        return cadena;
    }

    private String estado(){
        String ca="Partida: "+this.acciones.getPartida().getNombrePartida()+"\nJugador: "+this.acciones.getPartida().getNombreJuagador()+
                "\n"+this.listaJugadores()+"\nEstado de la partida: "+this.acciones.getPartida().verEstadoPartida().toString();
        return ca;
    }

    private String puntuaciones(){
        Map<String,Integer> mapa=this.acciones.getPartida().verPuntuaciones();
        String ca="Puntuaciones:";
        for (String nombre:mapa.keySet()
             ) {
            ca+="\n"+nombre+": "+mapa.get(nombre);
        }
        return ca;
    }

    private String unirsePartida(String partida,String jugador){
        if(this.acciones.getCliente().unirsePartida(partida,jugador))return "Entando,,,\n"+this.estado();
        return "No se ha podido entrar";
    }
    private String crearPartida(String partida){
        if(this.acciones.getCliente().crearPartida(partida))return "Partida creada";
        return "No se ha podido crear";
    }

    private String salir(){
        if(this.acciones.getCliente().salir())return "Saliendo...";
        return "Error al salir";
    }

    private String salirPartida(){

        if(this.acciones.getPartida().salir()){
            this.setPartida(null);
            return "saliendo de la partida";
        }
        return "No es posible abandonar la partida";
    }

}
