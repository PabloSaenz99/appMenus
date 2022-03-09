package ucm.appmenus.ui.inicio;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import ucm.appmenus.MainActivity;
import ucm.appmenus.entities.Restaurante;
import ucm.appmenus.recyclers.RecyclerAdapter;
import ucm.appmenus.recyclers.ViewHolderRestaurantes;
import ucm.appmenus.R;
import ucm.appmenus.utils.Constantes;
import ucm.appmenus.utils.OpenStreetMap;

public class InicioFragment extends Fragment {

    private MainActivity mainActivity;

    private InicioViewModel inicioViewModel;
    private View root;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        this.mainActivity = (MainActivity) getActivity();

        //inicioViewModel = ViewModelProviders.of(this).get(InicioViewModel.class);
        inicioViewModel = new ViewModelProvider(mainActivity).get(InicioViewModel.class);
        this.root = inflater.inflate(R.layout.fragment_inicio, container, false);

        //Usado para no hacer la busqueda cada vez que se abre el fragment
        if(getArguments() != null){
            if(getArguments().getBoolean(Constantes.ACTUALIZAR_INTENT)) {
                //Log.i("ESTADO", "Actualizando datos");
                //Actualizar la localizacion:
                mainActivity.getUsuario().getLocalizacion().refrescarLocalizacion();
                //Usado para cargar los datos de OpenStreetMap (ver funciones para mas informacion)
                OpenStreetMap osm = new OpenStreetMap();
                osm.setPlaces(inicioViewModel.getRestaurantes(), new OpenStreetMap.OpenStreetAttributes(
                        getArguments().getStringArrayList(Constantes.TIPOS_LOCAL),
                        getArguments().getStringArrayList(Constantes.TIPOS_COCINA),
                        getArguments().getInt(Constantes.AREA),
                        mainActivity.getUsuario().getLocalizacion().latitude,
                        mainActivity.getUsuario().getLocalizacion().longitude));
            }
            else{
                //Log.i("ESTADO", "Recuperando datos");
                inicioViewModel.getRestaurantes().postValue(
                        savedInstanceState.<Restaurante>getParcelableArrayList(Constantes.LISTA_RESTAURANTES));
            }
        }
        else {
            //Log.i("ESTADO", "Nuevos datos");
            //TODO: Pedir los datos por defecto (los almacenados en la config inicial en un fichero) en vez de estos
            OpenStreetMap osm = new OpenStreetMap();
            osm.setPlaces(inicioViewModel.getRestaurantes(), new OpenStreetMap.OpenStreetAttributes(
                    new ArrayList<String>(){{add("restaurant");}},
                    new ArrayList<String>(),
                    1500,
                    mainActivity.getUsuario().getLocalizacion().latitude,
                    mainActivity.getUsuario().getLocalizacion().longitude));
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

    //Usado para guardar los datos cuando cambia el fragmento
    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        //Log.i("ESTADO", "Guardando");
        outState.putParcelableArrayList(Constantes.LISTA_RESTAURANTES, inicioViewModel.getRestaurantes().getValue());
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
