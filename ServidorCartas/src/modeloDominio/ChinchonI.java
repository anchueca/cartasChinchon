package modeloDominio;

import modeloDominio.baraja.Carta;

/*
Acciones del jugador enle chinchon
 */
public interface ChinchonI {
    public Carta cogerCartaCubierta();
    public Carta cogerCartaDecubierta();
    boolean echarCarta(Carta carta);
    boolean cerrar(Carta carta);
    boolean meterCarta(Carta carta);
}
