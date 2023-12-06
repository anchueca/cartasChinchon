package cliente;

import cliente.interfaz.ConsolaBonita;
import modeloDominio.Codigos;
import modeloDominio.EstadoPartida;
import modeloDominio.ProcesadorMensajes;
import modeloDominio.baraja.Carta;
import modeloDominio.baraja.Mano;

import java.io.IOException;
import java.net.Socket;
import java.util.List;
import java.util.Map;

/*
Implementación de las acciones asociadas a la presentación de consolaBonita
 */

public class EjecutorConsolaBonita implements AccionesConsola {

    private final Actualizador actualizador;
    private final Socket s;
    private final ConsolaBonita consolaBonita;
    /////Atributos//////
    private PartidaCliente partida;

    //////COntructores/////////
    public EjecutorConsolaBonita(PartidaCliente partida, ConsolaBonita consolaBonita) throws IOException {
        this.s = new Socket("localhost", 55555);
        this.actualizador = new Actualizador(this);
        this.consolaBonita = consolaBonita;
        this.partida = partida;
    }

    public EjecutorConsolaBonita(ConsolaBonita consolaBonita) throws IOException {
        this(null, consolaBonita);
    }

    ///////GEtters y setters//////////
    public boolean enPartida() {
        return this.partida != null;
    }

    public EstadoPartida estadoPartida() {
        return this.partida.verEstadoPartida();
    }

    ////////Accioens de gestión (fuera de partida)///////////////
    public void unirsePartida(String partida, String jugador) {
        synchronized (this.s) {
            ProcesadorMensajes.getProcesadorMensajes().enviarObjeto("entrar " + partida + " " + jugador, this.s);
            if (ProcesadorMensajes.getProcesadorMensajes().recibirCodigo(this.s) == Codigos.BIEN) {
                this.partida = new PartidaCliente(partida, jugador, this.s);
                this.setPartida(this.partida);
                this.consolaBonita.setPartida(partida);
                this.consolaBonita.setJugador(jugador);
                this.consolaBonita.limpiarPantalla();
                this.consolaBonita.meterSalida("Entando...");
                this.estado();
            } else this.consolaBonita.meterSalida("No se ha podido entrar");
        }

    }

    public void crearPartida(String partida, String baraja) {
        synchronized (this.s) {
            if (baraja.isEmpty()) baraja = "NORMAL";
            ProcesadorMensajes.getProcesadorMensajes().enviarObjeto("crear " + partida + " " + baraja, this.s);
            if (ProcesadorMensajes.getProcesadorMensajes().recibirCodigo(this.s) == Codigos.BIEN)
                this.consolaBonita.meterSalida("Partida creada");
            else this.consolaBonita.meterSalida("No se ha podido crear");
        }

    }

    public void setPartida(PartidaCliente partida) {
        String cadena;
        if (partida == null) {//salgo
            this.actualizador.pausar();
            this.partida = null;
            this.consolaBonita.setPartida("");
            this.consolaBonita.setJugador("");
            cadena = "Abandonando partida...";
        } else {//entro
            this.partida = partida;
            if (!this.actualizador.isAlive()) this.actualizador.start();
            else this.actualizador.reanudar();
            this.consolaBonita.setPartida("");
            this.consolaBonita.setJugador("");
            cadena = "Cargando partida";
        }
        this.consolaBonita.limpiarPantalla();
        this.consolaBonita.meterSalida(cadena);
    }

    public void listaPartidas() {
        synchronized (this.s) {
            ProcesadorMensajes.getProcesadorMensajes().enviarObjeto("partidas", this.s);
            String n = "Lista partidas:\n";
            if (ProcesadorMensajes.getProcesadorMensajes().recibirCodigo(this.s) == Codigos.BIEN) {
                List<String> lista = (List<String>) ProcesadorMensajes.getProcesadorMensajes().recibirObjeto(this.s);
                for (int i = 0, j = lista.size(); i < j; i++) {
                    n += i + ".- " + lista.get(i) + "\n";
                }
            }

            this.consolaBonita.meterSalida(n);
        }
    }

