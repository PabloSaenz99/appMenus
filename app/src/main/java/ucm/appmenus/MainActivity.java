package ucm.appmenus;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import ucm.appmenus.entities.Usuario;
import ucm.appmenus.utils.BaseDatos;
import ucm.appmenus.utils.Constantes;
import ucm.appmenus.utils.Localizacion;

/**
 * IMPORTANTE: Esta activity ya loguea al usuario desde SharedPreferences
 * */


public class MainActivity extends AppCompatActivity {

    //esto estaba de antes
    private Usuario usuario;
    private NavController navController;

    private static MainActivity instance;

    /**
     * En principio no hay que hacer nada mas en esta actividad ya que tod0 se hace en los fragments
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        instance = this;

        //Loguea al usuario para poder usar sus datos en caso de ser necesarios
        Bundle loginInfo = getIntent().getExtras();
        this.usuario = Usuario.crearUsuario(
                loginInfo.getString(Constantes.ID_USUARIO, "error"),
                loginInfo.getString(Constantes.EMAIL_USUARIO, "Error"),
                new Localizacion(this));

        //Importante que esté después del login de usuario o lanzará nullpointer
        //TODO: PONER AFTERLOGIN UNA VEZ
        //setContentView(R.layout.activity_afterlogin);
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

    public static void medirTiempo(String nombreFunc, long nanoIni, long nanoFin) {
        double res = (nanoFin - nanoIni)/1000000.0;
        Log.i("tiempo",  nombreFunc + ": " + res + "ms");
    }

    public static MainActivity getInstance(){
        return instance;
    }
 }





