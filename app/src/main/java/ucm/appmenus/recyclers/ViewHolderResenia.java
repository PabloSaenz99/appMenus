package ucm.appmenus.recyclers;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import ucm.appmenus.R;
import ucm.appmenus.entities.Resenia;
import ucm.appmenus.entities.Usuario;
import ucm.appmenus.ui.inicio.ReseniaActivity;
import ucm.appmenus.utils.BaseDatos;
import ucm.appmenus.utils.Constantes;

public class ViewHolderResenia  extends RecyclerView.ViewHolder implements IReclycerElement<Resenia>, View.OnClickListener {

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
        itemView.setOnClickListener(this);
    }

    @Override
    public void setDatos(Resenia res) {
        this.resenia = res;
        titulo.setText(res.getTitulo());
        creador.setText(res.getUsuarioNombre());
        texto.setText(res.getDescripcion());
        valoracion.setRating((float) res.getValoracion());
        valoracion.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float value, boolean b) {
                resenia.setValoracion(value);
                BaseDatos.getInstance().setValoracionRestaurante(resenia.getIdRestaurante(), value);
                BaseDatos.getInstance().addResenia(resenia);
            }
        });
    }

    @Override
    public Resenia getDatos() {
        return resenia;
    }

    @Override
    public void onClick(View view) {
        //Si el usuario que creó la reseña es el mismo que el usuario actual se permite editar
        if(resenia.getIdUsuario().contentEquals(Usuario.getUsuario().getIdUsuario())){
            Intent intent = new Intent(view.getContext(), ReseniaActivity.class);
            Bundle b = new Bundle();
            b.putParcelable(Constantes.RESENIA, resenia);
            intent.putExtras(b);
            view.getContext().startActivity(intent);
        }
    }
}
