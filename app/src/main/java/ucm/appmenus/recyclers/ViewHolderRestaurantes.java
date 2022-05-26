package ucm.appmenus.recyclers;

import android.content.Intent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import androidx.annotation.NonNull;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.RecyclerView;

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
import ucm.appmenus.utils.Pair;
import ucm.appmenus.utils.Precios;

public class ViewHolderRestaurantes extends RecyclerView.ViewHolder implements IReclycerElement<Restaurante>, View.OnClickListener {

    private final View view;
    private Restaurante restaurante;

    private final TextView nombre;
    private final TextView url;
    private final ToggleButton favorito;
    private final RatingBar valoracion;
    private final TextView direccion;
    private final ImageView imagenPrinc;
    private RecyclerView filtrosRecycler;
    private final TextView precioMin, precioMed, precioMax;
    private final Button abierto;

    public ViewHolderRestaurantes(@NonNull View view) {
        super(view);
        this.view = view;
        nombre = view.findViewById(R.id.textRestarurantNameRecycler);
        url = view.findViewById(R.id.textRestaurantURL);
        favorito = view.findViewById(R.id.toggleButtonFavRestaurantRecycler);
        valoracion = view.findViewById(R.id.ratingRestaurantRecycler);
        direccion = view.findViewById(R.id.textDireccionRestaurante);
        imagenPrinc = view.findViewById(R.id.imageRestaurantRecycler);
        precioMin = view.findViewById(R.id.textPrecioMin);
        precioMed = view.findViewById(R.id.textPrecioMed);
        precioMax = view.findViewById(R.id.textPrecioMax);
        abierto = view.findViewById(R.id.buttonAbierto);

        Usuario usuario = Usuario.getUsuario();

        favorito.setOnClickListener(v -> {

            if(usuario.getEmail().equals(Constantes.EMAIL_INVITADO)){
                favorito.setChecked(false);
                Toast.makeText(view.getContext(), Constantes.NECESARIO_LOGIN, Toast.LENGTH_LONG).show();
            }else{

                if(favorito.isChecked()) {
                    Toast.makeText(view.getContext(), nombre.getText().toString() + " añadido a favoritos",
                            Toast.LENGTH_LONG).show();
                    Usuario.getUsuario().addRestauranteFavorito(restaurante);
                }
                else {
                    Toast.makeText(view.getContext(), nombre.getText().toString() + " eliminado de favoritos",
                            Toast.LENGTH_LONG).show();
                    Usuario.getUsuario().removeRestauranteFavorito(restaurante);
                }
                BaseDatos.getInstance().addFavoritosUsuario(Usuario.getUsuario().getRestaurantesFavoritosID());
            }
        });

        itemView.setOnClickListener(this);
    }

    @Override
    public void setDatos(final Restaurante restaurante) {
        this.restaurante = restaurante;

        nombre.setText(restaurante.getNombre());
        url.setText(restaurante.getStringURL());
        favorito.setChecked(false);
        valoracion.setRating(restaurante.getLiveDataValoracion().getValue().floatValue());
        valoracion.setClickable(false);
        direccion.setText(restaurante.getLiveDataDireccion().getValue());

        int apertura = restaurante.getAbierto();
        if(apertura == 1) {
            abierto.setBackgroundColor(view.getResources().getColor(R.color.restauranteAbierto));
            abierto.setText(R.string.aperturaAbierto);
        }
        else if(apertura == -1){
            abierto.setBackgroundColor(view.getResources().getColor(R.color.restauranteCerrado));
            abierto.setText(R.string.aperturaCerrado);
        }
        else{
            abierto.setBackgroundColor(view.getResources().getColor(R.color.restauranteSinHorario));
            abierto.setText(R.string.aperturaDesconocida);
        }

        /**
         * Observa el RatingBar que contiene la valoración, cuando se actualiza mediante la BD
         * se muestran las estrellas
         * */
        final Observer<Double> observerValoracion = val -> valoracion.setRating(val.floatValue());
        restaurante.getLiveDataValoracion().observe((LifecycleOwner) view.getContext(), observerValoracion);

        /**
         * Observa el TextView que contiene la direccion, cuando se actualiza mediante WebScrapping
         * se muestra en la vista
         * */
        final Observer<String> observerDireccion = direccion::setText;
        restaurante.getLiveDataDireccion().observe((LifecycleOwner) view.getContext(), observerDireccion);

        /**
         * Observa los TextView que contienen los precios del restaurante, cuando se actualizan mediante WebScrapping
         * se muestran en la vista
         * */
        final Observer<Precios> observerPrecios = precios -> {
            if(precios.esCorrecto()) {
                view.findViewById(R.id.layoutPrecios).setVisibility(View.VISIBLE);
                precioMin.setText("Min: " + precios.minimo + "€");
                precioMed.setText("Med: " + precios.mediana + "€");
                precioMax.setText("Max: " + precios.maximo + "€");
            }
            else {
                view.findViewById(R.id.layoutPrecios).setVisibility(View.INVISIBLE);
            }
        };
        restaurante.getLiveDataPrecios().observe((LifecycleOwner) view.getContext(), observerPrecios);

        /**
         * Observa el ImageView que contiene la imagen, cuando se actualiza mediante WebScrapping
         * se muestra en la vista
         * */
        final Observer<List<Bitmap>> observerImagen = img -> imagenPrinc.setImageBitmap(img.get(0));
        restaurante.getliveDataImagen().observe((LifecycleOwner) view.getContext(), observerImagen);

        /**
         * Observa el recycler que contiene los filtros, cuando se actualizan mediante WebScrapping
         * se muestra en la vista
         * */
        final Observer<Set<String>> observerFiltros = new Observer<Set<String>>() {
            @Override
            public void onChanged(Set<String> filtros) {
                RecyclerAdapter<ViewHolderFiltros, Pair<String, Boolean>> recycler =
                        RecyclerAdapter.crearRecyclerGrid(
                                FiltrosFragment.transform(Constantes.traducirAlEsp(filtros), false),
                                ViewHolderFiltros.class, R.id.filtrosRestauranteRecycler,
                                R.layout.recycler_filtros, view, 3);
                //recycler.setMax(ViewGroup.LayoutParams.MATCH_PARENT, 250);
            }
        };
        restaurante.getLivedataFiltros().observe((LifecycleOwner) view.getContext(), observerFiltros);
    }

    @Override
    public Restaurante getDatos() { return restaurante; }

    @Override
    public void onClick(View view) {
        Intent intent = new Intent(view.getContext(), RestauranteDetalladoActivity.class);
        intent.putExtra(Constantes.RESTAURANTE, restaurante);
        view.getContext().startActivity(intent);
    }
}