package servidor.usuarios;

import modeloDominio.baraja.Carta;
import modeloDominio.baraja.Mano;

import java.util.Map;

public interface JugadorI {
    Mano verMano();
    void darCarta(Carta carta);
}
