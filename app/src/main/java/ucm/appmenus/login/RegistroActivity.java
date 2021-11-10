package ucm.appmenus.login;

import androidx.appcompat.app.AppCompatActivity;

import ucm.appmenus.MainActivity;
import ucm.appmenus.R;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class RegistroActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registro);

        final SharedPreferences sp = this.getSharedPreferences(
                getString(R.string.ucm_appmenus_ficherologin), Context.MODE_PRIVATE);

        final Button botonInicioSesion = findViewById(R.id.botonInicioSesion);
        botonInicioSesion.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                //Abrir activity
                Intent intent = new Intent(botonInicioSesion.getContext(), LoginActivity.class);
                startActivity(intent);
            }
        });

        final Button botonRegistro = findViewById(R.id.botonRegistro);
        botonRegistro.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                //Buscar resultados
                TextView email = findViewById(R.id.textEmailRegistro);
                TextView password = findViewById(R.id.textPasswordRegistro);
                TextView nombre = findViewById(R.id.textNombreRegistro);

                //TODO: Mirar en la BD que no exista
                //Busca en la BD si no existe


                if(true/*No existe*/){
                    //Guarda el login en SharedPreferences
                    SharedPreferences.Editor editor = sp.edit();
                    editor.putString(getString(R.string.email_usuario), email.getText().toString());
                    editor.putString(getString(R.string.nombre_usuario), nombre.getText().toString());
                    //editor.putString(getString(R.string.imagen_usuario), imagenUsuario);
                    editor.commit();

                    //Abrir activity
                    Intent intent = new Intent(botonRegistro.getContext(), MainActivity.class);
                    startActivity(intent);
                }
            }
        });
    }
}