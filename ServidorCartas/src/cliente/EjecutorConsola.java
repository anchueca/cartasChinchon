package cliente;

import modeloDominio.EstadoPartida;
import modeloDominio.baraja.Carta;
import modeloDominio.baraja.Mano;

import java.util.List;
import java.util.Map;

public class EjecutorConsola implements AccionesConsola{

    private PartidaCliente partida;
    private Actualizador actualizador;
    public boolean enPartida(){
        return this.partida!=null;
    }

    public PartidaCliente getPartida(){return this.partida;}
    public EstadoPartida estadoPartida(){return this.partida.verEstadoPartida();}

    public  EjecutorConsola(PartidaCliente partida) {
        this();
        this.partida=partida;

    }
    public EjecutorConsola(){
        this.actualizador=new Actualizador(this);
    }
    public  String pintarPartida(){
        String salida="";
        if(this.partida.verEstadoPartida()== EstadoPartida.ENCURSO){
            Mano mano=this.partida.verMano();
            if(mano!=null)for (Carta carta: mano
            ) {
                salida+=" ("+carta+")";
            }
            Carta descubierta=this.partida.verCartaDescubierta();

            salida+="\nDescubierta: "+(descubierta==null?"Nada":descubierta);
        }
        else salida="La partida no está en curso";
        return salida;
    }
    public String setPartida(PartidaCliente partida){
        synchronized (this.actualizador){
            if(partida==null){
                try {
                    this.actualizador.wait();
                    this.partida=null;
                    return "Abandonando partida...";
                } catch (InterruptedException e) {
                    return "Actualizador no funciona";
                }
            }
            this.partida=partida;
            if(!this.actualizador.isAlive())this.actualizador.start();
            else this.actualizador.notify();
            return "Cargando partida";
        }
    }

    public String listaJugadores(){
        String cadena="Juagdores: ";
        for (String ca: this.partida.listaJugadores()
        ) {
            cadena+=ca+" ";
        }
        return cadena;
    }
    public String estado(){
        return "Partida: "+this.partida.getNombrePartida()+"\nJugador: "+this.partida.getNombreJuagador()+
                "\n"+this.listaJugadores()+"\nEstado de la partida: "+this.partida.verEstadoPartida().toString();
    }

    public String puntuaciones(){
        Map<String,Integer> mapa=this.partida.verPuntuaciones();
        String ca="Puntuaciones:";
        for (String nombre:mapa.keySet()
        ) {
            ca+="\n"+nombre+": "+mapa.get(nombre);
        }
        return ca;
    }

    public String unirsePartida(String partida,String jugador){
        if(Cliente.server.entrarPartida(partida,jugador)){
            this.partida=new PartidaCliente(partida,jugador);
            this.setPartida(this.partida);
            return "Entando,,,\n"+this.estado();
        }
        return "No se ha podido entrar";
    }
    public String crearPartida(String partida){
        if(Cliente.server.crearPartida(partida))return "Partida creada";
        return "No se ha podido crear";
    }

    public String salir(){
        //return "Saliendo...";
        return "Error al salir";
    }

    public String salirPartida(){

        if(this.partida.salir()){
            this.setPartida(null);
            return "saliendo de la partida";
        }
        return "No es posible abandonar la partida";
    }

    public String empezar() {
        if(this.partida.empezarPartida())return "Partida iniciada correctamente";
        return "No se ha podido iniciar";
    }

    public String ordenar() {
        this.partida.ordenar();
        return this.pintarPartida();
    }

    public String coger(boolean cubierta) {
        return null;
    }

    public String cerrar(int i) {

        return null;
    }

    public String echar(int i) {
        return null;
    }

    public String mover(int i, int j) {
        return null;
    }

    public String verMano() {
        return null;
    }

    public String salirForzado(){
        String s=this.salirPartida();
        if(this.partida!=null){
            this.setPartida(null);
            return "Salida forzada";
        }
        return s;
    }

    public String listaPartidas(){
        String n = "Lista partidas:\n";
        List<String> lista = Cliente.server.getPartidas();
        for (int i = 0, j = lista.size(); i < j; i++) {
            n += i + ".- " + lista.get(i) + "\n";
        }
        return n;
    }

    public String actualizarPartida(){
        if(!this.partida.verPartidaActualizada())return this.pintarPartida();
        return null;
    }
    public String coger(){
        return this.coger("0");
    }
    public String coger(String opcion){
        boolean jugadaValida = opcion.compareTo("descubierta") == 0
                ? this.cogerCartaDecubierta()
                : this.cogerCartaCubierta();

        if (!jugadaValida) return ("Jugada no válida");
        return "Jugada efectuada";
    }
    public boolean cogerCartaDecubierta(){
        return true;
    }
    public boolean cogerCartaCubierta(){
        return true;
    }

}
