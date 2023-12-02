package cliente.interfaz;

import cliente.AccionesConsola;
import cliente.EjecutorConsolaBonita;
import cliente.ProcesadorComandos;
import cliente.excepciones.NumeroParametrosExcepcion;

import javax.swing.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.LinkedList;
import java.util.List;

public class ConsolaBonita extends JFrame {
    List<String> comandos;
    int indice;
    private JTextField Entrada;
    private JTextArea Salida;
    private JLabel Partida;
    private JLabel jugador;
    private JPanel prinicpal;
    private JLabel PartidaActual;
    private final ProcesadorComandos procesadorComandos;

    public ConsolaBonita() {
        this.procesadorComandos = new ProcesadorComandos(new EjecutorConsolaBonita(this));
        this.indice = 0;
        this.comandos = new LinkedList<>();
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setBounds(100, 100, 450, 173);
        Entrada.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                gestionarEntrada(e);
            }
        });

        AccionesConsola acciones = procesadorComandos.getAccionesConsola();
        if (acciones instanceof EjecutorConsolaBonita) ((EjecutorConsolaBonita) acciones).setConsolaBonita(this);
        else this.Salida.setText("Error en el procesador de instrucciones. La funcionalidad puede verse limitada");

        this.setTitle("SuperChinchon");
        this.setContentPane(this.prinicpal);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.pack();
        this.setVisible(true);
    }

    public static void main(String[] args) {
        new ConsolaBonita()/*.iniciar()*/;
    }

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

    public void gestionarEntrada(KeyEvent e) {
        switch (e.getKeyCode()) {
            case KeyEvent.VK_ENTER:
                String entrada = this.Entrada.getText();
                try {
                    this.Entrada.setText("");
                    if (entrada.length() > 100) {//limita la entrada
                        meterSalida("Error: El comando es demasiado largo");
                        return;
                    }
                    if (entrada.isEmpty()) return;
                    //procesa
                    if (!this.procesadorComandos.procesarInstrccion(entrada)) meterSalida("Comando no reconocido");
                } catch (NumeroParametrosExcepcion ex) {
                    meterSalida("Número de parámetros incorrecto");
                }
                this.comandos.add(0, entrada);
                this.indice = -1;
                break;

            case KeyEvent.VK_UP:
                if (this.indice < this.comandos.size() - 1) this.indice++;
                this.Entrada.setText(this.comandos.get(this.indice));
                break;

            case KeyEvent.VK_DOWN:
                if(this.indice==0)this.Entrada.setText("");
                else if (this.indice > 0) this.indice--;
                else this.Entrada.setText(this.comandos.get(this.indice));
                break;
        }
    }
}
