package cliente;

import modeloDominio.EstadoPartida;

public interface AccionesConsola {
     String pintarPartida();
     String setPartida(PartidaCliente partida);
     boolean enPartida();
     String actualizarPartida();
     String salirForzado();
     PartidaCliente getPartida();
     EstadoPartida estadoPartida();
     String listaJugadores();
     String estado();
     String puntuaciones();
     String listaPartidas();
     String unirsePartida(String partida,String jugador);
     String crearPartida(String partida);
     String salir();
     String salirPartida();
     String empezar();
     String ordenar();
     String coger();
     String coger(String opcion);
     String cerrar(int i);
     String echar(int i);
     String mover(int i,int j);
     String verMano();
}
