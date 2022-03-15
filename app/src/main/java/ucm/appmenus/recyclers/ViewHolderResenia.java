package ucm.appmenus.recyclers;

import android.view.View;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import ucm.appmenus.R;
import ucm.appmenus.entities.Resenia;

public class ViewHolderResenia  extends RecyclerView.ViewHolder implements IReclycerElement<Resenia> {

    private Resenia resenia;

    private final TextView titulo;
    private final TextView texto;
    private final RatingBar valoracion;

    public ViewHolderResenia(@NonNull View view) {
        super(view);
        titulo = (TextView) view.findViewById(R.id.tituloResenia);
        texto = (TextView) view.findViewById(R.id.descripcionResenia);
        valoracion = view.findViewById(R.id.valoracionResenia);
    }

    @Override
    public void setDatos(Resenia res) {
        this.resenia = res;
        titulo.setText(res.getTexto());
        texto.setText(res.getTitulo());
        valoracion.setRating((float) res.getValoracion());
        valoracion.setClickable(false);
    }

    @Override
    public Resenia getDatos() {
        return resenia;
    }
}
