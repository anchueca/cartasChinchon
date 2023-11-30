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
        String cadena=super.setPartida(partida);
        if(partida==null){
            this.consolaBonita.setPartida("");
            this.consolaBonita.setJugador("");
        }else{
            this.consolaBonita.setPartida(partida.getNombrePartida());
            this.consolaBonita.setJugador(partida.getNombreJuagador());
        }
        this.consolaBonita.limpiarPantalla();
        return cadena;
    }

    @Override
    public String pintarPartida() {
        this.consolaBonita.limpiarPantalla();
        return super.pintarPartida();
    }
}
