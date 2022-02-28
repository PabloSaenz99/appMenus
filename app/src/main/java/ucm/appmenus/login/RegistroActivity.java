package ucm.appmenus.login;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import ucm.appmenus.MainActivity;
import ucm.appmenus.R;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;

public class RegistroActivity extends AppCompatActivity {
    Button btn_registrar;
    EditText et_email,et_password;

    FirebaseAuth firebaseAuth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registro);

        firebaseAuth=FirebaseAuth.getInstance();
        et_email=findViewById(R.id.textEmailRegistro);
        et_password=findViewById(R.id.textPasswordRegistro);


        btn_registrar=findViewById(R.id.botonRegistro);
btn_registrar.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View view) {
        String email = et_email.getText().toString();
        String password = et_password.getText().toString();

        firebaseAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
if(task.isSuccessful()){
    Toast.makeText(RegistroActivity.this,"usuario creado con exito", Toast.LENGTH_SHORT).show();
finish();
}else{
    String errorCode=((FirebaseAuthException) task.getException()).getErrorCode();
}
            }
        });
    }

});

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