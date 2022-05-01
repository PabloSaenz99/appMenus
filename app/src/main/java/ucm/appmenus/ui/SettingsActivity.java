package ucm.appmenus.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import ucm.appmenus.R;
import ucm.appmenus.login.LoginActivity;

public class SettingsActivity extends AppCompatActivity {

    private int index;

    private AlertDialog.Builder dialogBuilder;
    private AlertDialog dialog;
    private EditText oldPassword, newPassword, newPasswordConfirm;
    private EditText newName;
    private EditText latitude, longitude;
    private TextView textoCompletar;
    private Button changePasswordButton, changeNameButton, deleteButton, changeLocationButton;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_settings);

        Button botonCerrarSesion = findViewById(R.id.logout);
        Button botonCambiarContrasena = findViewById(R.id.changePassword);
        Button botonCambiarNombre = findViewById(R.id.changeName);
        Button botonCambiarLocalizacion = findViewById(R.id.changeLocation);
        Button botonBorrarDatos = findViewById(R.id.deleteData);
        Button botonBorrarCuenta = findViewById(R.id.deleteAccount);

        //https://www.youtube.com/watch?v=4GYKOzgQDWI

        botonCambiarContrasena.setOnClickListener(view -> {
            changePassword();
        });

        botonCambiarNombre.setOnClickListener(view -> {
            changeName();
        });

        botonCambiarLocalizacion.setOnClickListener(view -> {
            changeLocation();
        });

        botonBorrarDatos.setOnClickListener(view -> {
            index = 0;
            delete();
        });

        botonBorrarCuenta.setOnClickListener(view -> {
            index = 1;
            delete();
        });

        botonCerrarSesion.setOnClickListener(view -> {
            startActivity(new Intent(this, LoginActivity.class));
            finish();
        });


    }


    private void changePassword(){
        dialogBuilder = new AlertDialog.Builder(this);
        final View changePasswordView = getLayoutInflater().inflate(R.layout.pop_up_password, null);
        oldPassword = (EditText) changePasswordView.findViewById(R.id.old_password);
        newPassword = (EditText) changePasswordView.findViewById(R.id.new_password);
        newPasswordConfirm = (EditText) changePasswordView.findViewById(R.id.confirm_new_password);

        changePasswordButton = (Button) changePasswordView.findViewById(R.id.change_password_button);

        dialogBuilder.setView(changePasswordView);
        dialog = dialogBuilder.create();
        dialog.show();

        changePasswordButton.setOnClickListener(view -> {
            //TODO
        });

    }


    private void changeName(){
        dialogBuilder = new AlertDialog.Builder(this);
        final View changeNameView = getLayoutInflater().inflate(R.layout.pop_up_name, null);
        newName = (EditText) changeNameView.findViewById(R.id.new_name);

        changeNameButton = (Button) changeNameView.findViewById(R.id.change_name_button);

        dialogBuilder.setView(changeNameView);
        dialog = dialogBuilder.create();
        dialog.show();

        changeNameButton.setOnClickListener(view -> {
            //TODO
        });

    }

    private void delete(){

        dialogBuilder = new AlertDialog.Builder(this);
        final View deleteView = getLayoutInflater().inflate(R.layout.pop_up_delete, null);
        textoCompletar = (TextView) deleteView.findViewById(R.id.text_completar);

        if(index == 0){
            textoCompletar.setText("borrar los datos?");
        }else{
            textoCompletar.setText("borrar la cuenta?");
        }

        deleteButton = (Button) deleteView.findViewById(R.id.change_name_button);

        dialogBuilder.setView(deleteView);
        dialog = dialogBuilder.create();
        dialog.show();

        changeNameButton.setOnClickListener(view -> {

            //TODO
            if(index == 0){



            }else{




            }
        });

    }


    private void changeLocation(){

        dialogBuilder = new AlertDialog.Builder(this);
        final View changeLocationView = getLayoutInflater().inflate(R.layout.pop_up_location, null);
        latitude = (EditText) changeLocationView.findViewById(R.id.latitude);
        longitude = (EditText) changeLocationView.findViewById(R.id.longitude);

        changeLocationButton = (Button) changeLocationView.findViewById(R.id.change_location_button);

        dialogBuilder.setView(changeLocationView);
        dialog = dialogBuilder.create();
        dialog.show();

        changePasswordButton.setOnClickListener(view -> {
            //TODO
        });

    }

}
