package cliente;

import modeloDominio.EstadoPartida;
import modeloDominio.baraja.Carta;
import modeloDominio.baraja.Mano;

import javax.swing.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.List;
import java.util.Map;

public class ConsolaBonita extends JFrame {
    private JTextField Entrada;
    private JTextArea Salida;
    private JLabel Partida;
    private JLabel jugador;
    private JPanel panel;
    private JLabel PartidaActual;
    private Cliente cliente;
    private PartidaCliente partida;
    public ConsolaBonita(Cliente cliente,PartidaCliente partida) {
        this.cliente=cliente;
        this.partida=partida;
    }

    public ConsolaBonita() {
        setSize(600, 400);
        Entrada.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                super.keyPressed(e);
                Salida.setText(procesarInstrccion(Entrada.getText()));
                Entrada.setText("");
            }
        });
    }

    void interpretarComando(){
        this.Salida.setText("Comando introducido");
    }
    public void setPartida(String nombrePartida) {
        PartidaActual.setText(nombrePartida);
    }

    public void setJugador(String nombrePartida) {
        jugador.setText(nombrePartida);
    }

    /*
    Proesa los comandos
     */
    public String procesarInstrccion(String instruccion) {
        String[] palabras = instruccion.split("\\s+");
        try {
            switch (palabras[0]) {
                case "entrar": {
                    if (this.partida != null || palabras.length != 3) throw new NumeroParametrosExcepcion();
                    return this.unirsePartida(palabras[1], palabras[2]);
                }
                case "crear": {
                    if (this.partida != null || palabras.length != 2) throw new NumeroParametrosExcepcion();
                    return this.crearPartida(palabras[1]);
                }
                case "salir": {
                    if (this.partida != null) {
                        return this.salirPartida();
                    } else {
                        // abandon program
                        return this.salir();
                    }
                }
                case "ajuda": {
                    return "Mostrando ayuda " + (this.partida == null ? "inicio" : "juego");
                }
                case "partidas": {
                    if (this.partida != null) throw new NumeroParametrosExcepcion();
                    String n = "Lista partidas:\n";
                    List<String> lista = this.cliente.listaPartidas();
                    for (int i = 0, j = lista.size(); i < j; i++) {
                        n += i + ".- " + lista.get(i) + "\n";
                    }
                    return n;
                }
                case "mover": {
                    if (this.partida == null || palabras.length != 3) throw new NumeroParametrosExcepcion();
                    this.partida.moverMano(Integer.parseInt(palabras[1]), Integer.parseInt(palabras[2]));
                    break;
                }
                case "echar": {
                    if (this.partida == null || palabras.length != 2) throw new NumeroParametrosExcepcion();
                    int i = Integer.parseInt(palabras[1]);
                    Mano mano = this.partida.verMano();
                    if (this.partida.echarCarta(mano.verCarta(i))) {
                        mano.tomarCarta(i);
                    } else return "Jugada no válida";
                    break;
                }
                case "cerrar": {
                    if (this.partida == null || palabras.length != 2) throw new NumeroParametrosExcepcion();
                    int i = Integer.parseInt(palabras[1]);
                    Mano mano = this.partida.verMano();
                    if (this.partida.cerrar(mano.verCarta(i))) {
                        mano.tomarCarta(i);
                    } else return ("Jugada no válida");
                    break;
                }
                case "coger": {
                    if (this.partida == null || palabras.length > 2) throw new NumeroParametrosExcepcion();
                    boolean jugadaValida;
                    if (palabras.length == 1) {
                        jugadaValida = this.partida.cogerCartaCubierta();
                    } else {
                        jugadaValida = palabras[1].compareTo("descubierta") == 0
                                ? this.partida.cogerCartaDecubierta()
                                : this.partida.cogerCartaCubierta();
                    }
                    if (!jugadaValida) return ("Jugada no válida");
                    break;
                }
                case "ordenar": {
                    if (this.partida == null) throw new NumeroParametrosExcepcion();
                    this.partida.verMano().ordenar();
                    break;
                }
                case "empezar": {
                    if (this.partida == null) throw new NumeroParametrosExcepcion();
                    this.partida.empezarPartida();
                    break;
                }
                case "jugadores": {
                    return this.listaJugadores();
                }
                case "estado": {
                    return this.estado();
                }
                case "puntuaciones": {
                    return this.puntuaciones();
                }
                default:
                    return ("Comando no reconocido.");
            }
        } catch (NumeroParametrosExcepcion ex) {
            return ("Número de parámetros incorrecto.");
        } catch (NumberFormatException e) {
            return ("Parámetros incorrectos");
        }
        return "OK";
    }

    public void setPartida(PartidaCliente partida){
        if(partida!=null)System.out.println("Cargando partida");
        else System.out.println("Abandonando partida...");
        this.partida=partida;
    }

    private String listaJugadores(){
        String cadena="Juagdores: ";
        for (String ca: this.partida.listaJugadores()
        ) {
            cadena+=ca+" ";
        }
        return cadena;
    }

    private String estado(){
        String ca="Partida: "+this.partida.getNombrePartida()+"\nJugador: "+this.partida.getNombreJuagador()+
                "\n"+this.listaJugadores()+"\nEstado de la partida: "+this.partida.verEstadoPartida().toString();
        return ca;
    }

    private String puntuaciones(){
        Map<String,Integer> mapa=this.partida.verPuntuaciones();
        String ca="Puntuaciones:";
        for (String nombre:mapa.keySet()
        ) {
            ca+="\n"+nombre+": "+mapa.get(nombre);
        }
        return ca;
    }

    private String unirsePartida(String partida,String jugador){
        if(this.cliente.unirsePartida(partida,jugador))return "Entando,,,\n"+this.estado();
        return "No se ha podido entrar";
    }
    private String crearPartida(String partida){
        if(this.cliente.crearPartida(partida))return "Partida creada";
        return "No se ha podido crear";
    }

    private String salir(){
        if(this.cliente.salir())return "Saliendo...";
        return "Error al salir";
    }

    private String salirPartida(){

        if(this.partida.salir()){
            this.partida=null;
            return "saliendo de la partida";
        }
        return "No es posible abandonar la partida";
    }
    private String pintarPartida(){
        String salida="";
        if(this.partida.verEstadoPartida()==EstadoPartida.ENCURSO){

            //System.out.print("\n\n\nEstado de la partida: "+this.partida.es);
            Mano mano=this.partida.verMano();
            if(mano!=null)for (Carta carta: mano
            ) {
                salida+=" "+carta;
            }
            Carta descubierta=this.partida.verCartaDescubierta();

            salida+="Descubierta: "+(descubierta==null?"Nada":descubierta);
        }
        return salida;
    }
}
