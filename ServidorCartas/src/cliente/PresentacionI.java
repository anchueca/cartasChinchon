package cliente;

import modeloDominio.Mano;
/*
Acciones generales de presentación
 */
public interface PresentacionI {
    boolean consultarPartidaActualizada();
    boolean consultarTurno();

    Mano consultarMano();
    boolean partidaEnCurso();
}
