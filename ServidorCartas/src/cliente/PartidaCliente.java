package cliente;

import modeloDominio.Mano;

import java.net.Socket;

public class PartidaCliente {
    private Mano mano;
    Socket s;

    public PartidaCliente(Socket s) {
        this.s = s;
    }
}
