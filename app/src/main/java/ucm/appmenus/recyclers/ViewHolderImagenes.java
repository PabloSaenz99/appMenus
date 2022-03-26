package ucm.appmenus.recyclers;

import android.graphics.Bitmap;
import android.view.View;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import ucm.appmenus.R;
import ucm.appmenus.utils.Pair;

/**
 * Clase usada para la representacion de los filtros
 * */
public class ViewHolderImagenes extends RecyclerView.ViewHolder implements IReclycerElement<Bitmap> {

    private final ImageView imagen;
    private Bitmap bitmap;

    public ViewHolderImagenes(@NonNull View itemView) {
        super(itemView);
        imagen = itemView.findViewById(R.id.imagenRestaurante);
    }

    @Override
    public void setDatos(Bitmap data) {
        this.bitmap = data;
        imagen.setImageBitmap(this.bitmap);
    }

    @Override
    public Bitmap getDatos() { return this.bitmap; }
}