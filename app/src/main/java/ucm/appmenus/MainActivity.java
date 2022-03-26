package ucm.appmenus;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import androidx.annotation.IdRes;
import androidx.annotation.LayoutRes;
import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import ucm.appmenus.entities.Usuario;
import ucm.appmenus.ficheros.JSONRestaurante;
import ucm.appmenus.login.LoginActivity;
import ucm.appmenus.recyclers.IReclycerElement;
import ucm.appmenus.recyclers.RecyclerAdapter;
import ucm.appmenus.utils.Constantes;
import ucm.appmenus.utils.Localizacion;

/**
 * IMPORTANTE: Esta activity ya loguea al usuario desde SharedPreferences
 * */


public class MainActivity extends AppCompatActivity {

    //esto estaba de antes
    private Usuario usuario;
    private NavController navController;

    /**
     * En principio no hay que hacer nada mas en esta actividad ya que tod0 se hace en los fragments
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //Loguea al usaurio para poder usar sus datos luego
        Bundle loginInfo = getIntent().getExtras();
        loginUsuario(loginInfo.getString(Constantes.EMAIL_USUARIO, "Error"),
                loginInfo.getString(Constantes.NOMBRE_USUARIO, "Error"));

        //Importante que esté después del login de usuario o lanzará nullpointer
        setContentView(R.layout.activity_afterlogin);
        setContentView(R.layout.activity_main);

        final BottomNavigationView navView = findViewById(R.id.nav_view);
        //Carga la vista de la barra inferior con las 3 ventanas que contiene
        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
                R.id.navigation_inicio, R.id.navigation_filtros, R.id.navigation_perfil)
                .build();

        navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        //NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
        NavigationUI.setupWithNavController(navView, navController);
    }

    /**
     * Cambia entre 2 fragments
     * @param id: fragment al que moverse
     * @param b: datos necesarios para dicho fragment
    */
    public void changeFragment(int id, Bundle b){
        Log.d("BUNDLE", b.toString());
        navController.navigate(id, b);
    }

    /**
     * Loguea al usuario
     * Obtiene el email y contraseña de SharedPreferences
     * TODO: Deberia comprobar en la BD que sea correcto(?)
     *
     * @param o
     * @param o1*/
    //Funcion de login local
    private void loginUsuario(String email, String nombre){
        /*final SharedPreferences sp = this.getSharedPreferences(
                getString(R.string.ucm_appmenus_ficherologin), Context.MODE_PRIVATE);
        String email = sp.getString(getString(R.string.email_usuario), "");
        String password = sp.getString(getString(R.string.password_usuario), "");*/
        this.usuario = Usuario.crearUsuario(email, nombre, new Localizacion(this));
    }
 }





