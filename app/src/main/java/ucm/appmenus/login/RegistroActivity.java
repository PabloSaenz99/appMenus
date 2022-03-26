package ucm.appmenus.login;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import ucm.appmenus.MainActivity;
import ucm.appmenus.R;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Objects;

public class RegistroActivity extends AppCompatActivity {

    //vars
    private FirebaseAuth firebaseAuth;
    private DatabaseReference databaseReference;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registro);

        //vars
        firebaseAuth = FirebaseAuth.getInstance();
        final EditText et_name = findViewById(R.id.nombreRegistro);
        final EditText et_email = findViewById(R.id.emailRegistro);
        final EditText et_password = findViewById(R.id.passwordRegistro);
        final Button botonRegistro = findViewById(R.id.botonRegistro);

        //Llamada cuando se pulse al boton registrar (llamada a la BD)
        botonRegistro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String nombre = et_name.getText().toString();
                String email = et_email.getText().toString();
                String password = et_password.getText().toString();

                if (TextUtils.isEmpty(nombre) || TextUtils.isEmpty(email) || TextUtils.isEmpty(password)) {
                    Toast.makeText(RegistroActivity.this, "todos los campos son requeridos", Toast.LENGTH_SHORT).show();
                } else {
                    registerUser(nombre, email, password);
                }
            }
        });

    }

    public void registerUser(String nombre, String email, String password) {
        firebaseAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull @org.jetbrains.annotations.NotNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    FirebaseUser rUser = firebaseAuth.getCurrentUser();
                    String userId = rUser.getUid();
                    databaseReference = FirebaseDatabase.getInstance().getReference("Users").child(userId);
                    HashMap<String, String> hashMap = new HashMap<>();
                    hashMap.put("userId", userId);
                    hashMap.put("userName", nombre);
                    hashMap.put("userEmail", email);
                    hashMap.put("userPassword", password);
                    databaseReference.setValue(hashMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull @NotNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                Intent intent = new Intent(RegistroActivity.this, LoginActivity.class);
                                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                                startActivity(intent);
                            } else {
                                Toast.makeText(RegistroActivity.this, Objects.requireNonNull(task.getException()).getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                } else {
                    Toast.makeText(RegistroActivity.this, Objects.requireNonNull(task.getException()).getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}






/*
        final SharedPreferences sp = this.getSharedPreferences(
                getString(R.string.ucm_appmenus_ficherologin), Context.MODE_PRIVATE);

        final Button botonInicioSesion = findViewById(R.id.botonIniciarSesion);
        botonInicioSesion.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                //Abrir activity
                Intent intent = new Intent(botonInicioSesion.getContext(), LoginActivity.class);
                startActivity(intent);
            }
        });

        botonRegistro.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                //Buscar resultados
                TextView email = findViewById(R.id.emailRegistro);
                TextView password = findViewById(R.id.passwordRegistro);
                TextView nombre = findViewById(R.id.nombreRegistro);

                //TODO: Mirar en la BD que no exista
                //Busca en la BD si no existe


                if(true){
                    //Guarda el login en SharedPreferences
                    SharedPreferences.Editor editor = sp.edit();
                    editor.putString(getString(R.string.email_usuario), email.getText().toString());
                    editor.putString(getString(R.string.password_usuario), password.getText().toString());
                    editor.commit();

                    //Abrir activity
                    Intent intent = new Intent(botonRegistro.getContext(), MainActivity.class);
                    startActivity(intent);
                }
            }
        });
    }*/

