package ucm.appmenus.entities;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import ucm.appmenus.R;
import ucm.appmenus.utils.Localizacion;

public class Usuario {

    public static final int MAX_FAV = 5;
    private static Usuario instance = null;

    private final String idUsuario;
    private final String email;
    private String nombre;
    private final Localizacion localizacion;
    private final String imagenDir;
    private final HashSet<Resenia> resenias;
    private final HashSet<Restaurante> restaurantesFavoritos;
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
     * Cierra la sesion actual del usuario y borra el login guardado en el dispositivo.
     */
    public static void cerrarSesion(Activity activity) {
        instance = null;
        SharedPreferences sp = activity.getSharedPreferences(
                activity.getString(R.string.ucm_appmenus_ficherologin), Context.MODE_PRIVATE);
        sp.edit().clear().commit();
    }

    /**
     * Crea el usaurio solo si no se ha creado
     * @param email correo del usuario.
     * @param loc la localizacion del usuario actual.
     * @return el usuario creado en caso de no existir previamente o el usuario creado previamente
     * en caso de existir ya.
     */
    public static Usuario crearUsuario(String idUsuario, String email, Localizacion loc) {
        if(instance == null){
            String imagen = "";
            HashSet<Restaurante> favoritos = new HashSet<>();
            HashSet<Resenia> resenias = new HashSet<>();
            ArrayList<String> preferencias = new ArrayList<>();
            instance = new Usuario(idUsuario, email, loc, imagen, favoritos, resenias, preferencias);
        }
        return instance;
    }

    private Usuario(String idUsuario, String email, Localizacion localizacion,
                    String imagenDir, HashSet<Restaurante> favoritos,
                    HashSet<Resenia> resenias, ArrayList<String> preferencias) {
        this.idUsuario = idUsuario;
        this.email = email;
        this.localizacion = localizacion;
        this.imagenDir = imagenDir;
        this.resenias = resenias;
        this.restaurantesFavoritos = favoritos;
        this.preferencias = preferencias;
    }

    public String getIdUsuario() { return idUsuario; }
    public String getEmail() { return email; }
    public String getNombre() { return nombre; }
    public Localizacion getLocalizacion() { return localizacion; }
    public String getImagenDir() { return imagenDir; }
    public HashSet<Resenia> getResenias() { return resenias; }
    public HashSet<Restaurante> getRestaurantesFavoritos() { return restaurantesFavoritos; }
    public ArrayList<String> getPreferencias() { return preferencias; }

    public void setNombre(String nombre) { this.nombre = nombre; }
    public void addPreferencias(String pref) { preferencias.add(pref); }
    public void addRestauranteFavorito(Restaurante r){ restaurantesFavoritos.add(r); }
    public void addResenia(Resenia r){ resenias.add(r); }

    public void removeRestauranteFavorito(Restaurante r){ this.restaurantesFavoritos.remove(r); }

}
