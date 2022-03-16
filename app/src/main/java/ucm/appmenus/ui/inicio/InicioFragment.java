package ucm.appmenus.ui.inicio;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import ucm.appmenus.MainActivity;
import ucm.appmenus.R;
import ucm.appmenus.entities.Restaurante;
import ucm.appmenus.recyclers.RecyclerAdapter;
import ucm.appmenus.recyclers.ViewHolderRestaurantes;
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

        root.findViewById(R.id.progressBarInicio).setVisibility(View.VISIBLE);

        //Usado para no hacer la busqueda cada vez que se abre el fragment
        //Si es distinto de null, es que en algun momento se han cargado restaurantes o que hay que cargarlos.
        if(getArguments() != null){
            /*Si esta marcado, significa que se ha llegado a este fragment desde el boton de "filtrar"
            en el fragment de filtros, por lo tanto hay que realizar una bsuqueda en OpenStreetMap
            con dichos filtros (guardados en getArguments() mediante un Bundle)*/
            if(getArguments().getBoolean(Constantes.ACTUALIZAR_INTENT)) {
                //Actualizar la localizacion con la posicion actual
                mainActivity.getUsuario().getLocalizacion().actualizarLocalizacion();
                //Usado para cargar los datos de OpenStreetMap (ver funciones para mas informacion)
                OpenStreetMap osm = new OpenStreetMap();
                osm.setPlaces(inicioViewModel.getRestaurantes(), new OpenStreetMap.OpenStreetAttributes(
                        getArguments().getStringArrayList(Constantes.TIPOS_LOCAL),
                        getArguments().getStringArrayList(Constantes.TIPOS_COCINA),
                        getArguments().getInt(Constantes.AREA),
                        mainActivity.getUsuario().getLocalizacion().latitude,
                        mainActivity.getUsuario().getLocalizacion().longitude));
            }
            /*Sino, significa que se ha llegado a este fragmento mediante swipe desde uno de los otros fragmentos,
            pero previamente se habia realizado una busqueda y por lo tanto se recuperan los datos guardados
            en saveInstanceState mediante la funcion onSaveInstanceState(@NonNull Bundle outState)*/
            else{
                inicioViewModel.getRestaurantes().postValue(
                        savedInstanceState.<Restaurante>getParcelableArrayList(Constantes.LISTA_RESTAURANTES));
            }
        }
        /*Es la primera vez que se llega a este Fragment, por lo tanto no hay datos prebuscados ni filtros
        establecidos, asi que se realiza una bsuqueda basica preestablecida*/
        else {
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
                root.findViewById(R.id.progressBarInicio).setVisibility(View.INVISIBLE);
                crearRecycler(inicioViewModel.getRestaurantes().getValue());
            }
        };
        inicioViewModel.getRestaurantes().observe(getActivity(), observer);

        return root;
    }

    /**
     * Usado para guardar los datos cuando cambia el fragmento (por ejemplo se hace swipe al fragmento de
     * "filtros", asi no hace falta volver a hacer la busqueda en OpenStreetMap
     * @param outState variable donde se van a guardar los datos necesarios
     */
    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
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
}
