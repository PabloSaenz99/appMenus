package ucm.appmenus.entities;

import java.util.ArrayList;

public class Restaurante {

    private final String idRestaurante;
    private final String nombre;
    private final String url;
    private String direccion;
    private final int telefono;
    private final String horarios;
    private final double valoracion;
    private final String imagenPrincDir;
    private final ArrayList<String> filtros;
    private final ArrayList<Foto> imagenesDir;

    public Restaurante(String idRestaurante, String nombre, String url, String direccion,
                       int telefono, String horarios, double valoracion, String imagenPrincDir,
                       ArrayList<String> filtros, ArrayList<Foto> imagenesDir){
        this.idRestaurante = idRestaurante;
        this.nombre = nombre;
        this.url = url;
        this.direccion = direccion;
        this.telefono = telefono;
        this.horarios = horarios;
        this.valoracion = valoracion;
        this.imagenPrincDir = imagenPrincDir;
        //TODO: Hacer algo para buscar tipos de comida abriendo la url del restaurante
        if(url != null){

        }
        if(filtros == null) this.filtros = new ArrayList<String>();
        else this.filtros = filtros;
        if(imagenesDir == null) this.imagenesDir = new ArrayList<Foto>();
        else this.imagenesDir = imagenesDir;
    }

    public String getIdRestaurante(){return idRestaurante;}
    public String getNombre() {
        return nombre;
    }
    public String getStringURL() { return url; }
    public String getDireccion() { return direccion; }
    public int getTelefono() { return telefono; }
    public String getHorarios() { return horarios; }
    public double getValoracion() {
        return valoracion;
    }
    public String getimagenPrincDir() {
        return imagenPrincDir;
    }
    public ArrayList<String> getFiltros() {
        return filtros;
    }
    public ArrayList<Foto> getFotos() {
        return imagenesDir;
    }

    public void setDireccion(String direccion) { this.direccion = direccion; }

    @Override
    public String toString(){
        String s = "";
        s+="Id: " + idRestaurante + "\n";
        s+="Nombre: " + nombre + "\n";
        s+="Valoracion: " + valoracion + "\n";
        s+="Imagen principal: " + imagenPrincDir + "\n";
        return s;
    }
}
