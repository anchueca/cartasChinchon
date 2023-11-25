package modeloDominio.baraja;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

public class Mano implements Iterable<Carta>, Serializable {
    private List<Carta> cartas;

    public Mano(List<Carta> cartas) {
        this.cartas = cartas;
    }
    public Mano(Collection<Carta> cartas){
        this.cartas=new ArrayList<>(cartas);
    }
    public Mano(Carta carta){this();this.cartas.add(carta);}
    public Mano(){
        this.cartas=new ArrayList<>();
    }

    public Carta tomarCarta(Carta carta){
        int i=this.cartas.indexOf(carta);
        if(i!=-1){
            Carta carta1=this.cartas.get(i);
            this.cartas.remove(i);
            return carta1;
        }
        return null;
    }
    public Carta tomarCarta(int carta){
            Carta carta1=this.cartas.get(carta);
            this.cartas.remove(carta);
            return carta1;
    }
    public Carta verCarta(int i){
        try {
            return this.cartas.get(i);
        }catch (IndexOutOfBoundsException e){
            return null;
        }
    }
    public void añadirCarta(Carta carta){
        this.cartas.add(carta);
    }
    public void ordenar(){
        this.cartas.sort(null);
    }
    public void permutar(int i, int j){
        Carta carta=this.cartas.get(i);
        this.cartas.add(i,this.cartas.get(j));
        this.cartas.add(j,carta);
    }
    @Override
    public Iterator<Carta> iterator() {
        return this.cartas.iterator();
    }
}
