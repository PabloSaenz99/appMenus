package ucm.appmenus.login;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import ucm.appmenus.MainActivity;
import ucm.appmenus.R;

public class LoginActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inicio_sesion);

        //Usado para comprobar si el usuario está ya logueado
        final SharedPreferences sp = this.getSharedPreferences(
                getString(R.string.ucm_appmenus_ficherologin), Context.MODE_PRIVATE);
        String usuario = sp.getString(getString(R.string.nombre_usuario), null);
        if(usuario == null){
            //Pide login
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