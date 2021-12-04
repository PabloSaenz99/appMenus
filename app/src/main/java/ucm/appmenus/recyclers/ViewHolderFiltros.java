package ucm.appmenus.recyclers;

import android.view.View;
import android.widget.CheckBox;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import ucm.appmenus.R;
import ucm.appmenus.utils.Pair;

public class ViewHolderFiltros extends RecyclerView.ViewHolder implements IReclycerElement<Pair<String, Boolean>> {

    private final TextView textView;
    private final CheckBox checkBox;
    private Pair<String, Boolean> datos;
    public ViewHolderFiltros(@NonNull View itemView) {
        super(itemView);
        textView = (TextView) itemView.findViewById(R.id.textoRecyclerFiltros);
        checkBox = itemView.findViewById(R.id.checkBoxRecyclerFiltros);
    }

    @Override
    public void setDatos(Pair<String, Boolean> data) {
        this.datos = data;
        textView.setText(datos.getPrimero());
        checkBox.setChecked(!datos.getSegundo());
        checkBox.setClickable(datos.getSegundo());
    }

    @Override
    public Pair<String, Boolean> getDatos() {
        return this.datos;
    }
}
