package modeloDominio.baraja;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

public class Mano implements Iterable<Carta>, Serializable {
    private final List<Carta> cartas;

    public Mano(List<Carta> cartas) {
        this.cartas = cartas;
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

    public void añadirCarta(Carta carta) {
        this.cartas.add(carta);
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


    /*
    Por ahora asumo que las escaleras y tríos están agrupados
Codificado primer bit 0 si es un trío o 1 si es pareja el resto de bits representan las cartas onvolucradas
ej: 10001110 escalera de la cuarta, quinta y sexta carta.
 */
    public static List<Byte> cartasCasadas(Mano mano){
        List<Byte> lista=new ArrayList<>();
        byte indicador=0;
        Carta cartaPrevia=mano.verCarta(0);
        Carta cartaActual;
        for(int i=1,j=mano.numCartas();i<j;i++){
            //nueva carta
            cartaActual=mano.verCarta(i);
            //si coinciden caso trío
            if(cartaActual.getNumero()==cartaPrevia.getNumero()){
                //Si buscaba tríos incorpora la carta
                if((indicador>>6)==0)indicador|= 1<<i;
                else{//Sino compruebo si era un trío(o más cartas) y lo tengo en cuenta si procede
                    if(Mano.comprobacionMayorQueTres(indicador))lista.add(indicador);
                    //Reinicio
                    indicador= (byte) (1 << i);
                }
            //Compruebo si se presta a una escalera
            } else if(cartaActual.getNumero()-1==cartaPrevia.getNumero()
                    && cartaPrevia.getPalo()==cartaActual.getPalo()){
                //Si buscaba la escalera añado la carta
                if((indicador>>6)==1)indicador|= 1<<i;
                else{//Sino compruebo si la escalera es válida (al menos tres cartas) y añado si prcede
                    if(Mano.comprobacionMayorQueTres(indicador))lista.add(indicador);/*al menos tres*/
                    //reinicio
                    indicador= (byte) ((1 << i) | (1<<6));
                }
            }
        }
        //Caso final
        if(Mano.comprobacionMayorQueTres(indicador))lista.add(indicador);/*al menos tres*/
        return lista;
    }
    private static boolean comprobacionMayorQueTres(byte indicador){
        int contador = 0;
        for (int i = 0; i < 8; i++) {
            contador += (indicador >> i) & 1;
            if (contador > 3) return true;
        }
        return false;
    }
}
