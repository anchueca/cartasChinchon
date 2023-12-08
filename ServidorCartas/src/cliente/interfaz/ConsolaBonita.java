package cliente.interfaz;

import cliente.EjecutorConsolaBonita;
import cliente.ProcesadorComandos;
import modeloDominio.excepciones.NumeroParametrosExcepcion;

import javax.swing.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

public class ConsolaBonita extends JFrame {
    private final ProcesadorComandos procesadorComandos;
    List<String> comandos;
    int indice;
    private JTextField Entrada;
    private JTextArea Salida;
    private JLabel Partida;
    private JLabel jugador;
    private JPanel prinicpal;
    private JLabel PartidaActual;

    //Constructor del cliente y la interfaz
    public ConsolaBonita() throws IOException {
        //Instancio el procesador y le asocio el objeto que se ocupará de procesar los comandos (este debe conocer la interfaz)
        this.procesadorComandos = new ProcesadorComandos(new EjecutorConsolaBonita(this));
        //Inicializo el historial
        this.indice = 0;
        this.comandos = new LinkedList<>();
        //Configuración de la interfaz
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setBounds(100, 100, 450, 173);
        //Añado el evento de entrada por teclado
        Entrada.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                gestionarEntrada(e);
            }
        });

        //AccionesConsola acciones = procesadorComandos.getAccionesConsola();
        //if (acciones instanceof EjecutorConsolaBonita) ((EjecutorConsolaBonita) acciones).setConsolaBonita(this);
        //else this.Salida.setText("Error en el procesador de instrucciones. La funcionalidad puede verse limitada");

        this.setTitle("SuperChinchon");
        this.setContentPane(this.prinicpal);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.pack();
        this.setVisible(true);
    }

    //Entrada al programa cliente
    public static void main(String[] args) {
        try {
            new ConsolaBonita()/*.iniciar()*/;
        } catch (IOException e) {
            System.out.println("No se ha podido establecer la conexión");
            e.printStackTrace();
        }
    }

    /*
    Manipulación de los label de información
     */
    public void setPartida(String nombrePartida) {
        PartidaActual.setText(nombrePartida);
    }

    public void setJugador(String nombrePartida) {
        jugador.setText(nombrePartida);
    }

    public void meterSalida(String salida) {
        Salida.setText(Salida.getText() + "\n" + salida);
    }

    public void limpiarPantalla() {
        Salida.setText("");
    }

    /*
    Procesa la intrducción de comandos
     */
    public void gestionarEntrada(KeyEvent e) {
        switch (e.getKeyCode()) {
            /*
            Cuando se pulsa enter se ejecuta el comando
             */
            case KeyEvent.VK_ENTER:
                String entrada = this.Entrada.getText();
                //Limito el tamaño del comando por seguridad
                try {
                    //Si la entrada es vacía no me molesto
                    if (entrada.isEmpty()) return;
                    this.Entrada.setText("");
                    if (entrada.length() > 100) {//limita la entrada
                        meterSalida("Error: El comando es demasiado largo");
                        return;
                    }
                    //Manda procesar y ejecutar el comando
                    if (!this.procesadorComandos.procesarInstruccion(entrada)) meterSalida("Comando no reconocido");
                } catch (NumeroParametrosExcepcion ex) {
                    meterSalida("Número de parámetros incorrecto");
                }
                //Añado el comando al historial
                this.comandos.add(0, entrada);
                this.indice = -1;
                break;
            //Navegación por el historial
            //Por ahora no está limitado
            case KeyEvent.VK_UP:
                if (this.indice < this.comandos.size() - 1) this.indice++;
                this.Entrada.setText(this.comandos.get(this.indice));
                break;
            case KeyEvent.VK_DOWN:
                if (this.indice == 0) this.Entrada.setText("");
                else if (this.indice > 0) this.indice--;
                else this.Entrada.setText(this.comandos.get(this.indice));
                break;
        }
    }
}
