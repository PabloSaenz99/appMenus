package ucm.appmenus.ui;

import android.content.Intent;
import android.os.Bundle;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import ucm.appmenus.MainActivity;
import ucm.appmenus.R;
import ucm.appmenus.entities.Usuario;
import ucm.appmenus.login.LoginActivity;
import ucm.appmenus.utils.BaseDatos;

public class SettingsActivity extends AppCompatActivity {

    private int index;
    private Usuario usuario;

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

        usuario = Usuario.getUsuario();

        Button botonCerrarSesion = findViewById(R.id.logout);
        Button botonCambiarContrasena = findViewById(R.id.changePassword);
        Button botonCambiarNombre = findViewById(R.id.changeName);
        Button botonCambiarLocalizacion = findViewById(R.id.changeLocation);
        Button botonRestablecerLocalizacion = findViewById(R.id.restoreLocation);
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

        botonRestablecerLocalizacion.setOnClickListener(view -> {
            Usuario.getUsuario().getLocalizacion().actualizar = true;
            Usuario.getUsuario().getLocalizacion().actualizarLocalizacion();
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
            Usuario.cerrarSesion();
        });
    }


    private void changePassword() {
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
            if(oldPassword.getText().toString().equals("") || newPassword.getText().toString().equals("") || newPasswordConfirm.getText().toString().equals("")){
                Toast.makeText(view.getContext(), "Hay algún campo vacío", Toast.LENGTH_LONG).show();
            }else if(oldPassword.getText().toString().equals(newPassword.getText().toString())) {
                Toast.makeText(view.getContext(), "La nueva contraseña no puede ser igual a la antigua", Toast.LENGTH_LONG).show();
            }else if(!newPasswordConfirm.getText().toString().equals(newPassword.getText().toString())){
                Toast.makeText(view.getContext(), "Las contraseñas no coinciden", Toast.LENGTH_LONG).show();
            }else{
                BaseDatos.getInstance().cambiarPassword(newPassword.getText().toString(), MainActivity.getInstance());
                dialog.dismiss();
            }

        });


        ImageView imageViewShowHidePwd = changePasswordView.findViewById(R.id.hidePwd);
        imageViewShowHidePwd.setImageResource(R.drawable.ic_visibility);
        imageViewShowHidePwd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (newPassword.getTransformationMethod().equals(HideReturnsTransformationMethod.getInstance())) {
                    newPassword.setTransformationMethod(PasswordTransformationMethod.getInstance());
                    oldPassword.setTransformationMethod(PasswordTransformationMethod.getInstance());
                    newPasswordConfirm.setTransformationMethod(PasswordTransformationMethod.getInstance());

                    imageViewShowHidePwd.setImageResource(R.drawable.ic_visibility);
                } else {

                    newPassword.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                    oldPassword.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                    newPasswordConfirm.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                    imageViewShowHidePwd.setImageResource(R.drawable.ic_visibility_no);
                }
            }
        });


    }


    private void changeName() {
        dialogBuilder = new AlertDialog.Builder(this);
        final View changeNameView = getLayoutInflater().inflate(R.layout.pop_up_name, null);
        newName = (EditText) changeNameView.findViewById(R.id.new_name);

        changeNameButton = (Button) changeNameView.findViewById(R.id.change_name_button);

        dialogBuilder.setView(changeNameView);
        dialog = dialogBuilder.create();
        dialog.show();

        changeNameButton.setOnClickListener(view -> {
            if (newName.getText().toString().equals("")) {
                Toast.makeText(view.getContext(), "Tienes que poner un nombre.",
                        Toast.LENGTH_LONG).show();
            } else if (newName.getText().toString().matches("\\d+")) {
                Toast.makeText(view.getContext(), "El nombre no puede tener números.",
                        Toast.LENGTH_LONG).show();
            } else if(newName.length() > 20){
                Toast.makeText(view.getContext(), "El nombre no puede tener más de 20 caracteres.", Toast.LENGTH_SHORT).show();
            } else {
                BaseDatos.getInstance().cambiarNombreUsuario(newName.getText().toString(), MainActivity.getInstance());
                dialog.dismiss();
            }
        });

    }

    private void delete() {

        dialogBuilder = new AlertDialog.Builder(this);
        final View deleteView = getLayoutInflater().inflate(R.layout.pop_up_delete, null);
        textoCompletar = (TextView) deleteView.findViewById(R.id.text_completar);

        if (index == 0) {
            textoCompletar.setText("borrar los datos?");
        } else {
            textoCompletar.setText("borrar la cuenta?");
        }

        deleteButton = (Button) deleteView.findViewById(R.id.delete_button);

        dialogBuilder.setView(deleteView);
        dialog = dialogBuilder.create();
        dialog.show();

        deleteButton.setOnClickListener(view -> {

            if (index == 0) {   //Ha elegido borrar los datos
                BaseDatos.getInstance().borrarDatos(MainActivity.getInstance());
            } else {            //Ha elegido borrar la cuenta
                BaseDatos.getInstance().borrarCuenta(MainActivity.getInstance());
            }
            dialog.dismiss();

        });

    }


    private void changeLocation() {
        dialogBuilder = new AlertDialog.Builder(this);
        final View changeLocationView = getLayoutInflater().inflate(R.layout.pop_up_location, null);
        latitude = (EditText) changeLocationView.findViewById(R.id.latitude);
        longitude = (EditText) changeLocationView.findViewById(R.id.longitude);

        changeLocationButton = (Button) changeLocationView.findViewById(R.id.change_location_button);

        dialogBuilder.setView(changeLocationView);
        dialog = dialogBuilder.create();
        dialog.show();

        changeLocationButton.setOnClickListener(view -> {
            if (latitude.getText().toString().equals("") || longitude.getText().toString().equals("")) {
                Toast.makeText(view.getContext(), "Hay algún campo vacío", Toast.LENGTH_LONG).show();

            } else if (!latitude.getText().toString().matches("^-*[0-9]+\\.[0-9]+$") //Reges antiguo: ^[0-9,.]+$
                    || !longitude.getText().toString().matches("^-*[0-9]+\\.[0-9]+$")) {
                Toast.makeText(view.getContext(), "No es una localización válida", Toast.LENGTH_LONG).show();
            } else {
                Usuario.getUsuario().getLocalizacion().actualizar = false;
                Usuario.getUsuario().getLocalizacion().latitude = Double.parseDouble(latitude.getText().toString());
                Usuario.getUsuario().getLocalizacion().longitude = Double.parseDouble(longitude.getText().toString());
                Toast.makeText(view.getContext(), "Localización cambiada a " +
                        latitude.getText().toString() + " " + longitude.getText().toString(), Toast.LENGTH_LONG).show();
                dialog.dismiss();
            }
        });

    }

}
