package modeloDominio;

import modeloDominio.baraja.Carta;

/*
Acciones del jugador enle chinchon
 */
public interface AccionesChinchonI {
    public boolean cogerCartaCubierta();
    public boolean cogerCartaDecubierta();
    boolean echarCarta(Carta carta);
    boolean cerrar(Carta carta);
    boolean meterCarta(Carta carta);
    boolean moverMano(int i,int j);
}
