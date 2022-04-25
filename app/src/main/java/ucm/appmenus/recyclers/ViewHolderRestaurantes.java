package ucm.appmenus.recyclers;

import android.content.Intent;
import android.view.View;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import androidx.annotation.NonNull;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import ucm.appmenus.R;
import ucm.appmenus.entities.Restaurante;

import android.graphics.Bitmap;

import ucm.appmenus.entities.Usuario;
import ucm.appmenus.ui.filtros.FiltrosFragment;
import ucm.appmenus.ui.inicio.RestauranteDetalladoActivity;
import ucm.appmenus.utils.BaseDatos;
import ucm.appmenus.utils.Constantes;

public class ViewHolderRestaurantes extends RecyclerView.ViewHolder implements IReclycerElement<Restaurante> {

    private final View view;
    private Restaurante restaurante;

    private final TextView nombre;
    private final TextView url;
    private final ToggleButton favorito;
    private final RatingBar valoracion;
    private final TextView direccion;
    private final ImageView imagenPrinc;
    private RecyclerView filtrosRecycler;

    public ViewHolderRestaurantes(@NonNull View view) {
        super(view);
        this.view = view;
        nombre = view.findViewById(R.id.textRestarurantNameRecycler);
        url = view.findViewById(R.id.textRestaurantURL);
        favorito = view.findViewById(R.id.toggleButtonFavRestaurantRecycler);
        valoracion = view.findViewById(R.id.ratingRestaurantRecycler);
        direccion = view.findViewById(R.id.textDireccionRestaurante);
        imagenPrinc = view.findViewById(R.id.imageRestaurantRecycler);

        favorito.setOnClickListener(v -> {
            //TODO Aqui se añadiria a los favoritos del usuario: tiene que llamar a add a la bd y a add a restaurantes fav de usuario
            if(favorito.isChecked()) {
                Toast.makeText(view.getContext(), nombre.getText().toString() + " añadido a favoritos",
                        Toast.LENGTH_LONG).show();
                Usuario.getUsuario().addRestauranteFavorito(restaurante);
                BaseDatos.getInstance().addFavoritosUsuario(Usuario.getUsuario().getRestaurantesFavoritosID());
            }
            else {
                Toast.makeText(view.getContext(), nombre.getText().toString() + " eliminado de favoritos",
                        Toast.LENGTH_LONG).show();
                Usuario.getUsuario().removeRestauranteFavorito(restaurante);
                BaseDatos.getInstance().addFavoritosUsuario(Usuario.getUsuario().getRestaurantesFavoritosID());
            }
        });
        imagenPrinc.setOnClickListener(v -> {
            //TODO (quiza) hacer que en vez de pulsando la imagen, añadiendo un boton
            Intent intent = new Intent(view.getContext(), RestauranteDetalladoActivity.class);
            intent.putExtra(Constantes.RESTAURANTE, restaurante);
            view.getContext().startActivity(intent);
        });
    }

    @Override
    public void setDatos(final Restaurante restaurante) {
        this.restaurante = restaurante;

        nombre.setText(restaurante.getNombre());
        url.setText(restaurante.getStringURL());
        favorito.setChecked(false);
        valoracion.setRating((float )restaurante.getValoracion());
        valoracion.setClickable(false);
        direccion.setText(restaurante.getLiveDataDireccion().getValue());

        /**
         * Observa el TextView que contiene la direccion, cuando se actualiza mediante WebScrapping
         * se muestra en la vista
         * */
        final Observer<String> observerDireccion = new Observer<String>() {
            @Override
            public void onChanged(String dir) {
                direccion.setText(dir);
            }
        };
        restaurante.getLiveDataDireccion().observe((LifecycleOwner) view.getContext(), observerDireccion);

        /**
         * Observa el ImageView que contiene la imagen, cuando se actualiza mediante WebScrapping
         * se muestra en la vista
         * */
        final Observer<List<Bitmap>> observerImagen = new Observer<List<Bitmap>>() {
            @Override
            public void onChanged(List<Bitmap> img) {
                imagenPrinc.setImageBitmap(img.get(0));
            }
        };
        restaurante.getliveDataImagen().observe((LifecycleOwner) view.getContext(), observerImagen);

        /**
         * Observa el recycler que contiene los filtros, cuando se actualizan mediante WebScrapping
         * se muestra en la vista
         * */
        final Observer<Set<String>> observerFiltros = new Observer<Set<String>>() {
            @Override
            public void onChanged(Set<String> filtros) {
                RecyclerAdapter.crearRecyclerGrid(FiltrosFragment.transform(filtros, false),
                        ViewHolderFiltros.class, R.id.filtrosRestauranteRecycler,
                        R.layout.recycler_filtros, view, 3);
            }
        };
        restaurante.getLivedataFiltros().observe((LifecycleOwner) view.getContext(), observerFiltros);
    }

    @Override
    public Restaurante getDatos() { return restaurante; }
}