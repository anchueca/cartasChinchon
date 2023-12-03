package servidor.usuarios;

import modeloDominio.AccionesChinchonI;
import modeloDominio.baraja.Carta;
import modeloDominio.baraja.Mano;
import servidor.Partida;
/*
El jugador realiza acciones que comunica a la partida a traves del atrobuto partida
 */
public abstract class Jugador implements AccionesChinchonI {
    protected Mano mano;
    private final String nombre;
    private int puntuacion;
    private boolean actualizado;
    private Partida partida;

    protected Jugador(String nombre,Partida partida) {
        this.nombre = nombre;
        this.mano = new Mano();
        this.puntuacion = 0;
        this.actualizado = false;
    }
    protected Jugador(String nombre,Partida partida,Mano mano,int puntuacion) {
        this.nombre = nombre;
        this.mano = mano;
        this.puntuacion = puntuacion;
        this.actualizado = false;
    }

    /////GETTERS Y SETTERS
		/*
	Devuelve falso si ha habido cambios (llamada a actualizado()) desde la última vez que se llamó al método.
	 */
    public boolean estaActualizado() {
        if (!this.actualizado) {
            this.actualizado = true;
            return false;
        }
        return true;
    }
    protected Partida getPartida(){
        return this.partida;
    }

    public abstract void darTurno();

    public String getNombre() {
        return this.nombre;
    }

    public int verPuntuacion() {
        return this.puntuacion;
    }

    public void setPuntuacion(int i) {
        this.puntuacion = i;
    }

    public Mano verMano() {
        return this.mano;
    }


    public String toString() {
        return this.nombre;
    }

    public void actualizado() {
        this.actualizado = false;
    }

    ///////ACCIOENS//////
    public void darCarta(Carta carta) {
        this.mano.añadirCarta(carta);
    }

    public Carta tomarCarta(int carta) {
        return this.mano.tomarCarta(carta);
    }


	/*
	Cada vez que se modifique un valor por parte del servior se llamará a este método
	 */

}
