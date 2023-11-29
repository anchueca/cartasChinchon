package cliente;

import cliente.interfaz.ConsolaBonita;

public class EjecutorConsolaBonita extends EjecutorConsola{

    private ConsolaBonita consolaBonita;
    public EjecutorConsolaBonita(PartidaCliente partida,ConsolaBonita consolaBonita) {
        super(partida);
        this.consolaBonita=consolaBonita;
    }

    public EjecutorConsolaBonita(){
        super();
    }

    public void setConsolaBonita(ConsolaBonita consolaBonita){
        this.consolaBonita=consolaBonita;
    }

    public String unirsePartida(String partida,String jugador){
        String salida=super.unirsePartida(partida,jugador);
        if(this.enPartida()){
            this.consolaBonita.setPartida(partida);
            this.consolaBonita.setJugador(jugador);
            this.consolaBonita.limpiarPantalla();
        }
        return salida;
    }

    public String setPartida(PartidaCliente partida){
        super.setPartida(partida);
        if(partida!=null)return "Cargando partida";
        return "Abandonando partida...";

    }

    public String actualizarPartida(){
        if(super.actualizarPartida()!=null)this.pintarPartida();
        return null;
    }


}
