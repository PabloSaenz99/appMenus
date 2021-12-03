package ucm.appmenus.recyclers;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import ucm.appmenus.utils.Pair;
import ucm.appmenus.R;

public class FiltrosRecyclerAdapter extends RecyclerView.Adapter<FiltrosRecyclerAdapter.ViewHolder> {

    private String[] listaDatos;
    private boolean marcados;
    private ArrayList<ViewHolder> viewHolder;

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private final TextView textView;
        private final CheckBox checkBox;
        private final boolean marcado;

        public ViewHolder(View view, boolean marcado) {
            super(view);
            this.marcado = marcado;
            textView = (TextView) view.findViewById(R.id.textoRecyclerFiltros);
            checkBox = view.findViewById(R.id.checkBoxRecyclerFiltros);
        }

        public void setDatos(String texto){
            textView.setText(texto);
            checkBox.setChecked(marcado);
            checkBox.setClickable(!marcado);
        }

        public Pair<String, Boolean> getDatos(){
            return new Pair<String, Boolean>(textView.getText().toString(), checkBox.isChecked());
        }

        public TextView getTextView() {
            return textView;
        }
    }

    /**
     * Initialize the dataset of the Adapter.
     */
    public FiltrosRecyclerAdapter(String[] dataSet, boolean marcados) {
        listaDatos = dataSet;
        this.marcados = marcados;
        viewHolder = new ArrayList<ViewHolder>();
    }

    public ArrayList<String> getDatos(){
        ArrayList<String> l = new ArrayList<String>();
        for (ViewHolder vh: viewHolder){
            if(vh.getDatos().getSegundo())
                l.add(vh.getDatos().getPrimero());
        }
        return l;
    }

    // Create new views (invoked by the layout manager)
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.recycler_filtros, viewGroup, false);
        viewHolder.add(new ViewHolder(view, marcados));
        return viewHolder.get(viewHolder.size() - 1);
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(ViewHolder viewHolder, final int position) {
        viewHolder.setDatos(listaDatos[position]);
    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() { return listaDatos.length; }
}