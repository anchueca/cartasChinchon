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
}
