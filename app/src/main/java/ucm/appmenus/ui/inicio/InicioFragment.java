package ucm.appmenus.ui.inicio;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import ucm.appmenus.MainActivity;
import ucm.appmenus.entities.Restaurante;
import ucm.appmenus.recyclers.RecyclerAdapter;
import ucm.appmenus.recyclers.ViewHolderRestaurantes;
import ucm.appmenus.R;
import ucm.appmenus.utils.OpenStreetMap;

public class InicioFragment extends Fragment {

    //private static ArrayList<Restaurante> ultimaListaDeRestaurantes = new ArrayList<Restaurante>();

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
        if(getArguments() != null && getArguments().getBoolean("actualizar")) {
            //Usado para cargar los datos de OpenStreetMap (ver funciones para mas informacion)
            OpenStreetMap osm = new OpenStreetMap();
            osm.setPlaces(inicioViewModel.getRestaurantes(), new OpenStreetMap.OpenStreetAttributes(
                    getArguments().getStringArrayList("tiposLocal"),
                    getArguments().getStringArrayList("tiposCocina"),
                    getArguments().getInt("area"),
                    mainActivity.getUsuario().getLocalizacion().latitude,
                    mainActivity.getUsuario().getLocalizacion().longitude));
        }
        else {
            crearRecycler(new ArrayList<Restaurante>());
        }

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
        RecyclerView recyclerViewRestaurantes = root.findViewById(R.id.recyclerRestauranteInicio);
        recyclerViewRestaurantes.setLayoutManager(new LinearLayoutManager(
                this.getContext(), LinearLayoutManager.VERTICAL, false));

        RecyclerAdapter<ViewHolderRestaurantes, Restaurante> adapterRestaurantes =
                new RecyclerAdapter<ViewHolderRestaurantes, Restaurante>(
                        restaurantes, R.layout.recycler_restaurantes, ViewHolderRestaurantes.class);
        recyclerViewRestaurantes.setAdapter(adapterRestaurantes);
    }

    /**
     * Patron Singleton:
     * Usado para inicializar solo una vez la clase, en aperturas siguientes no será necesario
     * */
    /*
    private void init(){
        primeraApertura = false;

        OpenStreetMap osm = new OpenStreetMap();
        osm.setPlaces(inicioViewModel.getRestaurantes(), new OpenStreetMap.OpenStreetAttributes(
                new ArrayList<String>(),
                new ArrayList<String>(),
                500,
                mainActivity.getUsuario().getLocalizacion().latitude,
                mainActivity.getUsuario().getLocalizacion().longitude));
        ultimaListaDeRestaurantes = inicioViewModel.getRestaurantes().getValue();
    }

     */
}