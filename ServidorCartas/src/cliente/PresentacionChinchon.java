package cliente;

import modeloDominio.baraja.Carta;
import modeloDominio.ChinchonI;
import servidor.usuarios.Jugador;

import java.util.Map;

/*
Acciones del cliente propias del chinchón
 */
public interface PresentacionChinchon extends PresentacionI, ChinchonI {
    Carta verCartaDescubierta();
    boolean verCerrado();
    Map<Jugador,Integer> verPuntuaciones();
}
