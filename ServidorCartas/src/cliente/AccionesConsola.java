package cliente;

import modeloDominio.EstadoPartida;

public interface AccionesConsola {
     String pintarPartida();
     void setPartida(PartidaCliente partida);
     boolean enPartida();
     boolean partidaActualizada();

     PartidaCliente getPartida();
     Cliente getCliente();

     EstadoPartida estadoPartida();

     String listaJugadores();

     String estado();

     String puntuaciones();

     String unirsePartida(String partida,String jugador);
     String crearPartida(String partida);

     String salir();

     String salirPartida();
}
