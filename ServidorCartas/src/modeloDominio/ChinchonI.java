package modeloDominio;

public interface ChinchonI {
    public Carta cogerCartaCubierta();
    public Carta cogerCartaDecubierta();
    boolean echarCarta(Carta carta);
    boolean cerrtar(Carta carta);
    boolean meterCarta(Carta carta);

}
