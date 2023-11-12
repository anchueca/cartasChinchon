package cliente;

import modeloDominio.Carta;

public interface ChinchonI {
    public Carta cogerCartaCubierta();
    public Carta cogerCartaDescubierta();
    public boolean echarCarta(Carta carta);
    public boolean cerrar(Carta carta);
}
