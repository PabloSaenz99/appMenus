package ucm.appmenus.ui.perfil;

import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import ucm.appmenus.MainActivity;
import ucm.appmenus.R;


import ucm.appmenus.entities.Resenia;
import ucm.appmenus.entities.Usuario;
import ucm.appmenus.login.LoginActivity;
import ucm.appmenus.recyclers.RecyclerAdapter;
import ucm.appmenus.recyclers.ViewHolderFiltros;
import ucm.appmenus.recyclers.ViewHolderResenia;
import ucm.appmenus.ui.filtros.FiltrosFragment;

public class PerfilFragment extends Fragment {

    private PerfilViewModel perfilViewModel;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        perfilViewModel =
                ViewModelProviders.of(this).get(PerfilViewModel.class);
        View root = inflater.inflate(R.layout.fragment_perfil, container, false);

        Usuario usuario = Usuario.getUsuario();

//        ImageView imagen = root.findViewById(R.id.imagenUsuarioPerfilFragment);
        TextView email = root.findViewById(R.id.emailUsuarioPerfilFragment);
        TextView nombre = root.findViewById(R.id.nombreUsuarioPerfilFragment);
        Button botonCerrarSesion = root.findViewById(R.id.botonCerrarSesion);

        //imagen.setImageBitmap(BitmapFactory.decodeFile(usuario.getImagenDir()));
        email.setText(usuario.getEmail());
        nombre.setText(usuario.getNombre());

        RecyclerAdapter.crearRecyclerLineal(new ArrayList<>(usuario.getResenias()), ViewHolderResenia.class,
                R.id.recyclerReseniasPerfilFragment, R.layout.recycler_resenias, root, LinearLayoutManager.VERTICAL);

        botonCerrarSesion.setOnClickListener(view -> {
            Usuario.cerrarSesion(getActivity());
            startActivity(new Intent(root.getContext(), LoginActivity.class));
            getActivity().finish();
        });
        return root;
    }
}