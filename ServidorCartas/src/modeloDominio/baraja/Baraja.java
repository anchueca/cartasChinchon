package modeloDominio.baraja;

import java.util.*;

public class Baraja implements Iterable<Carta> {
    private Deque<Carta> cartas;

    public Baraja(Deque<Carta> cartas) {
        this.cartas = cartas;
    }
    public Baraja(Collection<Carta> cartas){
        this.cartas= new ArrayDeque<>(cartas);
    }
    public static Baraja barajaFactoria(Tamano tamano){//Comvendría establecer otros parámetros
        Deque<Carta> cartas=new ArrayDeque<>();

        for (Palo palo: Palo.values()
             ) {
            for(int i=1;i<13;i++){
                if(tamano==Tamano.NORMAL && i==8)i=10;//Caso normal
                cartas.add(new Carta(i,palo));
            }
        }
        return new Baraja(cartas);
    }
    public Carta tomarCarta() {
        return this.cartas.peekFirst();
    }
    public void barajar() {
        List<Carta> lista=new ArrayList<>(this.cartas);
        Collections.shuffle(lista);
        this.cartas=new ArrayDeque<>(lista);
    }
    public void meterCarta(Carta carta) {
        this.cartas.addFirst(carta);
    }
    public int numCartas(){
        return this.cartas.size();
    }
    public Iterator<Carta> iterator() {
        return this.cartas.iterator();
    }
}
