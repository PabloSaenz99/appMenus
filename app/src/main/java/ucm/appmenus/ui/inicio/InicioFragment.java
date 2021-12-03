package ucm.appmenus.ui.inicio;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import ucm.appmenus.MainActivity;
import ucm.appmenus.entities.Restaurante;
import ucm.appmenus.recyclers.RestauranteRecyclerAdapter;
import ucm.appmenus.R;
import ucm.appmenus.utils.Localizacion;
import ucm.appmenus.utils.OpenStreetMap;

public class InicioFragment extends Fragment {

    private static ArrayList<Restaurante> ultimaListaDeRestaurantes = new ArrayList<Restaurante>();

    private MainActivity mainActivity;

    private InicioViewModel inicioViewModel;
    private View root;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        inicioViewModel = ViewModelProviders.of(this).get(InicioViewModel.class);

        this.root = inflater.inflate(R.layout.fragment_inicio, container, false);
        this.mainActivity = (MainActivity) getActivity();
        //Actualizar la localizacion:
        mainActivity.getUsuario().getLocalizacion().refrescarLocalizacion();

        //Usado para no hacer la busqueda cada vez que se abre el fragment
        //if(getArguments().getBoolean("actualizar") || ultimaListaDeRestaurantes.size() > 0){
            //Usado para cargar los datos de OpenStreetMap (ver funciones para mas informacion)
            OpenStreetMap osm = new OpenStreetMap();
            osm.setPlaces(inicioViewModel.getRestaurantes(), new OpenStreetMap.OpenStreetAttributes(
                    new ArrayList<String>(), new ArrayList<String>(), 500,
                    mainActivity.getUsuario().getLocalizacion().latitude,
                    mainActivity.getUsuario().getLocalizacion().longitude));
            ultimaListaDeRestaurantes = inicioViewModel.getRestaurantes().getValue();
        /*}
        else{
            crearRecycler(ultimaListaDeRestaurantes);
        }
         */
        //Actualiza el recycler cuando se reciben los datos
        final Observer<ArrayList<Restaurante>> observer = new Observer<ArrayList<Restaurante>>() {
            @Override
            public void onChanged(ArrayList<Restaurante> restaurantes) {
                root.findViewById(R.id.progressBarInicio).setVisibility(View.GONE);
                crearRecycler(inicioViewModel.getRestaurantes().getValue());
            }
        };
        inicioViewModel.getRestaurantes().observe(getActivity(), observer);

        return root;
    }

    private void crearRecycler(ArrayList<Restaurante> restaurantes){
        //Crear el recycler de los restaurantes
        RecyclerView recyclerViewRestaurantes = root.findViewById(R.id.recyclerRestauranteInicio);
        recyclerViewRestaurantes.setLayoutManager(new LinearLayoutManager(
                this.getContext(), LinearLayoutManager.VERTICAL, false));

        //Crear el adapter y asignarlo
        RestauranteRecyclerAdapter adapterRestaurantes = new RestauranteRecyclerAdapter(restaurantes);
        recyclerViewRestaurantes.setAdapter(adapterRestaurantes);
    }
}