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

    /**
     * Recibe una mano ordenada de siete u ocho cartas y devuelve una lista de BitSets que codifican lo siguiente:
     * cada byte representa un conjunto de cartas coincidentes (tres o más cartas del mismo número, llamado trio,
     * o tres o más cartas en secuencia ascendente del mismo palo, llamado escalera).
     * El primer bit representa si es un trío (0) o una escalera (1), y el bit n-ésimo indica
     * si la carta n-ésima en la mano es parte del trío o escalera.
     * Se asume que las cartas coincidentes están agrupadas, y la misma carta no puede estar en múltiples tríos o escaleras.
     * Las escaleras deben ser del mismo palo. En caso de que haya ocho cartas, se ignora la última.
     *
     * @param mano la mano de cartas
     * @return una lista de BitSets que representan las cartas coincidentes
     */
    //AUN HAY QUE RETOCAR. SI EL TRÍO VA PRIMERO PARECE IR BIEN
    public static List<BitSet> cartasCasadas(Mano mano) {
        //Lista que almacenrá las casadas
        List<BitSet> lista = new ArrayList<>();
        //Indicador de casadas
        BitSet indicador = new BitSet(8);
        indicador.set(1);//Caso inicial
        Carta cartaPrevia = mano.verCarta(1);
        Carta cartaActual;
        //Recorro las cartas desde la segunda
        for (int numCarta = 1; numCarta < 7; numCarta++) {
            //Tomo la siguiente carta
            cartaActual = mano.verCarta(numCarta);

            //Gestiono caso base particlar escalera
            if(cartaActual.getNumero() - 1 == cartaPrevia.getNumero()
                    && cartaPrevia.getPalo() == cartaActual.getPalo()&& numCarta==1)
                indicador.set(0);


            //si coinciden caso trío
            if (cartaActual.getNumero() == cartaPrevia.getNumero()) {
                //Si buscaba tríos incorpora la carta
                if (!indicador.get(0)) indicador.set(numCarta+1);
                else {//Se acaban las posibles casadas. Compruebo si era una escalera y lo tengo en cuenta si procede
                    if (indicador.cardinality()>3) {//Tengo en cuenta el uno de la escalera
                        lista.add(indicador);
                        indicador=new BitSet(8);
                    }
                    //Reinicio
                    indicador.clear();
                    //Añado el actual
                    indicador.set(numCarta+1);
                }
                //Compruebo si se presta a una escalera del mismo palo
            } else if (cartaActual.getNumero() - 1 == cartaPrevia.getNumero()
                    && cartaPrevia.getPalo() == cartaActual.getPalo()) {
                //Si buscaba la escalera añado la carta
                if (indicador.get(0)) indicador.set(numCarta+1);
                else {//Si no compruebo si el trío es válido (al menos tres cartas) y añado si procede
                    if (indicador.cardinality()>2){
                        lista.add(indicador);
                        indicador=new BitSet(8);
                    }
                    //reinicio
                    indicador.clear();
                    //pongo el primero y el actual
                    indicador.set(0);
                    indicador.set(numCarta);
                    indicador.set(numCarta+1);
                }
            }else{//Caso que no se preste a ninguna. Se cancela también el posible
                if (indicador.cardinality()>(indicador.get(0)?3:2)){
                    lista.add(indicador);
                    indicador=new BitSet(8);
                }
                //reinicio
                indicador.clear();
            }
            cartaPrevia=cartaActual;
        }
        //Caso final
        if (indicador.cardinality()>2) lista.add(indicador);//al menos tres
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
