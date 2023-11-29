package cliente;

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
    private procesadorComandos procesadorComandos;
    public ConsolaBonita(procesadorComandos procesadorComandos) {
        this();
        this.procesadorComandos = procesadorComandos;
    }

    public ConsolaBonita() {
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setBounds(100, 100, 450, 173);
        Entrada.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() != KeyEvent.VK_ENTER)return;
                meterSalida(procesadorComandos.procesarInstrccion(Entrada.getText()));
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

    public static JFrame iniciar(procesadorComandos procesadorComandos) {

        ConsolaBonita consolaBonita=new ConsolaBonita(procesadorComandos);

        JFrame frame = new JFrame("ConsolaBonita");
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
