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
    private boolean marcados;

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private final TextView textView;
        private final CheckBox checkBox;
        private final boolean marcado;

        public ViewHolder(View view, boolean marcado) {
            super(view);
            this.marcado = marcado;
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
            checkBox.setChecked(marcado);
            checkBox.setClickable(!marcado);
        }

        public TextView getTextView() {
            return textView;
        }
    }

    /**
     * Initialize the dataset of the Adapter.
     */
    public FiltrosRecyclerAdapter(ArrayList<String> dataSet, boolean marcados) {
        listaDatos = dataSet;
        this.marcados = marcados;
    }

    // Create new views (invoked by the layout manager)
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        // Create a new view, which defines the UI of the list item
        View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.recycler_filtros, viewGroup, false);

        //Hola
        return new ViewHolder(view, marcados);
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