    public void salir() {
        this.consolaBonita.dispose();
        this.actualizador.interrupt();
        this.consolaBonita.meterSalida("Saliendo...");
        try {
            this.s.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    ///////////CONSULTA DE DATOS (dentro de la partida)////////////
    public void listaJugadores() {
        String cadena = "Juagdores: ";
        for (String ca : this.partida.listaJugadores()
        ) {
            cadena += ca + " ";
        }
        this.consolaBonita.meterSalida(cadena);

    }

    public void estado() {
        this.consolaBonita.meterSalida("Nombre partida: "+this.partida.getNombrePartida()+" "
                +this.partida.verEstadoPartida().toString() +"\nJugador:"+this.partida.getNombreJuagador());
        this.listaJugadores();
    }


    public void pintarPartida() {
        this.consolaBonita.limpiarPantalla();
        String salida = "";
        if (this.partida.verEstadoPartida() == EstadoPartida.ENCURSO) {
            this.verMano();//pinto la mano
            Carta descubierta = this.partida.verCartaDescubierta();
            salida += "\nDescubierta: " + (descubierta == null ? "Nada" : descubierta);
        } else salida = "La partida no está en curso";
        this.consolaBonita.meterSalida(salida);

    }

    public void verMano() {
        String salida = "";
        if (this.partida.verEstadoPartida() == EstadoPartida.ENCURSO) {
            salida += "Cartas: ";
            Mano mano = this.partida.verMano();
            if (mano != null) for (int i = 0, j = mano.numCartas(); i < j; i++) {
                salida += "\n" + i + ".-(" + mano.verCarta(i) + ")";
            }
            salida += "\n\nCasadas:";
            for (Byte bite : Mano.cartasCasadas(mano)
            ) {
                salida += "\n" + bite.toString();
            }
        } else salida = "La partida no está en curso";
        this.consolaBonita.meterSalida(salida);
    }

    public void puntuaciones() {
        Map<String, Integer> mapa = this.partida.verPuntuaciones();
        String ca = "Puntuaciones:";
        for (String nombre : mapa.keySet()
        ) {
            ca += "\n" + nombre + ": " + mapa.get(nombre);
        }
        this.consolaBonita.meterSalida(ca);
    }

    public void verTurno(){
        String turno=this.partida.verTurno();
        if (this.partida.verEstadoPartida()!=EstadoPartida.ENCURSO || turno==null) {
            this.consolaBonita.meterSalida("No es posible consultar el turno");
        } else this.consolaBonita.meterSalida("Turno: r"+(turno.compareTo(this.partida.getNombreJuagador())==0?"Te toca":turno));
    }

    //////////ACCIOENES DE JUEGO/////////////

    public void empezar() {
        if (this.partida.empezarPartida()) {
            this.consolaBonita.meterSalida("Partida iniciada correctamente");
            this.estado();
        } else this.consolaBonita.meterSalida("No se ha podido iniciar");
    }

    public void ordenar() {
        if (this.partida.ordenar()) this.pintarPartida();
        else this.consolaBonita.meterSalida("No se ha podido ordenar");
    }

    public void echar(int i) {//por refinar
        Mano mano = this.partida.verMano();
        if (this.partida.echarCarta(i)==Codigos.BIEN) {
            mano.tomarCarta(i);
            this.consolaBonita.meterSalida("OK");
        } else this.consolaBonita.meterSalida("Jugada no válida");

    }

    public void salirPartida() {
        if (this.partida.salir()) {
            this.setPartida(null);
            this.consolaBonita.meterSalida("saliendo de la partida");
        } else this.consolaBonita.meterSalida("No es posible abandonar la partida");

    }

    public void mover(int i, int j) {
        if (this.partida.moverMano(i, j)) this.consolaBonita.meterSalida("Movimiento efectuado");
        else this.consolaBonita.meterSalida("Movimiento incorrecto");

    }

    public void cerrar(int i) {

    }


    public void coger() {
        this.coger("0");
    }

    public void coger(String opcion) {
        //Compruebo cuál de las opciones es
        Carta carta;
        if(opcion.compareTo("descubierta") == 0)carta=this.cogerCartaDecubierta();
        else if(opcion.compareTo("cubierta") == 0)carta=this.cogerCartaCubierta();
        else {
            this.consolaBonita.meterSalida("Opción desconocida");
            return;
        }
        if(carta==null)this.consolaBonita.meterSalida("Jugada inválida");
        else this.consolaBonita.meterSalida("Carta obtenida: "+carta);
    }

    public Carta cogerCartaDecubierta() {
        return this.partida.cogerCartaDecubierta();
    }

    public Carta cogerCartaCubierta() {
        return this.partida.cogerCartaCubierta();
    }

    /*
Comprueba actualizaciones y si procede hace las acciones convenientes. La idea es que sea llamado por el actualizador
 */
    public boolean actualizarPartida() {
        String cadena;
        if ((cadena=this.partida.actualizarPartida())!=null) {
            this.consolaBonita.meterSalida(cadena);
            return true;
        }
        return false;

    }

    public void crearIA(String nombre){
        if (this.partida.crearIA(nombre)) this.consolaBonita.meterSalida("IA creada");
        else this.consolaBonita.meterSalida("No se ha podido crear");
    }

    /////////////OTROS///////////////
    public void salirForzado() {
        this.salirPartida();
        if (this.partida != null) {
            this.setPartida(null);
            this.consolaBonita.meterSalida("Salida forzada");
        }

    }

}
