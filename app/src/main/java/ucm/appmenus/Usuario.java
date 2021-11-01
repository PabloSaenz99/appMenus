package ucm.appmenus;

import java.util.ArrayList;

public class Usuario {

    private String email;
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

    public void setEmail(String email) { this.email = email; }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) { this.nombre = nombre; }

    public String getLocalizacion() {
        return localizacion;
    }

    public void getLocalizacion(String localizacion) { this.localizacion = localizacion; }

    public String getImagenDir() {
        return imagenDir;
    }

    public void setImagenDir(String imagenDir) { this.imagenDir = imagenDir; }

    public ArrayList<String> getReseñas() {
        return reseñas;
    }

    public void getReseñas(ArrayList<String> reseñas) { this.reseñas = reseñas; }

    public ArrayList<Restaurante> getRestaurantesFavoritos() {
        return restaurantesFavoritos;
    }

    public void setRestaurantesFavoritos(ArrayList<Restaurante> restaurantesFavoritos) { this.restaurantesFavoritos = restaurantesFavoritos; }

    public ArrayList<String> getPreferencias() {
        return preferencias;
    }

    public void setPreferencias(ArrayList<String> preferencias) { this.preferencias = preferencias; }

}
