package ucm.appmenus;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import ucm.appmenus.entities.Usuario;
import ucm.appmenus.ficheros.JSONRestaurante;
import ucm.appmenus.utils.Localizacion;

/**
 * IMPORTANTE: Esta activity ya loguea al usuario desde SharedPreferences
 * */
public class MainActivity extends AppCompatActivity {

    private Usuario usuario;
    private NavController navController;

    //En principio no hay que hacer nada mas en esta actividad ya que tod0 se hace en los fragments
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        loginUsuario();

        //Importante que esté después del login de usuario o lanzará nullpointer
        setContentView(R.layout.activity_main);
        final BottomNavigationView navView = findViewById(R.id.nav_view);
        //Carga la vista de la barra inferior con las 3 ventanas que contiene
        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
                R.id.navigation_inicio, R.id.navigation_filtros, R.id.navigation_perfil)
                .build();

        navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
        NavigationUI.setupWithNavController(navView, navController);
    }

    public void changeFragment(int id, Bundle b){
        Log.d("BUNDLE", b.toString());
        navController.navigate(id, b);
    }

    public Usuario getUsuario() { return usuario; }

    /**
     * Loguea al usuario
     * Obtiene el email, nombre e imagen de SharedPreferences
     * TODO: Deberia comprobar en la BD que sea correcto(?)
     * */
    private void loginUsuario(){
        final SharedPreferences sp = this.getSharedPreferences(
                getString(R.string.ucm_appmenus_ficherologin), Context.MODE_PRIVATE);
        String email = sp.getString(getString(R.string.email_usuario), null);
        String nombre = sp.getString(getString(R.string.nombre_usuario), null);
        String imagen = sp.getString(getString(R.string.imagen_usuario), null);
        JSONRestaurante jsonRes = new JSONRestaurante(getApplicationContext(),
                getString(R.string.ucm_appmenus_restaurantesFavoritos),
                getString(R.string.ucm_appmenus_restaurantesFavoritos));
        this.usuario = new Usuario(email, nombre, new Localizacion(this), imagen,
                null, jsonRes.readRestaurantesJSON(), null);

    }
}