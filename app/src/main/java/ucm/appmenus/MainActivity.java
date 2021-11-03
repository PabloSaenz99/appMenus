package ucm.appmenus;

import android.content.Context;
import android.os.Bundle;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import org.json.JSONException;

import java.io.FileNotFoundException;
import java.io.IOException;

import ucm.appmenus.utils.JSONReader;


public class MainActivity extends AppCompatActivity {

    //En principio no hay que hacer nada mas en esta actividad ya que tod0 se hace en los fragments
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        BottomNavigationView navView = findViewById(R.id.nav_view);
        //Carga la vista de la barra inferior con las 3 ventanas que contiene
        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
                R.id.navigation_inicio, R.id.navigation_filtros, R.id.navigation_perfil)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
        NavigationUI.setupWithNavController(navView, navController);
        //new Localizacion(this);
        JSONReader r = null;
        try {
            //data/user/0/ucm.appmenus/files/ejemplomenus.json
            r = new JSONReader(getApplicationContext(),"ejemplomenus.json");
            r.parsePlaceDetailsJSON();
        } catch (JSONException | IOException e) {
            e.printStackTrace();
        }
    }

}