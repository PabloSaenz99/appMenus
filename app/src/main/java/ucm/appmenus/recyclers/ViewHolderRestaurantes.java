/*package ucm.appmenus.recyclers;

import android.view.View;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.ToggleButton;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import ucm.appmenus.R;
import ucm.appmenus.entities.Restaurante;

public class RecyclerTest extends AbstractViewHolder<Restaurante>{

    private View view;

    private final TextView nombre;
    private final TextView url;
    private final ToggleButton favorito;
    private final RatingBar valoracion;
    private final ImageView imagenPrincDir;
    private final RecyclerView filtrosRecycler;

    public RecyclerTest(@NonNull View view) {
        super(view);
        this.view = view;
        nombre = view.findViewById(R.id.textRestarurantNameRecycler);
        url = view.findViewById(R.id.textRestaurantURL);
        favorito = view.findViewById(R.id.toggleButtonFavRestaurantRecycler);
        valoracion = view.findViewById(R.id.ratingRestaurantRecycler);
        imagenPrincDir = view.findViewById(R.id.imageRestaurantRecycler);
        filtrosRecycler = view.findViewById(R.id.filtrosRestauranteRecycler);

        favorito.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Aqui se añadiria a los favoritos del usuario
            }
        });
    }

    @Override
    public void setDatos(Restaurante restaurante) {
        nombre.setText(restaurante.getNombre());
        url.setText(restaurante.getStringURL());
        favorito.setChecked(false);
        valoracion.setRating((float )restaurante.getValoracion());
        valoracion.setClickable(false);
        //TODO: Hacer algo con la imagen
        //imagenPrincDir.setImageBitmap(BitmapFactory.decodeFile(restaurante.getimagenPrincDir()));
        //Recycler filtros
        filtrosRecycler.setLayoutManager(new LinearLayoutManager(
                view.getContext(), LinearLayoutManager.HORIZONTAL, false));
        //Create adapter
        FiltrosRecyclerAdapter adapterFiltros = new FiltrosRecyclerAdapter(
                restaurante.getFiltros().toArray(new String[0]), true);
        //Set the adapter
        filtrosRecycler.setAdapter(adapterFiltros);
    }
}
 */
package ucm.appmenus.recyclers;

import android.view.View;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.ToggleButton;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import ucm.appmenus.R;
import ucm.appmenus.entities.Restaurante;
import ucm.appmenus.ui.filtros.FiltrosFragment;
import ucm.appmenus.utils.Pair;

public class ViewHolderRestaurantes extends RecyclerView.ViewHolder implements IReclycerElement<Restaurante>{

    private View view;
    private Restaurante datos;

    private final TextView nombre;
    private final TextView url;
    private final ToggleButton favorito;
    private final RatingBar valoracion;
    private final ImageView imagenPrincDir;
    private final RecyclerView filtrosRecycler;

    public ViewHolderRestaurantes(@NonNull View view) {
        super(view);
        this.view = view;
        nombre = view.findViewById(R.id.textRestarurantNameRecycler);
        url = view.findViewById(R.id.textRestaurantURL);
        favorito = view.findViewById(R.id.toggleButtonFavRestaurantRecycler);
        valoracion = view.findViewById(R.id.ratingRestaurantRecycler);
        imagenPrincDir = view.findViewById(R.id.imageRestaurantRecycler);
        filtrosRecycler = view.findViewById(R.id.filtrosRestauranteRecycler);

        favorito.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Aqui se añadiria a los favoritos del usuario
            }
        });
    }

    @Override
    public void setDatos(Restaurante restaurante) {
        nombre.setText(restaurante.getNombre());
        url.setText(restaurante.getStringURL());
        favorito.setChecked(false);
        valoracion.setRating((float )restaurante.getValoracion());
        valoracion.setClickable(false);
        //TODO: Hacer algo con la imagen
        //imagenPrincDir.setImageBitmap(BitmapFactory.decodeFile(restaurante.getimagenPrincDir()));

        //Recycler filtros
        filtrosRecycler.setLayoutManager(new LinearLayoutManager(
                view.getContext(), LinearLayoutManager.HORIZONTAL, false));
        //Create adapter
        RecyclerAdapter<ViewHolderFiltros, Pair<String, Boolean>> adapterFiltros =
                new RecyclerAdapter<ViewHolderFiltros, Pair<String, Boolean>>(
                        FiltrosFragment.transform(restaurante.getFiltros(), false),
                        R.layout.recycler_filtros, ViewHolderFiltros.class);
        //Set the adapter
        filtrosRecycler.setAdapter(adapterFiltros);
    }

    @Override
    public Restaurante getDatos() {
        return datos;
    }
}