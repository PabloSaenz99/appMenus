package ucm.appmenus.ui;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import ucm.appmenus.R;

public class SettingsActivity extends AppCompatActivity {


    private AlertDialog.Builder dialogBuilder;
    private AlertDialog dialog;
    private EditText oldPassword, newPassword, newPasswordConfirm;
    private Button changePassword;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_settings);

        //https://www.youtube.com/watch?v=4GYKOzgQDWI


    }


    public void changePassword(){
        dialogBuilder = new AlertDialog.Builder(this);
        final View changePasswordView = getLayoutInflater().inflate(R.layout.popupPassword, null);
    }



}
