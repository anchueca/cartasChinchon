package modeloDominio.baraja;

import java.io.Serializable;
import java.util.*;


public class Mano implements Iterable<Carta>, Serializable {
    private final List<Carta> cartas;

    public Mano(List<Carta> cartas) {
        this.cartas = cartas;
    }
    /*
    Constructor copia
     */
    public Mano (Mano mano){
        this.cartas=new ArrayList<>(mano.numCartas());
        for(Carta carta : mano)this.cartas.add(carta);
    }
    public Mano(Collection<Carta> cartas) {
        this.cartas = new ArrayList<>(cartas);
    }

    public Mano(Carta carta) {
        this();
        this.cartas.add(carta);
    }

    public Mano() {
        this.cartas = new ArrayList<>();
    }

    /*
        Por ahora asumo que las escaleras y tríos están agrupados
    Codificado primer bit 0 si es un trío o 1 si es pareja el resto de bits representan las cartas onvolucradas
    ej: 10001110 escalera de la cuarta, quinta y sexta carta.
     */
    /*
    Recibe una lista de 7 cartas (pares de valores número, palo) ordenado y devuelva una lista de bytes que codifica
    lo siguiente: cada byte representa un conjunto de cartas casadas (conjunto de tres o más cartas del mismo número
    al que llamaré siempre trío o tres o más cartas en escalera del mismo palo) de modo que el primer bit representa
    si es un trío (0) o una escalera (1) y el resto de bits estarán a uno si la posición de esa carta en la lista
    forma parte del trío o escalera. Las cartas casadas ya están agrupadas y una misma carta no puede estar en varios
    tríos o escaleras
     */
    public static List<BitSet> cartasCasadas(Mano mano) {
        //Lista que almacenrá las casadas
        List<BitSet> lista = new ArrayList<>();
        //Indicador de casadas
        BitSet indicador = new BitSet(7);
        Carta cartaPrevia = mano.verCarta(0);
        Carta cartaActual;
        //Recorro las cartas desde la primera
        for (int numCarta = 1, cartasTotales = mano.numCartas(); numCarta < cartasTotales; numCarta++) {
            //Tomo la siguiente carta
            cartaActual = mano.verCarta(numCarta);
            //si coinciden caso trío
            if (cartaActual.getNumero() == cartaPrevia.getNumero()) {
                //Si buscaba tríos incorpora la carta
                if (!indicador.get(7)) indicador.set(numCarta);
                else {//Si no compruebo si era un trío(o más cartas) y lo tengo en cuenta si procede
                    if (indicador.cardinality()>2) lista.add(indicador);
                    //Reinicio
                    indicador.clear();
                }
                //Compruebo si se presta a una escalera del mismo palo
            } else if (cartaActual.getNumero() - 1 == cartaPrevia.getNumero()
                    && cartaPrevia.getPalo() == cartaActual.getPalo()) {
                //Si buscaba la escalera añado la carta
                if (indicador.get(7)) indicador.set(numCarta);
                else {//Sino compruebo si la escalera es válida (al menos tres cartas) y añado si procede
                    if (indicador.cardinality()-1>2)//al menos tres y debo tener en cuenta el bit de escalera
                        lista.add(indicador);
                    //reinicio
                    indicador.clear();
                }
            }
            cartaPrevia=cartaActual;
        }
        //Caso final
        if (indicador.cardinality()>2) lista.add(indicador);/*al menos tres*/
        return lista;
    }

    public int numCartas() {
        return this.cartas.size();
    }

    public Carta tomarCarta(Carta carta) {
        int i = this.cartas.indexOf(carta);
        if (i != -1) {
            Carta carta1 = this.cartas.get(i);
            this.cartas.remove(i);
            return carta1;
        }
        return null;
    }

    public Carta tomarCarta(int carta) {
        try {
            Carta carta1 = this.cartas.get(carta);
            this.cartas.remove(carta);
            return carta1;
        } catch (IndexOutOfBoundsException e) {
            return null;
        }

    }

    public Carta verCarta(int i) {
        try {
            return this.cartas.get(i);
        } catch (IndexOutOfBoundsException e) {
            return null;
        }
    }
    public Carta verCarta(Carta i) {
        int j = this.cartas.indexOf(i);
        if (j != -1) {
            return this.cartas.get(j);
        }
        return null;
    }

    public void añadirCarta(Carta carta) {
        this.cartas.add(carta);
    }
    public void añadirCarta(Carta carta,int destino) {
        this.cartas.add(destino,carta);
    }

    public void ordenar() {
        this.cartas.sort(null);
    }

    public void permutar(int i, int j) {
        if (i >= 0 && i < this.cartas.size() && j >= 0 && j < this.cartas.size()) {
            Carta carta = this.cartas.get(i);
            this.cartas.set(i, this.cartas.get(j));
            this.cartas.set(j, carta);
        }
    }
    @Override
    public Iterator<Carta> iterator() {
        return this.cartas.iterator();
    }
    public String toString(){
        return "Mano: "+this.cartas.toString();
    }
}
