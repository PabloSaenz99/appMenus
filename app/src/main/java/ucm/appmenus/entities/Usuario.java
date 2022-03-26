package ucm.appmenus.entities;

import android.util.Log;

import java.util.ArrayList;
import java.util.HashSet;

import ucm.appmenus.utils.Localizacion;
import ucm.appmenus.utils.Pair;

public class Usuario {

    public static final int MAX_FAV = 5;
    private static Usuario instance = null;

    private final String email;
    private String nombre;
    private final Localizacion localizacion;
    private String imagenDir;
    private HashSet<Resenia> resenias;
    private HashSet<Restaurante> restaurantesFavoritos;
    private ArrayList<String> preferencias;

    /**
     * Devuelve el usuario de la aplicación. Debe haberse creado anteriormente con
     * {@link #crearUsuario(String, String, Localizacion)} o devolverá null.
     * @return el usuario o null si no se creó.
     */
    public static Usuario getUsuario() {
        return instance;
    }

    /**
     * Crea el usaurio solo si no se ha creado (TODO: los credenciales se comprueban en el login).
     * @param email correo del usuario.
     * @param nombre nombre del usuario.
     * @param loc la localizacion del usuario actual.
     * @return el usuario creado en caso de no existir previamente o el usuario creado previamente
     * en caso de existir ya.
     */
    public static Usuario crearUsuario(String email, String nombre, Localizacion loc) {
        //Log.i("email", email);
        if(instance == null){
            String imagen = "";
            HashSet<Restaurante> favoritos = new HashSet<>();
            HashSet<Resenia> resenias = new HashSet<>();
            ArrayList<String> preferencias = new ArrayList<>();
            instance = new Usuario(email, nombre, loc, imagen, favoritos, resenias, preferencias);
        }
        return instance;
    }

    private Usuario(String email, String nombre, Localizacion localizacion,
                    String imagenDir, HashSet<Restaurante> favoritos,
                    HashSet<Resenia> resenias, ArrayList<String> preferencias) {
        this.email = email;
        this.nombre = nombre;
        this.localizacion = localizacion;
        this.imagenDir = imagenDir;
        this.resenias = resenias;
        this.restaurantesFavoritos = favoritos;
        this.preferencias = preferencias;
    }

    public String getEmail() { return email; }
    public String getNombre() { return nombre; }
    public Localizacion getLocalizacion() { return localizacion; }
    public String getImagenDir() { return imagenDir; }
    public HashSet<Resenia> getResenias() { return resenias; }
    public HashSet<Restaurante> getRestaurantesFavoritos() { return restaurantesFavoritos; }
    public ArrayList<String> getPreferencias() { return preferencias; }

    public void setNombre(String nombre) { this.nombre = nombre; }
    public void setPreferencias(ArrayList<String> preferencias) { this.preferencias = preferencias; }
    public void addRestauranteFavorito(Restaurante r){
        if(restaurantesFavoritos.size() < MAX_FAV)
            restaurantesFavoritos.add(r);
    }

    public void removeRestauranteFavorito(Restaurante r){ this.restaurantesFavoritos.remove(r); }
}
