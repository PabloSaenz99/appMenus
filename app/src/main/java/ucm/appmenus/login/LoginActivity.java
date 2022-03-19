package ucm.appmenus.login;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

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
        String email = sp.getString(getString(R.string.nombre_usuario), null);

        //Si el usuario ya está logueado, email será != null, por lo que abre la MainActivity directamente
        if(email != null){
            //Inicia la main activity
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
        }

        final Button botonRegistro = findViewById(R.id.botonRegistro);
        botonRegistro.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                //Abrir activity
                Intent intent = new Intent(botonRegistro.getContext(), RegistroActivity.class);
                startActivity(intent);
            }
        });

        final Button botonInicioSesion = findViewById(R.id.botonIniciarSesion);
        botonInicioSesion.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                //Buscar resultados
                TextView emailText = findViewById(R.id.emailLogin);
                TextView passwordText = findViewById(R.id.passwordLogin);

                //TODO: Mirar en la BD que sea correcto
                String emailUsuario = "AQUI BUSCO EN LA BD";
                String passwordUsuario = "AQUI BUSCO EN LA BD";
                String nombreUsuario = "AQUI BUSCO EN LA BD";
                String imagenUsuario = "AQUI BUSCO EN LA BD";

                if(emailText.getText().toString().equals(emailUsuario) &&
                        passwordText.getText().toString().equals(passwordUsuario)) {
                    //Guarda el login en SharedPreferences
                    SharedPreferences.Editor editor = sp.edit();
                    editor.putString(getString(R.string.email_usuario), emailUsuario);
                    editor.putString(getString(R.string.nombre_usuario), nombreUsuario);
                    editor.putString(getString(R.string.imagen_usuario), imagenUsuario);
                    editor.commit();

                    //Abrir activity
                    Intent intent = new Intent(botonInicioSesion.getContext(), MainActivity.class);
                    //Intent intent = new Intent(botonInicioSesion.getContext(), MapActivity.class);
                    startActivity(intent);
                }
            }
        });
    }
}