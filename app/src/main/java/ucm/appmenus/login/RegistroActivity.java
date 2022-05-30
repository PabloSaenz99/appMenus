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
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
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
//boton de ocultar/mostrar pwd
        ImageView imageViewShowHidePwd= findViewById(R.id.hidePwdRegister);
        imageViewShowHidePwd.setImageResource(R.drawable.ic_visibility);
        imageViewShowHidePwd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (et_password.getTransformationMethod().equals(HideReturnsTransformationMethod.getInstance())) {
                    //si pwd esta visible y lo ocultamos
                    et_password.setTransformationMethod(PasswordTransformationMethod.getInstance());

                    //cambiar icono
                    imageViewShowHidePwd.setImageResource(R.drawable.ic_visibility);
                } else {

                    et_password.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                    imageViewShowHidePwd.setImageResource(R.drawable.ic_visibility_no);
                }
            }
        });

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
                    if(nombre.length() > 20)
                        Toast.makeText(RegistroActivity.this, "El nombre no puede contener más de 20 caracteres.", Toast.LENGTH_SHORT).show();
                    else
                        registerUser(nombre, email, password);
                }
            }
        });

    }

    /**
     * Registra al usuario en la BD
     * @param nombre nombre del usaurio
     * @param email email del usaurio
     * @param password contraseña del usuario
     */
    private void registerUser(String nombre, String email, String password) {
        long ini = System.nanoTime();
        firebaseAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull @org.jetbrains.annotations.NotNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    FirebaseUser rUser = firebaseAuth.getCurrentUser();
                    String userId = rUser.getUid();
                    databaseReference = FirebaseDatabase.getInstance().getReference("Users").child(userId);
                    HashMap<String, String> hashMap = new HashMap<>();
                    hashMap.put("usuarioId", userId);
                    hashMap.put("usuarioNombre", nombre);
                    hashMap.put("usuarioEmail", email);
                    databaseReference.setValue(hashMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull @NotNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                //Usado para loguear automaticamente
                                SharedPreferences sp = getSharedPreferences(
                                        getString(R.string.ucm_appmenus_ficherologin), Context.MODE_PRIVATE);
                                SharedPreferences.Editor editor = sp.edit();
                                editor.putString(getString(R.string.email_usuario), email);
                                editor.putString(getString(R.string.password_usuario), password);
                                editor.commit();

                                //Abre la activity de login
                                Intent intent = new Intent(RegistroActivity.this, LoginActivity.class);
                                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                                startActivity(intent);
                                MainActivity.medirTiempo("Registro", ini, System.nanoTime());
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