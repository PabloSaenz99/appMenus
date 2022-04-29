package ucm.appmenus.login;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;


import android.content.Context;

import android.content.Intent;
import android.content.SharedPreferences;
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
import ucm.appmenus.utils.BaseDatos;
import ucm.appmenus.utils.Constantes;

public class LoginActivity extends AppCompatActivity {

    private EditText et_email, et_password;
    private FirebaseAuth firebaseAuth;
    private SharedPreferences sp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inicio_sesion);

        Button btn_registrar = findViewById(R.id.botonRegistro);
        Button btn_login = findViewById(R.id.botonIniciarSesion);
        Button btn_acceso_facil = findViewById(R.id.accesoFacil);
        et_email = findViewById(R.id.emailLogin);
        et_password = findViewById(R.id.passwordLogin);
        firebaseAuth = FirebaseAuth.getInstance();

        //Usado para comprobar si el usuario está ya logueado
        sp = this.getSharedPreferences(
                getString(R.string.ucm_appmenus_ficherologin), Context.MODE_PRIVATE);
        String email = sp.getString(getString(R.string.email_usuario), null);
        String password = sp.getString(getString(R.string.password_usuario), null);

        //Si el usuario ya está logueado, email será != null, por lo que abre la MainActivity directamente
        if (email != null && !email.equals(Constantes.EMAIL_INVITADO)){
            //Inicia la main activity
            login(email, password);
        }

        //aBoton registro
        btn_registrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                  startActivity(new Intent(LoginActivity.this, RegistroActivity.class));
            }
        });

        //Boton Login
        btn_login.setOnClickListener(view -> loginButton());
        //Boton acceso facil
        btn_acceso_facil.setOnClickListener(v -> accesoFacil());
    }

    private void accesoFacil(){
        String text_email = Constantes.EMAIL_INVITADO;
        String text_password = Constantes.PASSWORD_INVITADO;
        login(text_email, text_password);
    }


    private void loginButton(){
        String text_email = et_email.getText().toString();
        String text_password = et_password.getText().toString();
        if (TextUtils.isEmpty(text_email) || TextUtils.isEmpty(text_password)) {
            Toast.makeText(LoginActivity.this, "All fields required", Toast.LENGTH_SHORT).show();
        } else {
            login(text_email, text_password);
        }
    }

    /**
     * Loguea al usuario en la BD
     * @param text_email email del usuario
     * @param text_password contraseña del usuario
     */
    private void login (String text_email, String text_password){
        firebaseAuth.signInWithEmailAndPassword(text_email, text_password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull @NotNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    //Parametros para hacer login en la main activity
                    Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                    Bundle b = new Bundle();
                    b.putString(Constantes.ID_USUARIO, firebaseAuth.getCurrentUser().getUid());
                    b.putString(Constantes.EMAIL_USUARIO, text_email);
                    intent.putExtras(b);

                    //Guarda el login actual de forma local
                    SharedPreferences.Editor editor = sp.edit();
                    editor.putString(getString(R.string.email_usuario), text_email);
                    editor.putString(getString(R.string.password_usuario), text_password);
                    editor.commit();

                    //Inicia la mainActiviry
                    startActivity(intent);
                    finish();
                } else {
                    Toast.makeText(LoginActivity.this, Objects.requireNonNull(task.getException()).getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}