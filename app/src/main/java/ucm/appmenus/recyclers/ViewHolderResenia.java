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
    private final TextView creador;
    private final TextView texto;
    private final RatingBar valoracion;

    public ViewHolderResenia(@NonNull View view) {
        super(view);
        titulo = view.findViewById(R.id.tituloResenia);
        creador = view.findViewById(R.id.usuarioResenia);
        texto = view.findViewById(R.id.descripcionResenia);
        valoracion = view.findViewById(R.id.valoracionResenia);
    }

    @Override
    public void setDatos(Resenia res) {
        this.resenia = res;
        titulo.setText(res.getTexto());
        creador.setText(res.getUsuarioID());
        texto.setText(res.getTitulo());
        valoracion.setRating((float) res.getValoracion());
        valoracion.setClickable(false);
    }

    @Override
    public Resenia getDatos() {
        return resenia;
    }
}
