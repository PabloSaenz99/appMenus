package ucm.appmenus.login;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;


import android.content.Context;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;

import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import org.jetbrains.annotations.NotNull;

import android.widget.TextView;


import java.util.Objects;

import ucm.appmenus.MainActivity;
import ucm.appmenus.R;
import ucm.appmenus.utils.Constantes;

public class LoginActivity extends AppCompatActivity {

    Button btn_registrar, btn_login;
    EditText et_name, et_email, et_password;
    FirebaseAuth firebaseAuth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inicio_sesion);//debo poner la vista de login y me abra directamente el login


        //hACER LOGIN

        btn_registrar = findViewById(R.id.botonRegistro);
        btn_login = findViewById(R.id.botonIniciarSesion);
        et_email = findViewById(R.id.emailLogin);
        et_password = findViewById(R.id.passwordLogin);
        firebaseAuth = FirebaseAuth.getInstance();

        //accion en los botones
        btn_registrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                  startActivity(new Intent(LoginActivity.this, RegistroActivity.class));
            }
        });

        //btn login
        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String text_email = et_email.getText().toString();
                String text_password = et_password.getText().toString();
                if (TextUtils.isEmpty(text_email) || TextUtils.isEmpty(text_password)) {
                    Toast.makeText(LoginActivity.this, "All fields required", Toast.LENGTH_SHORT).show();
                } else {
                    login(text_email, text_password);
                }
            }
        });
    }

    //metodo login
    private void login (String text_email, String text_password){
        firebaseAuth.signInWithEmailAndPassword(text_email, text_password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull @NotNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                    //intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                    //TODO: get usaurio nombre
                    Bundle b = new Bundle();
                    b.putString(Constantes.EMAIL_USUARIO, text_email);
                    b.putString(Constantes.NOMBRE_USUARIO, "Usuario");
                    intent.putExtras(b);
                    startActivity(intent);
                    finish();
                } else {
                    Toast.makeText(LoginActivity.this, Objects.requireNonNull(task.getException()).getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}



/*

        //Usado para comprobar si el usuario está ya logueado
        final SharedPreferences sp = this.getSharedPreferences(
                getString(R.string.ucm_appmenus_ficherologin), Context.MODE_PRIVATE);
        String email = sp.getString(getString(R.string.email_usuario), null);

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

                if(emailText.getText().toString().equals(emailUsuario) &&
                        passwordText.getText().toString().equals(passwordUsuario)) {
                    //Guarda el login en SharedPreferences
                    SharedPreferences.Editor editor = sp.edit();
                    editor.putString(getString(R.string.email_usuario), emailUsuario);
                    editor.putString(getString(R.string.password_usuario), passwordUsuario);
                    editor.commit();

                    //Abrir activity
                    Intent intent = new Intent(botonInicioSesion.getContext(), MainActivity.class);
                    //Intent intent = new Intent(botonInicioSesion.getContext(), MapActivity.class);
                    startActivity(intent);
                }
            }
        });
    }

}*/




