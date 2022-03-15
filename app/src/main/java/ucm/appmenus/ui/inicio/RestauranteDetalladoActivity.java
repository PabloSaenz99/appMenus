package ucm.appmenus.ui.inicio;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import java.util.HashSet;

import ucm.appmenus.R;
import ucm.appmenus.entities.Restaurante;
import ucm.appmenus.recyclers.RecyclerAdapter;
import ucm.appmenus.recyclers.ViewHolderFiltros;
import ucm.appmenus.ui.filtros.FiltrosFragment;
import ucm.appmenus.utils.Pair;

public class RestauranteDetalladoActivity extends AppCompatActivity {

    private Restaurante restaurante;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_restaurante_detallado);

        restaurante = getIntent().getParcelableExtra("restaurante");

        ImageView imagen = findViewById(R.id.imagenPrincipalRestaurante);
        TextView nombre = findViewById(R.id.nombreRestaurante);
        RatingBar valoracion = findViewById(R.id.valoracionRestaurante);
        TextView url = findViewById(R.id.webRestaurante);
        TextView direccion = findViewById(R.id.direccionRestaurante);
        TextView telefono = findViewById(R.id.telefonoRestaurante);
        TextView horario = findViewById(R.id.horarioRestaurante);
        RecyclerView filtrosRecycler = findViewById(R.id.filtrosRestauranteRecycler);
        Button botonResenia = findViewById(R.id.botonAniadirResenia);

        nombre.setText(restaurante.getNombre());
        url.setText(restaurante.getStringURL());
        //imagen.setImageBitmap(restaurante.getListaImagenes().get(0));
        valoracion.setRating((float )restaurante.getValoracion());
        valoracion.setClickable(false);
        direccion.setText(restaurante.getDireccion().getValue());
        telefono.setText(restaurante.getTelefono() + "");
        horario.setText(restaurante.getHorarios());

        restaurante.updateImagenes();
        restaurante.updateFiltros();

        final Observer<HashSet<String>> observerFiltros = new Observer<HashSet<String>>() {
            @Override
            public void onChanged(HashSet<String> filtros) {
                //Recycler filtros
                filtrosRecycler.setLayoutManager(new GridLayoutManager(getApplicationContext(), 3));
                RecyclerAdapter<ViewHolderFiltros, Pair<String, Boolean>> adapterFiltros = new RecyclerAdapter<>(
                        FiltrosFragment.transform(filtros, false),
                        R.layout.recycler_filtros, ViewHolderFiltros.class);
                filtrosRecycler.setAdapter(adapterFiltros);
            }
        };
        restaurante.getLivedataFiltros().observe(this, observerFiltros);

        botonResenia.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), ReseniaActivity.class);
                startActivity(intent);
            }
        });
    }
}