package ucm.appmenus.ui.filtros;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import ucm.appmenus.R;

public class FiltrosRecyclerAdapter extends RecyclerView.Adapter<FiltrosRecyclerAdapter.ViewHolder> {

    private ArrayList<String> listaDatos;

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private final TextView textView;
        private final CheckBox checkBox;

        public ViewHolder(View view) {
            super(view);
            textView = (TextView) view.findViewById(R.id.textoRecyclerFiltros);
            checkBox = view.findViewById(R.id.checkBoxRecyclerFiltros);

            checkBox.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                }
            });
        }

        public void setDatos(String texto){
            textView.setText(texto);
        }

        public TextView getTextView() {
            return textView;
        }
    }

    /**
     * Initialize the dataset of the Adapter.
     */
    public FiltrosRecyclerAdapter(ArrayList<String> dataSet) {
        listaDatos = dataSet;
    }

    // Create new views (invoked by the layout manager)
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        // Create a new view, which defines the UI of the list item
        View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.recycler_filtros, viewGroup, false);

        return new ViewHolder(view);
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(ViewHolder viewHolder, final int position) {
        //Obtener los datos de listadatos y pasarselos al viewholder para que los muestre en la vista
        viewHolder.setDatos(listaDatos.get(position));
    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return listaDatos.size();
    }
}