package ucm.appmenus.ui.inicio;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Icon;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import ucm.appmenus.R;
import ucm.appmenus.Restaurante;
import ucm.appmenus.ui.filtros.FiltrosRecyclerAdapter;

public class RestauranteRecyclerAdapter extends RecyclerView.Adapter<RestauranteRecyclerAdapter.ViewHolder> {

    private ArrayList<Restaurante> listaDatos;

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private final View view;

        private final TextView nombre;
        private final TextView descripcion;
        private final CheckBox favorito;
        private final RatingBar valoracion;
        private final ImageView imagenPrincDir;
        private final RecyclerView filtrosRecycler;
        //private final ArrayList<String> imagenesDir;

        public ViewHolder(View view) {
            super(view);
            this.view = view;
            nombre = (TextView) view.findViewById(R.id.textRestarurantNameRecycler);
            descripcion = (TextView) view.findViewById(R.id.textRestaurantDescRecycler);
            favorito = view.findViewById(R.id.checkBoxFavRestaurantRecycler);
            valoracion = view.findViewById(R.id.ratingRestaurantRecycler);
            imagenPrincDir = view.findViewById(R.id.imageRestaurantRecycler);
            filtrosRecycler = view.findViewById(R.id.filtrosRestauranteRecycler);

            favorito.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                }
            });
        }

        public void setDatos(Restaurante restaurante){
            nombre.setText(restaurante.getNombre());
            descripcion.setText(restaurante.getDescripcion());
            favorito.setChecked(false);
            valoracion.setRating(restaurante.getValoracion());
            valoracion.setClickable(false);
            imagenPrincDir.setImageBitmap(BitmapFactory.decodeFile(restaurante.getimagenPrincDir()));
            //Recycler filtros
            filtrosRecycler.setLayoutManager(new LinearLayoutManager(
                    view.getContext(), LinearLayoutManager.HORIZONTAL, false));
            //Create adapter
            FiltrosRecyclerAdapter adapterFiltros = new FiltrosRecyclerAdapter(restaurante.getFiltros(), true);
            //Set the adapter
            filtrosRecycler.setAdapter(adapterFiltros);
        }
    }

    /**
     * Initialize the dataset of the Adapter.
     */
    public RestauranteRecyclerAdapter(ArrayList<Restaurante> dataSet) {listaDatos = dataSet;}

    // Create new views (invoked by the layout manager)
    @Override
    public RestauranteRecyclerAdapter.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        // Create a new view, which defines the UI of the list item
        View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.recycler_restaurante_busqueda, viewGroup, false);
        //Hola
        return new RestauranteRecyclerAdapter.ViewHolder(view);
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(RestauranteRecyclerAdapter.ViewHolder viewHolder, final int position) {
        //Obtener los datos de listadatos y pasarselos al viewholder para que los muestre en la vista
        viewHolder.setDatos(listaDatos.get(position));
    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return listaDatos.size();
    }
}
