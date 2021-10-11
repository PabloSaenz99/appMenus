package ucm.appmenus;

import java.util.ArrayList;

public class Restaurante {

    private final String nombre;
    private final String descripcion;
    private final double valoracion;
    private final String imagenPrincDir;
    private final ArrayList<String> filtros;
    private final ArrayList<String> imagenesDir;

    public Restaurante(String nombre, String descripcion, double valoracion, String imagenPrincDir,
                       ArrayList<String> filtros, ArrayList<String> imagenesDir){
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.valoracion = valoracion;
        this.imagenPrincDir = imagenPrincDir;
        this.filtros = filtros;
        this.imagenesDir = imagenesDir;
    }

    public String getNombre() {
        return nombre;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public double getValoracion() {
        return valoracion;
    }

    public String getimagenPrincDir() {
        return imagenPrincDir;
    }

    public ArrayList<String> getFiltros() {
        return filtros;
    }
}
