package cliente;

import modeloDominio.Carta;
/*
Acciones del cliente propias del chinchón
 */
public interface PresentacionChinchon extends PresentacionI, ChinchonI {
    Carta consultarCartaDescubierta();
    boolean consultarCerrado();


}
