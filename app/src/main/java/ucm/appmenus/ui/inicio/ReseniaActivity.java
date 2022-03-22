package ucm.appmenus.ui.inicio;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RatingBar;

import ucm.appmenus.R;
import ucm.appmenus.entities.Resenia;
import ucm.appmenus.entities.Usuario;

/**
 * Clase utilizada para crear la reseña de un restaurante
 */
public class ReseniaActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_resenia);

        EditText titulo = findViewById(R.id.tituloResenia);
        EditText descripcion = findViewById(R.id.descripcionResenia);
        RatingBar valoracion = findViewById(R.id.valoracionResenia);
        findViewById(R.id.usuarioResenia).setVisibility(View.INVISIBLE);

        Button crearResenia = findViewById(R.id.botonCrearResenia);
        crearResenia.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                /*//TODO: En vez de esto, guardarla en la BD y en vez de poner USUARIO poner el nombre del usuario
                Resenia r = new Resenia("", Usuario.getInstance().getEmail(), Usuario.getInstance().getNombre(),
                        titulo.getText().toString(), descripcion.getText().toString(), valoracion.getRating());
                Intent intent = new Intent(view.getContext(), RestauranteDetalladoActivity.class);
                intent.putExtra("resenia", r);
                 */
                finish();
            }
        });
    }
}