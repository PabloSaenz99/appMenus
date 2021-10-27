package ucm.appmenus;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import java.util.ArrayList;

import ucm.appmenus.login.RegistroActivity;
import ucm.appmenus.ui.filtros.FiltrosRecyclerAdapter;
import ucm.appmenus.ui.filtros.FiltrosRestauranteActivity;


public class MainActivity extends AppCompatActivity {

    //En principio no hay que hacer nada mas en esta actividad ya que tod0 se hace en los fragments
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        //ACCION BOTON DE LA PAGINA LOGIN

      final  Button botonRegistro = findViewById(R.id.boton_registro);
        botonRegistro.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                //Buscar resultados y abrir activity
                Intent intent = new Intent(botonRegistro.getContext(), RegistroActivity.class);
                startActivity(intent);
            }
        });
        /*
        BottomNavigationView navView = findViewById(R.id.nav_view);
        //Carga la vista de la barra inferior con las 3 ventanas que contiene
        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
                R.id.navigation_inicio, R.id.navigation_filtros, R.id.navigation_perfil)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
        NavigationUI.setupWithNavController(navView, navController);
        new Localizacion(this);
        */

    }

}