package ucm.appmenus.ui.inicio;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioGroup;
import android.widget.TextView;

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
import ucm.appmenus.R;
import ucm.appmenus.entities.Restaurante;
import ucm.appmenus.entities.Usuario;
import ucm.appmenus.recyclers.RecyclerAdapter;
import ucm.appmenus.recyclers.ViewHolderRestaurantes;
import ucm.appmenus.utils.Constantes;
import ucm.appmenus.utils.OpenStreetMap;
import ucm.appmenus.utils.OrdenarRestaurantes;

public class InicioFragment extends Fragment {

    private MainActivity mainActivity;
    private RecyclerAdapter recyclerAdapter;

    private TextView filtrosAplicados;
    private InicioViewModel inicioViewModel;
    private View root;

    //TODO: seguir este tutorial: https://androidwave.com/fragment-communication-using-viewmodel/

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        this.mainActivity = (MainActivity) getActivity();

        inicioViewModel = new ViewModelProvider(getActivity()).get(InicioViewModel.class);
        this.root = inflater.inflate(R.layout.fragment_inicio, container, false);

        root.findViewById(R.id.progressBarInicio).setVisibility(View.VISIBLE);
        filtrosAplicados = root.findViewById(R.id.descripcionFiltrosAplicados);

        //Usado para no hacer la busqueda cada vez que se abre el fragment
        //Si es distinto de null, es que en algun momento se han cargado restaurantes o que hay que cargarlos.
        if(getArguments() != null){
            /*Si esta marcado, significa que se ha llegado a este fragment desde el boton de "filtrar"
            en el fragment de filtros, por lo tanto hay que realizar una bsuqueda en OpenStreetMap
            con dichos filtros (guardados en getArguments() mediante un Bundle)*/
            if(getArguments().getBoolean(Constantes.ACTUALIZAR_INTENT)) {
                //Usado para cargar los datos de OpenStreetMap (ver funciones para mas informacion)
                OpenStreetMap osm = new OpenStreetMap();
                osm.setPlaces(inicioViewModel.getRestaurantes(), new OpenStreetMap.OpenStreetAttributes(
                        getArguments().getStringArrayList(Constantes.TIPOS_DIETA),
                        getArguments().getStringArrayList(Constantes.TIPOS_LOCAL),
                        getArguments().getStringArrayList(Constantes.TIPOS_COCINA),
                        getArguments().getInt(Constantes.AREA),
                        Usuario.getUsuario().getLocalizacion().latitude,
                        Usuario.getUsuario().getLocalizacion().longitude));
            }

            /*Sino, significa que se ha llegado a este fragmento mediante swipe desde uno de los otros fragmentos,
            pero previamente se habia realizado una busqueda y por lo tanto se recuperan los datos guardados
            en saveInstanceState mediante la funcion onSaveInstanceState(@NonNull Bundle outState)*/
            else{
                inicioViewModel.getRestaurantes().postValue(
                        savedInstanceState.<Restaurante>getParcelableArrayList(Constantes.LISTA_RESTAURANTES));
            }
            filtrosAplicados.setText("Buscando por: " + getArguments().getString(Constantes.FILTROS_BUSQUEDA));
        }
        /*Es la primera vez que se llega a este Fragment, por lo tanto no hay datos prebuscados ni filtros
        establecidos, asi que se realiza una bsuqueda basica preestablecida*/
        else {
            //TODO: Pedir los datos por defecto (los almacenados en la config inicial en un fichero) en vez de estos
            OpenStreetMap osm = new OpenStreetMap();
            osm.setPlaces(inicioViewModel.getRestaurantes(), new OpenStreetMap.OpenStreetAttributes(
                    new ArrayList<>(),
                    new ArrayList<String>(){{add("restaurant");}},
                    new ArrayList<>(),
                    1500,
                    Usuario.getUsuario().getLocalizacion().latitude,
                    Usuario.getUsuario().getLocalizacion().longitude));
        }

        //Actualiza el recycler cuando se reciben los datos
        final Observer<ArrayList<Restaurante>> observer = restaurantes -> {
            root.findViewById(R.id.progressBarInicio).setVisibility(View.INVISIBLE);
            recyclerAdapter = RecyclerAdapter.crearRecyclerLineal(restaurantes, ViewHolderRestaurantes.class, R.id.recyclerRestauranteInicio,
                    R.layout.recycler_restaurantes, root, LinearLayoutManager.VERTICAL);
        };
        inicioViewModel.getRestaurantes().observe(getActivity(), observer);

        RadioGroup rg = root.findViewById(R.id.radioGroupOrdenar);
        rg.setOnCheckedChangeListener((radioGroup, i) -> {
            if(i == R.id.radioPrecio)
                OrdenarRestaurantes.ordenarPorPrecio(inicioViewModel.getRestaurantes().getValue());
            else if(i == R.id.radioAbierto)
                OrdenarRestaurantes.ordenarPorApertura(inicioViewModel.getRestaurantes().getValue());
            else if(i == R.id.radioVegano)
                OrdenarRestaurantes.ordenarPorVegano(inicioViewModel.getRestaurantes().getValue());

            recyclerAdapter = RecyclerAdapter.crearRecyclerLineal(inicioViewModel.getRestaurantes().getValue(),
                    ViewHolderRestaurantes.class, R.id.recyclerRestauranteInicio,
                    R.layout.recycler_restaurantes, root, LinearLayoutManager.VERTICAL);
        });

        return root;
    }

    /**
     * Usado para guardar los datos cuando cambia el fragmento (por ejemplo se hace swipe al fragmento de
     * "filtros", asi no hace falta volver a hacer la busqueda en OpenStreetMap
     * @param outState variable donde se van a guardar los datos necesarios
     * TODO hacer que guarde bien el estado no va Xd:
     *                 https://stackoverflow.com/questions/42781409/restoring-fragment-state-when-changing-fragments-through-bottom-navigation-bar
     */
    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        Log.i("Salvo", "cosas");
        outState.putParcelableArrayList(Constantes.LISTA_RESTAURANTES, inicioViewModel.getRestaurantes().getValue());
        outState.putString(Constantes.FILTROS_BUSQUEDA, filtrosAplicados.getText().toString());
    }

    /*
    @Override
    public void onPrimaryNavigationFragmentChanged(boolean bool){
        super.onPrimaryNavigationFragmentChanged(bool);
        Log.i("info", "cambio frag");
    }

    @Override
    public void onPause() {
        super.onPause();
        Log.i("info", "pause");
    }

    @Override
    public void onStop() {
        super.onStop();
        Log.i("info", "stop");
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        Log.i("info", "destroy view");
    }

    @Override
    public  void onDestroy() {
        super.onDestroy();
        Log.i("info", "destroy");
    }*/
}
