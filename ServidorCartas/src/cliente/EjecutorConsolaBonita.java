package cliente;

import cliente.interfaz.ConsolaBonita;
import modeloDominio.EstadoPartida;
import modeloDominio.baraja.Carta;
import modeloDominio.baraja.Mano;

import java.util.List;
import java.util.Map;

public class EjecutorConsolaBonita implements AccionesConsola {

    /////Atributos//////
    private PartidaCliente partida;
    private final Actualizador actualizador;
    private ConsolaBonita consolaBonita;
    //////COntructores/////////
    public EjecutorConsolaBonita(PartidaCliente partida,ConsolaBonita consolaBonita) {
        this.actualizador=new Actualizador(this);
        this.consolaBonita=consolaBonita;
        this.partida=partida;
    }
    public EjecutorConsolaBonita(ConsolaBonita consolaBonita) {
        this(null,consolaBonita);
    }


    ///////GEtters y setters//////////
    public boolean enPartida(){
        return this.partida!=null;
    }
    //public PartidaCliente getPartida(){return this.partida;}
    public EstadoPartida estadoPartida(){return this.partida.verEstadoPartida();}
    public void setConsolaBonita(ConsolaBonita consolaBonita){
        this.consolaBonita=consolaBonita;
    }

    ////////Accioens de gestión///////////////
    public void unirsePartida(String partida,String jugador){
        if(Cliente.server.entrarPartida(partida,jugador)){
            this.partida=new PartidaCliente(partida,jugador);
            this.setPartida(this.partida);
            this.consolaBonita.setPartida(partida);
            this.consolaBonita.setJugador(jugador);
            this.consolaBonita.limpiarPantalla();
            this.consolaBonita.meterSalida("Entando...");
            this.estado();
        }else this.consolaBonita.meterSalida("No se ha podido entrar");
    }
    public void crearPartida(String partida){
        if(Cliente.server.crearPartida(partida))this.consolaBonita.meterSalida("Partida creada");
        else this.consolaBonita.meterSalida("No se ha podido crear");
    }
    public void setPartida(PartidaCliente partida){
        String cadena="";
        if(partida==null){//salgo
            this.actualizador.pausar();
            this.partida=null;
            this.consolaBonita.setPartida("");
            this.consolaBonita.setJugador("");
            cadena="Abandonando partida...";
        }else {//entro
            this.partida = partida;
            if (!this.actualizador.isAlive()) this.actualizador.start();
            else this.actualizador.reanudar();
            this.consolaBonita.setPartida("");
            this.consolaBonita.setJugador("");
            cadena="Cargando partida";
        }
        this.consolaBonita.limpiarPantalla();
        this.consolaBonita.meterSalida(cadena);
    }
    public void empezar() {
        if(this.partida.empezarPartida())this.consolaBonita.meterSalida("Partida iniciada correctamente");
        else this.consolaBonita.meterSalida("No se ha podido iniciar");
    }

    ///////////CONSULTA DE DATOS////////////
    public void listaJugadores(){
        String cadena="Juagdores: ";
        for (String ca: this.partida.listaJugadores()
        ) {
            cadena+=ca+" ";
        }
        this.consolaBonita.meterSalida(cadena);
    }

    public void estado(){
        this.partida.getNombrePartida();
        this.partida.getNombreJuagador();
        this.listaJugadores();
        this.partida.verEstadoPartida().toString();
    }
    public void listaPartidas(){
        String n = "Lista partidas:\n";
        List<String> lista = Cliente.server.getPartidas();
        for (int i = 0, j = lista.size(); i < j; i++) {
            n += i + ".- " + lista.get(i) + "\n";
        }
        this.consolaBonita.meterSalida( n);
    }

    public void pintarPartida() {
        this.consolaBonita.limpiarPantalla();
        String salida="";
        if(this.partida.verEstadoPartida()== EstadoPartida.ENCURSO){
            this.verMano();//pinto la mano
            Carta descubierta=this.partida.verCartaDescubierta();
            salida+="\nDescubierta: "+(descubierta==null?"Nada":descubierta);
        }
        else salida="La partida no está en curso";
        this.consolaBonita.meterSalida(salida);
    }
    public void puntuaciones(){
        Map<String,Integer> mapa=this.partida.verPuntuaciones();
        String ca="Puntuaciones:";
        for (String nombre:mapa.keySet()
        ) {
            ca+="\n"+nombre+": "+mapa.get(nombre);
        }
        this.consolaBonita.meterSalida(ca);
    }
    //////ACCIOENS De JUEGO
    public void ordenar() {
        this.partida.ordenar();
        this.pintarPartida();
    }

    public void salir(){
        this.consolaBonita.dispose();
        this.consolaBonita.meterSalida("Saliendo...");
    }
    public void echar(int i) {//por refinar
        Mano mano = this.partida.verMano();
        if (this.partida.echarCarta(mano.verCarta(i))) {
            mano.tomarCarta(i);
            this.consolaBonita.meterSalida("OK");
        } else this.consolaBonita.meterSalida("Jugada no válida");

    }
    public void salirPartida(){
        if(this.partida.salir()){
            this.setPartida(null);
            this.consolaBonita.meterSalida("saliendo de la partida");
        }else this.consolaBonita.meterSalida("No es posible abandonar la partida");
    }
    public void mover(int i, int j) {
        if(this.partida.moverMano(i,j))this.consolaBonita.meterSalida("Movimiento efectuado");
        else this.consolaBonita.meterSalida("Movimiento incorrecto");
    }

    public void cerrar(int i) {

    }
    public void salirForzado(){
        this.salirPartida();
        if(this.partida!=null){
            this.setPartida(null);
            this.consolaBonita.meterSalida("Salida forzada");
        }
    }
    public void verMano() {
        String salida="";
        if(this.partida.verEstadoPartida()== EstadoPartida.ENCURSO){
            salida+="Cartas: ";
            Mano mano=this.partida.verMano();
            if(mano!=null)for (Carta carta: mano
            ) {
                salida+=" ("+carta+")";
            }
            Carta descubierta=this.partida.verCartaDescubierta();
        }else salida="La partida no está en curso";
        this.consolaBonita.meterSalida(salida);
    }
    /*
Comprueba actualizaciones y si procede hace las acciones convenientes
 */
    public boolean actualizarPartida(){
        if(this.partida.verPartidaActualizada()){
            this.pintarPartida();
            return true;
        }
        return false;
    }
    public void coger(){
        this.coger("0");
    }
    public void coger(String opcion){
        boolean jugadaValida = opcion.compareTo("descubierta") == 0
                ? this.cogerCartaDecubierta()
                : this.cogerCartaCubierta();

        if (!jugadaValida) this.consolaBonita.meterSalida ("Jugada no válida");
        else this.consolaBonita.meterSalida("Jugada efectuada");
    }
    public boolean cogerCartaDecubierta(){
        return true;
    }
    public boolean cogerCartaCubierta(){
        return true;
    }
}
