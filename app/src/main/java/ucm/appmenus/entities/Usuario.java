package ucm.appmenus.entities;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;

import androidx.lifecycle.MutableLiveData;

import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import ucm.appmenus.MainActivity;
import ucm.appmenus.R;
import ucm.appmenus.login.LoginActivity;
import ucm.appmenus.utils.BaseDatos;
import ucm.appmenus.utils.Constantes;
import ucm.appmenus.utils.Localizacion;

public class Usuario {

    public static final int MAX_FAV = 5;
    private static Usuario instance = null;

    private final String idUsuario;
    private final String email;
    private MutableLiveData<String> nombre;
    private final Localizacion localizacion;
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
     * Abre la pantalla de login.
     */
    public static void cerrarSesion() {
        SharedPreferences sp = MainActivity.getInstance().getSharedPreferences(
                MainActivity.getInstance().getString(R.string.ucm_appmenus_ficherologin), Context.MODE_PRIVATE);
        sp.edit().clear().commit();

        instance = null;
        MainActivity.getInstance().startActivity(new Intent(MainActivity.getInstance(), LoginActivity.class));
        MainActivity.getInstance().finish();
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
            instance = new Usuario(idUsuario, email, loc, favoritos, resenias, preferencias);
        }
        return instance;
    }

    private Usuario(String idUsuario, String email, Localizacion localizacion,
                    HashSet<Restaurante> favoritos, HashSet<Resenia> resenias,
                    ArrayList<String> preferencias) {
        this.idUsuario = idUsuario;
        this.email = email;
        this.localizacion = localizacion;
        this.resenias = resenias;
        this.restaurantesFavoritos = favoritos;
        this.preferencias = preferencias;
        this.nombre = new MutableLiveData<>();

        BaseDatos.getInstance().setDatosUsuario();
    }

    public String getIdUsuario() { return idUsuario; }
    public String getEmail() { return email; }
    public MutableLiveData<String> getNombre() { return nombre; }
    public Localizacion getLocalizacion() { return localizacion; }
    public HashSet<Resenia> getResenias() { return resenias; }
    public HashSet<Restaurante> getRestaurantesFavoritos() { return restaurantesFavoritos; }
    public ArrayList<String> getPreferencias() { return preferencias; }
    public List<String> getRestaurantesFavoritosID() {
        List<String> res = new ArrayList<>();
        for(Restaurante r: restaurantesFavoritos){
            res.add(r.getIdRestaurante());
        }
        return res;
    }

    public void setNombre(String nombre) { this.nombre.postValue(nombre); }
    public void addPreferencias(String pref) { preferencias.add(pref); }
    public void addRestauranteFavorito(Restaurante r){ restaurantesFavoritos.add(r); }
    public void addResenia(Resenia r){ resenias.add(r); }

    public void removeRestauranteFavorito(Restaurante r){ this.restaurantesFavoritos.remove(r); }
    public void removeResenia(Resenia r) { this.resenias.remove(r); }

    public void borrarDatos(){
        restaurantesFavoritos.clear();
        resenias.clear();
    }
}
