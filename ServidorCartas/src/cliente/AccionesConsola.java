package cliente;

import modeloDominio.EstadoPartida;

public interface AccionesConsola {
    void pintarPartida();

    void setPartida(PartidaCliente partida);

    boolean enPartida();

    boolean actualizarPartida();

    void salirForzado();

    //PartidaCliente getPartida();
    EstadoPartida estadoPartida();

    void listaJugadores();

    void estado();

    void puntuaciones();

    void listaPartidas();

    void unirsePartida(String partida, String jugador);

    void crearPartida(String partida, String baraja);

    void salir();

    void salirPartida();

    void empezar();

    void ordenar();

    void coger();

    void coger(String opcion);

    void cerrar(int i);

    void echar(int i);

    void mover(int i, int j);

    void verMano();
    void crearIA(String nombre);
    void verTurno();
}
