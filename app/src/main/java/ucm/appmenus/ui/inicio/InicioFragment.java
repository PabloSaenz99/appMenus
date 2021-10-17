package ucm.appmenus.ui.inicio;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import ucm.appmenus.R;
import ucm.appmenus.Restaurante;
import ucm.appmenus.ui.filtros.FiltrosRecyclerAdapter;

public class InicioFragment extends Fragment {

    private InicioViewModel inicioViewModel;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        inicioViewModel =
                ViewModelProviders.of(this).get(InicioViewModel.class);
        //Inflater se usa cuando se quiere mostrar en bucle una cosa, por ejemplo para poner 100 fotos
        //En este caso se usa ya que hay 3 fragments(inicio, filtros y perfil), pero no es lo tipico
        View root = inflater.inflate(R.layout.fragment_inicio, container, false);

        //Crear el recycler de los restaurantes
        RecyclerView recyclerViewRestaurantes = root.findViewById(R.id.recyclerRestauranteInicio);
        recyclerViewRestaurantes.setLayoutManager(new LinearLayoutManager(
                this.getContext(), LinearLayoutManager.VERTICAL, false));
        //Crear el adapter y asignarlo
        RestauranteRecyclerAdapter adapterRestaurantes = new RestauranteRecyclerAdapter(cargarRestaurantes());
        recyclerViewRestaurantes.setAdapter(adapterRestaurantes);

        return root;
    }

    private ArrayList<Restaurante> cargarRestaurantes(){
        ArrayList<Restaurante> restaurantes = new ArrayList<Restaurante>();
        restaurantes.add(new Restaurante("El mexicano", "La mejor comida mexicana", 3.9f,
                "/data/data/ucm.appmenus/files/mexicano.jpg",
                new ArrayList<String>(){{add("Mexicana");add("Tacos");add("Picante");}},
                new ArrayList<String>(){{add("img1");add("img2");add("img3");}}));
        restaurantes.add(new Restaurante("La Fabada", "Comida asturiana", 4.4f,
                "/data/data/ucm.appmenus/files/asturiano.jpg",
                new ArrayList<String>(){{add("Asturiana");add("Fabada");add("Casera");}},
                new ArrayList<String>(){{add("img1");add("img2");add("img3");}}));
        restaurantes.add(new Restaurante("VIPS", "Hamburguesas y tortitas", 3.7f,
                "/data/data/ucm.appmenus/files/vips.jpg",
                new ArrayList<String>(){{add("Hamburguesa");add("Tacos");}},
                new ArrayList<String>(){{add("img1");add("img2");}}));

        return restaurantes;
    }
}