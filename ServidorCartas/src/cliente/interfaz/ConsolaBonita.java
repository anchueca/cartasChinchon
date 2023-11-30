package cliente.interfaz;

import cliente.AccionesConsola;
import cliente.EjecutorConsolaBonita;
import cliente.ProcesadorComandos;

import javax.swing.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

public class ConsolaBonita extends JFrame{
    private JTextField Entrada;
    private JTextArea Salida;
    private JLabel Partida;
    private JLabel jugador;
    private JPanel prinicpal;
    private JLabel PartidaActual;
    private ProcesadorComandos procesadorComandos;
    public ConsolaBonita(ProcesadorComandos procesadorComandos) {
        this();
        this.procesadorComandos = procesadorComandos;
    }

    public ConsolaBonita() {
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setBounds(100, 100, 450, 173);
        Entrada.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER){
                    meterSalida(procesadorComandos.procesarInstrccion(Entrada.getText()));
                    Entrada.setText("");
                }
            }
        });
    }

    public void setPartida(String nombrePartida) {
        PartidaActual.setText(nombrePartida);
    }

    public void setJugador(String nombrePartida) {
        jugador.setText(nombrePartida);
    }

    public static void main(String[] args){
        new ConsolaBonita().iniciar();
    }
    public JFrame iniciar() {
        ProcesadorComandos procesadorComandos=new ProcesadorComandos(new EjecutorConsolaBonita());
        ConsolaBonita consolaBonita=new ConsolaBonita(procesadorComandos);
        AccionesConsola acciones=procesadorComandos.getAccionesConsola();
        if(acciones instanceof EjecutorConsolaBonita)((EjecutorConsolaBonita) acciones).setConsolaBonita(consolaBonita);
        else consolaBonita.Salida.setText("Error en el procesador de instrucciones. La funcionalidad puede verse limitada");

        JFrame frame = new JFrame("ConsolaBonita");
        frame.setTitle("SuperChinchon");
        frame.setContentPane(consolaBonita.prinicpal);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);
        return frame;
    }

    public void meterSalida(String salida){
        Salida.setText(Salida.getText()+"\n"+salida);
    }

    public void limpiarPantalla(){
        Salida.setText("");
    }


}
