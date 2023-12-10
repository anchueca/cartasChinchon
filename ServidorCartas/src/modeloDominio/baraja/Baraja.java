package modeloDominio.baraja;

import java.util.*;

public class Baraja implements Iterable<Carta> {
    private final Tamano tipo;
    private Deque<Carta> cartas;

    public Baraja(Deque<Carta> cartas, Tamano tamano) {
        this.cartas = cartas;
        this.tipo = tamano;
    }

    public Baraja(Collection<Carta> cartas, Tamano tamano) {
        this.cartas = new ArrayDeque<>(cartas);
        this.tipo = tamano;
    }

    public Baraja() {
        this.cartas = new ArrayDeque<>();
        this.tipo = null;
    }

    public static Baraja barajaFactoria(Tamano tamano) {//Comvendría establecer otros parámetros
        Deque<Carta> cartas = new ArrayDeque<>();
        for (Palo palo : Palo.values()
        ) {
            for (int i = 1; i < 13; i++) {
                if (tamano == Tamano.NORMAL && i == 8) i = 10;//Caso normal
                cartas.add(new Carta(i, palo));
            }
        }
        return new Baraja(cartas, tamano);
    }

    /*
    Toma una carta de la baraja
     */
    public Carta tomarCarta() {
        return this.cartas.poll();
    }

    /*
    Devuelve la carta sin quitarla de la baraja
     */
    public Carta verCarta() {
        return this.cartas.peek();
    }

    public void barajar() {
        ArrayList<Carta> lista = new ArrayList<>(this.cartas);
        Collections.shuffle(lista);
        this.cartas = new ArrayDeque<>(lista);
    }

    public void meterCarta(Carta carta) {
        this.cartas.addFirst(carta);
    }

    /*
    Mete un conjunto de cartas en la baraja
     */
    public void meterCarta(Collection<Carta> cartas) {
        this.cartas.addAll(cartas);
    }

    public void meterCarta(Baraja baraja) {
        this.meterCarta(baraja.cartas);
        baraja.cartas.clear();
    }

    public int numCartas() {
        return this.cartas.size();
    }

    public Iterator<Carta> iterator() {
        return this.cartas.iterator();
    }

    public String toString() {
        return "Baraja " + this.tipo.toString().toLowerCase() + ": cartas " + this.numCartas();
    }
}
