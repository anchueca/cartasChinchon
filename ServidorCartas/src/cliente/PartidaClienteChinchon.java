package cliente;

import modeloDominio.Carta;
import modeloDominio.Mano;
import modeloDominio.ProcesadorMensajes;
import org.w3c.dom.Document;

import java.io.IOException;
import java.net.Socket;

/*
Implementaciṕn de la partida propia del chinchón
 */
public class PartidaClienteChinchon extends PartidaCliente implements PresentacionChinchon{
    public PartidaClienteChinchon(Socket s) {
        super(s);
    }
    /*
    Recibir y procesar mensajes del servidor
     */
    public void atenderServidor() throws IOException {
        if(!ProcesadorMensajes.enEsperaXML(this.s))return;
        Document xml=ProcesadorMensajes.recibirXml(this.s);
    }

    @Override
    public void bienvenidaPartida() {

    }

    @Override
    public Carta cogerCartaCubierta() {
        return null;
    }

    @Override
    public Carta cogerCartaDescubierta() {
        return null;
    }

    @Override
    public boolean echarCarta(Carta carta) {
        return false;
    }

    @Override
    public boolean cerrar(Carta carta) {
        return false;
    }

    @Override
    public Carta consultarCartaDescubierta() {
        return null;
    }

    @Override
    public boolean consultarCerrado() {
        return false;
    }

    @Override
    public boolean consultarPartidaActualizada() {
        return false;
    }

    @Override
    public boolean consultarTurno() {
        return false;
    }

    @Override
    public Mano consultarMano() {
        return null;
    }

    @Override
    public boolean partidaEnCurso() {
        return false;
    }
}
