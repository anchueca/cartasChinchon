package cliente;

import modeloDominio.Codigos;

public interface RecibeMensajesI {
    boolean recibirMensaje();
    boolean recibirMensaje(Codigos codigos);
}
