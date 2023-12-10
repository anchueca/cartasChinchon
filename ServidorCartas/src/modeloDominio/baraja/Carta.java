package modeloDominio.baraja;

import java.io.Serializable;

public class Carta implements Comparable<Carta>, Serializable {
    private final Palo palo;
    private final int numero;

    public Carta(int numero, Palo palo) {
        this.palo = palo;
        this.numero = numero;
    }

    public Palo getPalo() {
        return palo;
    }

    public int getNumero() {
        return numero;
    }

    public int compareTo(Carta o) {
        if (this.getPalo() == o.getPalo()) return Integer.compare(this.numero, o.numero);
        return this.getPalo().compareTo(o.getPalo());
    }

    public String toString() {
        String tipoCarta = switch (numero) {
            case 1 -> "As";
            case 10 -> "Sota";
            case 11 -> "Caballo";
            case 12 -> "Rey";
            default -> String.valueOf(numero);
        };

        return tipoCarta + " de " + this.palo;
    }

}
