package ucm.appmenus.ui.perfil;

import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import ucm.appmenus.R;
import ucm.appmenus.entities.Resenia;
import ucm.appmenus.entities.Usuario;
import ucm.appmenus.recyclers.RecyclerAdapter;
import ucm.appmenus.recyclers.ViewHolderResenia;
import ucm.appmenus.recyclers.ViewHolderRestaurantes;

public class PerfilFragment extends Fragment {

    private PerfilViewModel notificationsViewModel;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        notificationsViewModel =
                ViewModelProviders.of(this).get(PerfilViewModel.class);
        View root = inflater.inflate(R.layout.fragment_perfil, container, false);

        //TODO: Obtener el usuario de mainactivity
        Usuario usuario = null;
        ImageView imagen = root.findViewById(R.id.imagenUsuarioPerfilFragment);
        TextView email = root.findViewById(R.id.emailUsuarioPerfilFragment);
        TextView nombre = root.findViewById(R.id.nombreUsuarioPerfilFragment);

        imagen.setImageBitmap(BitmapFactory.decodeFile(usuario.getImagenDir()));
        email.setText(usuario.getEmail());
        nombre.setText(usuario.getNombre());

        crearRecycler(new ArrayList<Resenia>(), root);
        return root;
    }

    private void crearRecycler(ArrayList<Resenia> resenias, View root){
        RecyclerView recyclerViewComentarios = root.findViewById(R.id.recyclerReseniasPerfilFragment);
        recyclerViewComentarios.setLayoutManager(new LinearLayoutManager(
                this.getContext(), LinearLayoutManager.VERTICAL, false));

        RecyclerAdapter<ViewHolderResenia, Resenia> adapterResenias =
                new RecyclerAdapter<ViewHolderResenia, Resenia>(
                        resenias, R.layout.recycler_resenias, ViewHolderResenia.class);
        recyclerViewComentarios.setAdapter(adapterResenias);
    }
}