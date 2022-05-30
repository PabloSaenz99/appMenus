package ucm.appmenus.ui.perfil;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.ToggleButton;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import ucm.appmenus.R;
import ucm.appmenus.entities.Restaurante;
import ucm.appmenus.entities.Usuario;
import ucm.appmenus.login.LoginActivity;
import ucm.appmenus.recyclers.RecyclerAdapter;
import ucm.appmenus.recyclers.ViewHolderResenia;
import ucm.appmenus.recyclers.ViewHolderRestaurantes;
import ucm.appmenus.ui.SettingsActivity;
import ucm.appmenus.utils.Constantes;

public class PerfilFragment extends Fragment {

    private PerfilViewModel perfilViewModel;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        perfilViewModel =
                ViewModelProviders.of(this).get(PerfilViewModel.class);
        View root = inflater.inflate(R.layout.fragment_perfil, container, false);

        Usuario usuario = Usuario.getUsuario();

        //ImageView imagen = root.findViewById(R.id.imagenUsuarioPerfilFragment);
        TextView email = root.findViewById(R.id.emailUsuarioPerfilFragment);
        TextView nombre = root.findViewById(R.id.nombreUsuarioPerfilFragment);
        Button botonCerrarSesion = root.findViewById(R.id.botonCerrarSesion);
        Button botonResenias = root.findViewById(R.id.buttonMisResenias);
        Button botonFavoritos = root.findViewById(R.id.buttonMisFavoritos);
        RecyclerView recyclerView = root.findViewById(R.id.recyclerReseniasPerfilFragment);
        View line = root.findViewById(R.id.divider);
        ToggleButton settings = root.findViewById(R.id.settings);


        if(usuario.getEmail().equals(Constantes.EMAIL_INVITADO)){

            nombre.setText("INICIA SESION");
            email.setText("para una mejor experiencia");

            botonResenias.setVisibility(View.INVISIBLE);
            line.setVisibility(View.INVISIBLE);
            botonFavoritos.setVisibility(View.INVISIBLE);
            recyclerView.setVisibility(View.INVISIBLE);
            botonCerrarSesion.setText("INICIA SESION");
            botonCerrarSesion.setVisibility(View.VISIBLE);
            settings.setVisibility(View.INVISIBLE);

        }else{
            //imagen.setImageBitmap(BitmapFactory.decodeFile(usuario.getImagenDir()));
            email.setText(usuario.getEmail());
            final Observer<String> observer = nombre::setText;
            Usuario.getUsuario().getNombre().observe(getActivity(), observer);
        }

        botonResenias.setOnClickListener(view -> {
            RecyclerAdapter.crearRecyclerLineal(new ArrayList<>(usuario.getResenias()), ViewHolderResenia.class,
                    R.id.recyclerReseniasPerfilFragment, R.layout.recycler_resenias, root, LinearLayoutManager.VERTICAL);
            botonFavoritos.setBackgroundColor(root.getResources().getColor(R.color.botonDesactivado));
            botonResenias.setBackgroundColor(root.getResources().getColor(R.color.botonActivado));
        });

        botonFavoritos.setOnClickListener(view -> {
            RecyclerAdapter.crearRecyclerLineal(new ArrayList<>(usuario.getRestaurantesFavoritos()), ViewHolderRestaurantes.class,
                    R.id.recyclerReseniasPerfilFragment, R.layout.recycler_restaurantes, root, LinearLayoutManager.VERTICAL);
            botonResenias.setBackgroundColor(root.getResources().getColor(R.color.botonDesactivado));
            botonFavoritos.setBackgroundColor(root.getResources().getColor(R.color.botonActivado));
        });

        botonCerrarSesion.setOnClickListener(view -> {
            Usuario.cerrarSesion();
        });

        settings.setOnClickListener(v ->{
            startActivity(new Intent(root.getContext(), SettingsActivity.class));
        });

        botonResenias.callOnClick();

        return root;
    }
}