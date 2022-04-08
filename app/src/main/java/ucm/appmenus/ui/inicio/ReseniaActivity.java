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
import ucm.appmenus.utils.BaseDatos;
import ucm.appmenus.utils.Constantes;

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
        crearResenia.setOnClickListener(view -> {
            Resenia r = new Resenia(getIntent().getExtras().getString(Constantes.RESTAURANTE),
                    Usuario.getUsuario().getIdUsuario(), Usuario.getUsuario().getNombre(),
                    titulo.getText().toString(), descripcion.getText().toString(), valoracion.getRating());
            Intent intent = new Intent(view.getContext(), RestauranteDetalladoActivity.class);
            intent.putExtra("resenia", r);
            BaseDatos.getInstance().addResenia(r);
            finish();
        });
    }
}