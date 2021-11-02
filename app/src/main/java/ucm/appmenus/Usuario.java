package ucm.appmenus;

import java.util.ArrayList;

public class Usuario {

    private final String email;
    private String nombre;
    private String localizacion;
    private String imagenDir;
    private ArrayList<String> reseñas;
    private ArrayList<Restaurante> restaurantesFavoritos;
    private ArrayList<String> preferencias;

    public Usuario(String nombre, String email, String localizacion, String imagenDir, ArrayList<String> reseñas,
                   ArrayList<Restaurante> restaurantesFavoritos, ArrayList<String> preferencias) {
        this.nombre = nombre;
        this.email = email;
        this.localizacion = localizacion;
        this.imagenDir = imagenDir;
        this.reseñas = reseñas;
        this.restaurantesFavoritos = restaurantesFavoritos;
        this.preferencias = preferencias;
    }

    public String getEmail() {
        return email;
    }
    public String getNombre() {
        return nombre;
    }
    public String getLocalizacion() {
        return localizacion;
    }
    public String getImagenDir() {
        return imagenDir;
    }
    public ArrayList<String> getReseñas() {
        return reseñas;
    }
    public ArrayList<Restaurante> getRestaurantesFavoritos() {
        return restaurantesFavoritos;
    }
    public ArrayList<String> getPreferencias() {
        return preferencias;
    }

    public void setNombre(String nombre) { this.nombre = nombre; }
    public void setLocalizacion(String localizacion) { this.localizacion = localizacion; }
    public void setImagenDir(String imagenDir) { this.imagenDir = imagenDir; }
    public void setReseñas(ArrayList<String> reseñas) { this.reseñas = reseñas; }
    public void setRestaurantesFavoritos(ArrayList<Restaurante> restaurantesFavoritos) { this.restaurantesFavoritos = restaurantesFavoritos; }
    public void setPreferencias(ArrayList<String> preferencias) { this.preferencias = preferencias; }

}
