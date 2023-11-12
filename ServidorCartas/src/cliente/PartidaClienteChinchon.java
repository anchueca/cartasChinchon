package cliente;

import modeloDominio.ProcesadorMensajes;
import org.w3c.dom.Document;

import java.io.IOException;
import java.lang.annotation.Documented;
import java.net.Socket;

public class PartidaClienteChinchon extends PartidaCliente{
    public PartidaClienteChinchon(Socket s,PresentacionChinchon interfaz) {
        super(s,interfaz);
    }

    @Override
    public void atenderServidor() throws IOException {
        if(!ProcesadorMensajes.enEsperaXML(this.s))return;
        Document xml=ProcesadorMensajes.recibirXml(this.s);
    }

    public PresentacionChinchon getInterfaz(){
        return (PresentacionChinchon) super.getInterfaz();
    }
}
