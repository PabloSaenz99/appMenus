package ucm.appmenus;

import java.util.ArrayList;

import ucm.appmenus.utils.Localizacion;
import ucm.appmenus.utils.Pair;

public class Usuario {

    public static final int MAX_FAV = 5;

    private final String email;
    private String nombre;
    private Localizacion localizacion;
    private String imagenDir;
    private ArrayList<String> reseñas;
    private ArrayList<Restaurante> restaurantesFavoritos;
    private ArrayList<String> preferencias;

    public Usuario(String email, String nombre, Localizacion localizacion,
                   String imagenDir, ArrayList<String> reseñas,
                   ArrayList<Restaurante> restaurantesFavoritos, ArrayList<String> preferencias) {
        this.email = email;
        this.nombre = nombre;
        this.localizacion = localizacion;
        this.imagenDir = imagenDir;
        this.reseñas = reseñas;
        this.setRestaurantesFavoritos(restaurantesFavoritos);
        this.preferencias = preferencias;
    }

    public String getEmail() {
        return email;
    }
    public String getNombre() {
        return nombre;
    }
    public Localizacion getLocalizacion() {
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
    public void setLocalizacion(Localizacion localizacion) { this.localizacion = localizacion; }
    public void setImagenDir(String imagenDir) { this.imagenDir = imagenDir; }
    public void setReseñas(ArrayList<String> reseñas) { this.reseñas = reseñas; }
    public void setRestaurantesFavoritos(ArrayList<Restaurante> restaurantesFavoritos) {
        if(restaurantesFavoritos.size() < MAX_FAV)
            this.restaurantesFavoritos = restaurantesFavoritos; }
    public void setPreferencias(ArrayList<String> preferencias) { this.preferencias = preferencias; }

    public void addRestauranteFavorito(Restaurante r){
        if(restaurantesFavoritos.size() < MAX_FAV)
            restaurantesFavoritos.add(r);
    }

    public void removeRestauranteFavorito(Restaurante r){
        int i = 0;
        boolean encontrado = false;
        while(i < restaurantesFavoritos.size() && !encontrado) {
            if(restaurantesFavoritos.get(i).getIdRestaurante().contentEquals(r.getIdRestaurante())) {
                restaurantesFavoritos.remove(i);
                encontrado = true;
            }
            i++;
        }
    }
}
