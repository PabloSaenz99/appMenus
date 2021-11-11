package ucm.appmenus.recyclers;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import ucm.appmenus.R;
import ucm.appmenus.Resenia;

public class ReseniaRecyclerAdapter extends RecyclerView.Adapter<ReseniaRecyclerAdapter.ViewHolder> {

    private ArrayList<Resenia> listaDatos;

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private final View view;

        private final TextView titulo;
        private final TextView texto;
        private final RatingBar valoracion;
        private final TextView restauranteNombre;

        public ViewHolder(View view) {
            super(view);
            this.view = view;
            titulo = (TextView) view.findViewById(R.id.textRestarurantNameRecycler);
            texto = (TextView) view.findViewById(R.id.textRestaurantURLRecycler);
            valoracion = view.findViewById(R.id.toggleButtonFavRestaurantRecycler);
            restauranteNombre = view.findViewById(R.id.ratingRestaurantRecycler);
        }

        public void setDatos(Resenia res){
            titulo.setText(res.getTexto());
            texto.setText(res.getTitulo());
            valoracion.setRating((float) res.getValoracion());
            valoracion.setClickable(false);
            restauranteNombre.setText(res.getRestaurante().getNombre());
        }
    }

    /**
     * Initialize the dataset of the Adapter.
     */
    public ReseniaRecyclerAdapter(ArrayList<Resenia> dataSet) {listaDatos = dataSet;}

    // Create new views (invoked by the layout manager)
    @Override
    public ReseniaRecyclerAdapter.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        // Create a new view, which defines the UI of the list item
        View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.recycler_restaurante_busqueda, viewGroup, false);
        return new ReseniaRecyclerAdapter.ViewHolder(view);
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(ReseniaRecyclerAdapter.ViewHolder viewHolder, final int position) {
        viewHolder.setDatos(listaDatos.get(position));
    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return listaDatos.size();
    }
}
