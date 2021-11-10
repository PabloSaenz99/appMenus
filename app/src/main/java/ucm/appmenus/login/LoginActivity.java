package ucm.appmenus.login;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import java.util.ArrayList;

import ucm.appmenus.MainActivity;
import ucm.appmenus.R;
import ucm.appmenus.Restaurante;
import ucm.appmenus.Usuario;
import ucm.appmenus.ficheros.JSONRestaurante;
import ucm.appmenus.utils.Localizacion;
import ucm.appmenus.utils.Pair;

public class LoginActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inicio_sesion);

        //Usado para comprobar si el usuario está ya logueado
        final SharedPreferences sp = this.getSharedPreferences(
                getString(R.string.ucm_appmenus_ficherologin), Context.MODE_PRIVATE);
        String email = sp.getString(getString(R.string.nombre_usuario), null);
        if(email == null){
            //Pide login
            //IMPORTANTE: a parte de email, password etc, tiene que mostrar la lista de filtros para
            //que elija 3 y usarlos por defecto en las busquedas
            /*
            //Usado para guardar el login en un archivo y no pedirlo cada vez que se entra en la app
            SharedPreferences.Editor editor = sp.edit();
            editor.putString(getString(R.string.nombre_usuario), "Pablo Saenz");
            editor.commit();
            */
        }
        else{
            //Inicia la main activity
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
        }

        final Button botonRegistro = findViewById(R.id.botonRegistro);
        botonRegistro.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                //Buscar resultados

                //Abrir activity
                Intent intent = new Intent(botonRegistro.getContext(), RegistroActivity.class);
                startActivity(intent);
            }
        });

        final Button botonInicioSesion = findViewById(R.id.botonInicioSesion);
        botonInicioSesion.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                //Buscar resultados

                //Abrir activity
                Intent intent = new Intent(botonInicioSesion.getContext(), MainActivity.class);
                startActivity(intent);
            }
        });
    }
}