package cliente;

import modeloDominio.EstadoPartida;
import modeloDominio.baraja.Carta;
import modeloDominio.baraja.Mano;

import java.util.Map;

public class EjecutosConsolaTonta implements AccionesConsola{

    private Cliente cliente;
    private PartidaCliente partida;

    public boolean enPartida(){
        return this.partida!=null;
    }

    public boolean partidaActualizada(){
        return this.partida.verPartidaActualizada();
    }

    public PartidaCliente getPartida(){return this.partida;}
    public Cliente getCliente(){return this.cliente;}
    public EstadoPartida estadoPartida(){return this.partida.verEstadoPartida();}

    public EjecutosConsolaTonta(Cliente cliente, PartidaCliente partida) {
        this.cliente=cliente;
        this.partida=partida;
    }
    public EjecutosConsolaTonta(Cliente cliente) {
        this.cliente = cliente;
        this.partida=null;
    }
    public String pintarPartida(){
        String salida="";
        if(this.partida.verEstadoPartida()== EstadoPartida.ENCURSO){

            //System.out.print("\n\n\nEstado de la partida: "+this.partida.es);
            Mano mano=this.partida.verMano();
            if(mano!=null)for (Carta carta: mano
            ) {
                salida+=" "+carta;
            }
            Carta descubierta=this.partida.verCartaDescubierta();

            salida+="Descubierta: "+(descubierta==null?"Nada":descubierta);
        }
        return salida;
    }

    public void setPartida(PartidaCliente partida){
        if(partida!=null)System.out.println("Cargando partida");
        else System.out.println("Abandonando partida...");
        this.partida=partida;
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
        String ca="Partida: "+this.partida.getNombrePartida()+"\nJugador: "+this.partida.getNombreJuagador()+
                "\n"+this.listaJugadores()+"\nEstado de la partida: "+this.partida.verEstadoPartida().toString();
        return ca;
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
        if(this.cliente.unirsePartida(partida,jugador))return "Entando,,,\n"+this.estado();
        return "No se ha podido entrar";
    }
    public String crearPartida(String partida){
        if(this.cliente.crearPartida(partida))return "Partida creada";
        return "No se ha podido crear";
    }

    public String salir(){
        if(this.cliente.salir())return "Saliendo...";
        return "Error al salir";
    }

    public String salirPartida(){

        if(this.partida.salir()){
            this.setPartida(null);
            return "saliendo de la partida";
        }
        return "No es posible abandonar la partida";
    }

}
