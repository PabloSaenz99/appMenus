package ucm.appmenus.recyclers;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import androidx.annotation.NonNull;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.HashSet;

import ucm.appmenus.R;
import ucm.appmenus.entities.Restaurante;

import android.graphics.Bitmap;

import androidx.recyclerview.widget.GridLayoutManager;

import ucm.appmenus.login.RegistroActivity;
import ucm.appmenus.ui.filtros.FiltrosFragment;
import ucm.appmenus.ui.inicio.RestauranteDetalladoActivity;
import ucm.appmenus.utils.Pair;

public class ViewHolderRestaurantes extends RecyclerView.ViewHolder implements IReclycerElement<Restaurante> {

    private final View view;
    private Restaurante datos;

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

        favorito.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //TODO Aqui se añadiria a los favoritos del usuario
                if(favorito.isChecked())
                    Toast.makeText(view.getContext(), nombre.getText().toString() + " añadido a favoritos",
                        Toast.LENGTH_LONG).show();
                else
                    Toast.makeText(view.getContext(), nombre.getText().toString() + " eliminado de favoritos",
                            Toast.LENGTH_LONG).show();
            }
        });
        imagenPrinc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //TODO (quiza) hacer que en vez de pulsando la imagen, añadiendo un boton
                Intent intent = new Intent(view.getContext(), RestauranteDetalladoActivity.class);
                intent.putExtra("restaurante", datos);
                view.getContext().startActivity(intent);
            }
        });
    }

    @Override
    public void setDatos(final Restaurante restaurante) {
        datos = restaurante;

        nombre.setText(restaurante.getNombre());
        url.setText(restaurante.getStringURL());
        favorito.setChecked(false);
        valoracion.setRating((float )restaurante.getValoracion());
        valoracion.setClickable(false);
        direccion.setText(restaurante.getDireccion().getValue());

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
        restaurante.getDireccion().observe((LifecycleOwner) view.getContext(), observerDireccion);

        /**
         * Observa el ImageView que contiene la imagen, cuando se actualiza mediante WebScrapping
         * se muestra en la vista
         * */
        final Observer<ArrayList<Bitmap>> observerImagen = new Observer<ArrayList<Bitmap>>() {
            @Override
            public void onChanged(ArrayList<Bitmap> img) {
                imagenPrinc.setImageBitmap(img.get(0));
            }
        };
        restaurante.getliveDataImagen().observe((LifecycleOwner) view.getContext(), observerImagen);

        /**
         * Observa el recycler que contiene los filtros, cuando se actualizan mediante WebScrapping
         * se muestra en la vista
         * */
        final Observer<HashSet<String>> observerFiltros = new Observer<HashSet<String>>() {
            @Override
            public void onChanged(HashSet<String> filtros) {
                RecyclerAdapter.crearRecyclerGrid(FiltrosFragment.transform(filtros, false),
                        ViewHolderFiltros.class, R.id.filtrosRestauranteRecycler,
                        R.layout.recycler_filtros, view, 3);
            }
        };
        restaurante.getLivedataFiltros().observe((LifecycleOwner) view.getContext(), observerFiltros);
    }

    @Override
    public Restaurante getDatos() { return datos; }
}