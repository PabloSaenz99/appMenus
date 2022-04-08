package ucm.appmenus.recyclers;

import android.util.Log;
import android.view.View;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import ucm.appmenus.R;
import ucm.appmenus.utils.Pair;
/**
 * Clase usada para la representacion de los filtros
 * */
public class ViewHolderFiltros extends RecyclerView.ViewHolder implements IReclycerElement<Pair<String, Boolean>> {

    private final View view;
    private final Button filtro;
    private boolean estaActivado, esModificable;
    private Pair<String, Boolean> datos;

    public ViewHolderFiltros(@NonNull View itemView) {
        super(itemView);
        this.view = itemView;
        this.filtro = itemView.findViewById(R.id.buttonFiltro);
    }

    /**
     * @param data el primer elemento del par es el dato a mostrar, el segundo indica si debe poder modificarse
     *             true: se puede modificar, es seleccionable por el usuario
     *             false: no se puede modificar, el elemento es solo informativo
     * */
    @Override
    public void setDatos(Pair<String, Boolean> data) {
        filtro.setText(data.getPrimero());
        estaActivado = data.getSegundo();
        esModificable = data.getSegundo();
        setColor(!estaActivado);

        this.datos = data;
        this.datos.setSegundo(false);

        if(esModificable) {                     //Se puede modificar
            filtro.setOnClickListener(v -> {
                setColor(estaActivado);
                datos.setSegundo(estaActivado);
                estaActivado =! estaActivado;
            });
        }
    }

    private void setColor(boolean activo){
        if(activo){
            filtro.setBackgroundColor(view.getResources().getColor(R.color.botonActivado));
        }
        else{
            filtro.setBackgroundColor(view.getResources().getColor(R.color.botonDesactivado));
        }
    }

    @Override
    public Pair<String, Boolean> getDatos() { return this.datos; }
}
