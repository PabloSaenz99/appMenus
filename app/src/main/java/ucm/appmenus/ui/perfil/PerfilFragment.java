package ucm.appmenus.ui.perfil;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Icon;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import ucm.appmenus.R;
import ucm.appmenus.Usuario;
import ucm.appmenus.ui.inicio.RestauranteRecyclerAdapter;

public class PerfilFragment extends Fragment {

    private PerfilViewModel notificationsViewModel;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        notificationsViewModel =
                ViewModelProviders.of(this).get(PerfilViewModel.class);
        View root = inflater.inflate(R.layout.fragment_perfil, container, false);

        Usuario usuario =null;
        ImageView imagen = root.findViewById(R.id.imagenUsuarioPerfilFragment);
        TextView email = root.findViewById(R.id.emailUsuarioPerfilFragment);
        TextView nombre = root.findViewById(R.id.nombreUsuarioPerfilFragment);

        imagen.setImageBitmap(BitmapFactory.decodeFile(usuario.getImagenDir()));
        email.setText(usuario.getEmail());
        nombre.setText(usuario.getNombre());

        //Crear el recycler de los comentarios
        RecyclerView recyclerViewComentarios = root.findViewById(R.id.recyclerComentariosUsuarioPerfilFragment);
        recyclerViewComentarios.setLayoutManager(new LinearLayoutManager(
                this.getContext(), LinearLayoutManager.VERTICAL, false));
        //TODO: Crear el adapter y asignarlo
        ReseniaRecyclerAdapter adapterRestaurantes = new ReseniaRecyclerAdapter(usuario.getResenias());
        recyclerViewComentarios.setAdapter(adapterRestaurantes);

        return root;
    }
}