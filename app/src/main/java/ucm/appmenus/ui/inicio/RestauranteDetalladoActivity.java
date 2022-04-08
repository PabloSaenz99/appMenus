package ucm.appmenus.ui.inicio;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RatingBar;
import android.widget.TextView;

import java.util.List;
import java.util.Set;

import ucm.appmenus.R;
import ucm.appmenus.entities.Resenia;
import ucm.appmenus.entities.Restaurante;
import ucm.appmenus.recyclers.RecyclerAdapter;
import ucm.appmenus.recyclers.ViewHolderFiltros;
import ucm.appmenus.recyclers.ViewHolderImagenes;
import ucm.appmenus.recyclers.ViewHolderResenia;
import ucm.appmenus.ui.filtros.FiltrosFragment;
import ucm.appmenus.utils.Constantes;
import ucm.appmenus.utils.Pair;

/**
 * Clase utilizada para mostar los detalles de un restaurante, por ejemplo con mas imagenes, filtros o reseñas
 */
public class RestauranteDetalladoActivity extends AppCompatActivity {

    private Restaurante restaurante;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_restaurante_detallado);

        restaurante = getIntent().getParcelableExtra(Constantes.RESTAURANTE);

        TextView nombre = findViewById(R.id.nombreRestaurante);
        RatingBar valoracion = findViewById(R.id.valoracionRestaurante);//TODO: BUSACR EN LA BD
        TextView url = findViewById(R.id.webRestaurante);
        TextView direccion = findViewById(R.id.direccionRestaurante);
        TextView telefono = findViewById(R.id.telefonoRestaurante);
        TextView horario = findViewById(R.id.horarioRestaurante);
        Button botonResenia = findViewById(R.id.botonAniadirResenia);
        Button botonFiltros = findViewById(R.id.botonAniadirFiltros);

        nombre.setText(restaurante.getNombre());
        url.setText(restaurante.getStringURL());
        //imagen.setImageBitmap(restaurante.getListaImagenes().get(0));
        valoracion.setRating((float )restaurante.getValoracion());
        valoracion.setClickable(false);
        direccion.setText(restaurante.getLiveDataDireccion().getValue());
        if(restaurante.getTelefono() == 0)
            telefono.setText("No phone number");
        else
            telefono.setText(restaurante.getTelefono() + "");
        horario.setText(restaurante.getHorarios());

        //Actaualiza las imagenes, filtros y reseñas de los restaurantes para tener mas detalles.
        restaurante.updateImagenes();
        restaurante.updateFiltros();
        restaurante.updateResenias();

        View v = getWindow().getDecorView().getRootView();
        //Recycler filtros
        final Observer<Set<String>> observerFiltros = filtros ->
                RecyclerAdapter.crearRecyclerGrid(FiltrosFragment.transform(filtros, false), ViewHolderFiltros.class,
                        R.id.filtrosRestauranteRecycler, R.layout.recycler_filtros, v, 3);
        restaurante.getLivedataFiltros().observe(this, observerFiltros);

        //Recycler filtros
        final Observer<Set<String>> observerFiltrosBD = filtros -> {
            RecyclerAdapter.crearRecyclerGrid(FiltrosFragment.transform(filtros, false), ViewHolderFiltros.class,
                    R.id.filtrosBDRestauranteRecycler, R.layout.recycler_filtros, v, 3);
            if(filtros.isEmpty()) {
                findViewById(R.id.infoFiltrosBD).setVisibility(View.INVISIBLE);
            }
            else {
                findViewById(R.id.infoFiltrosBD).setVisibility(View.VISIBLE);
            }
        };
        restaurante.getLivedataFiltrosBD().observe(this, observerFiltrosBD);

        //Recycler imagenes
        final Observer<List<Bitmap>> observerImagenes = img ->
                RecyclerAdapter.crearRecyclerLineal(img, ViewHolderImagenes.class, R.id.recyclerImagenesRestaurante,
                        R.layout.recycler_imagenes, v, LinearLayoutManager.HORIZONTAL);
        restaurante.getliveDataImagen().observe(this, observerImagenes);

        //Recycler reseñas
        final Observer<List<Resenia>> observerResenias = res ->
                RecyclerAdapter.crearRecyclerLineal(res, ViewHolderResenia.class, R.id.recyclerReseniaRestaurante,
                        R.layout.recycler_resenias, v, LinearLayoutManager.VERTICAL);
        restaurante.getLiveDataResenia().observe(this, observerResenias);

        //Boton que abre la activity para crear una reseña de un restaurante
        botonResenia.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), ReseniaActivity.class);
                Bundle b = new Bundle();
                b.putString(Constantes.RESTAURANTE, restaurante.getIdRestaurante());
                intent.putExtras(b);
                startActivity(intent);
            }
        });

        botonFiltros.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), AniadirFiltrosActivity.class);
                intent.putExtra(Constantes.RESTAURANTE, restaurante.getIdRestaurante());
                startActivity(intent);
            }
        });
    }
}