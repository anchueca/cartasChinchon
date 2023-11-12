package cliente;

import modeloDominio.Carta;
import modeloDominio.Mano;

public interface PresentacionChinchon extends PresentacionI, ChinchonI {
    boolean consultarTurno();
    Mano consultarMano();
    Carta consultarCartaDescubierta();
    boolean consultarCerrado();
    boolean consultarPartidaActualizada();
}
