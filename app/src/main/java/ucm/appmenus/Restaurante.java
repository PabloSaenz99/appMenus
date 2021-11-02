package ucm.appmenus;

import java.util.ArrayList;

public class Restaurante {

    private final int idRestaurante;
    private final String nombre;
    private final String descripcion;
    private final float valoracion;
    private final String imagenPrincDir;
    private final ArrayList<String> filtros;
    private final ArrayList<String> imagenesDir;

    public Restaurante(int idRestaurante, String nombre, String descripcion, float valoracion, String imagenPrincDir,
                       ArrayList<String> filtros, ArrayList<String> imagenesDir){
        this.idRestaurante = idRestaurante;
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

    public float getValoracion() {
        return valoracion;
    }

    public String getimagenPrincDir() {
        return imagenPrincDir;
    }

    public ArrayList<String> getFiltros() {
        return filtros;
    }
}
