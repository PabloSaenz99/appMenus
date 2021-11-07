package ucm.appmenus;

import android.net.Uri;

import java.util.ArrayList;

import ucm.appmenus.exceptions.RestaurantNotFoundException;

public class Restaurante {

    private final String idRestaurante;
    private final String nombre;
    private final Uri url;
    private final double valoracion;
    private final String imagenPrincDir;
    private final ArrayList<String> filtros;
    private final ArrayList<Foto> imagenesDir;

    public Restaurante(String idRestaurante, String nombre, Uri url, double valoracion,
                       String imagenPrincDir, ArrayList<String> filtros, ArrayList<Foto> imagenesDir){
        this.idRestaurante = idRestaurante;
        this.nombre = nombre;
        this.url = url;
        this.valoracion = valoracion;
        this.imagenPrincDir = imagenPrincDir;
        this.filtros = filtros;
        this.imagenesDir = imagenesDir;
        //TODO: Hacer algo para buscar tipos de comida
        if(url != null){

        }
    }

    public String getIdRestaurante(){return idRestaurante;}
    public String getNombre() {
        return nombre;
    }
    public Uri getUri() {return url;}
    public String getStringURL() { return url.toString(); }
    public double getValoracion() {
        return valoracion;
    }
    public String getimagenPrincDir() {
        return imagenPrincDir;
    }
    public ArrayList<String> getFiltros() {
        return filtros;
    }

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
