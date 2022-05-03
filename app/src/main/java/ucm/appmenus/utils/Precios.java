package ucm.appmenus.utils;

public class Precios {

    public double minimo;
    public double mediana;
    public double maximo;

    public Precios(double minimo, double mediana, double maximo){
        this.minimo = minimo;
        this.mediana = mediana;
        this.maximo = maximo;
    }

    public Precios(){
        this(0, 0, 0);
    }

    public boolean esCorrecto(){ return minimo > 0; }
}
