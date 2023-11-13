package cliente;

import modeloDominio.Carta;
/*
Accioens del jugador en el chinchón
 */
public interface ChinchonI {
    Carta cogerCartaCubierta();
    Carta cogerCartaDescubierta();
    boolean echarCarta(Carta carta);
    boolean cerrar(Carta carta);
}
