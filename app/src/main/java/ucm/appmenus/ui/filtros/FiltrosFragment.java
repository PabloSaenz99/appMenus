package ucm.appmenus.ui.filtros;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import ucm.appmenus.R;
import ucm.appmenus.recyclers.FiltrosRecyclerAdapter;

public class FiltrosFragment extends Fragment {

    //https://wiki.openstreetmap.org/wiki/Category:Food_and_beverages
    public static final String[] filtrosLocal = {"bar","cafe", "fast+food", "nightclub", "pub", "restaurant"};
    //Las 3 siguientes (comida, pais y postres) se podrian poner en una misma lista
    //https://wiki.openstreetmap.org/wiki/Key:cuisine
    public static final String[] filtrosComida = {"barbecue", "brazilian", "burger", "chicken",
            "curry", "dessert", "fish", "friture", "hot+dog", "ice-cream", "kebab", "noodle", "pasta",
            "pizza", "ramen", "sandwich", "sausage", "seafood", "soap", "steak-house", "sushi",
            "tapas", "waffle"};
    public static final String[] filtrosPais = {};
    public static final String[] filtrosPostres = {};
    //https://wiki.openstreetmap.org/wiki/Key:drink
    public static final String[] filtrosBebida = {};
    //https://wiki.openstreetmap.org/wiki/Key:diet
    public static final String[] filtrosDieta = {};

    private FiltrosViewModel filtrosViewModel;
    private ArrayList<FiltrosRecyclerAdapter> listaAdaptadores;

    public View onCreateView(@NonNull final LayoutInflater inflater,
                             final ViewGroup container, Bundle savedInstanceState) {
        filtrosViewModel =
                ViewModelProviders.of(this).get(FiltrosViewModel.class);
        View root = inflater.inflate(R.layout.fragment_filtros, container, false);

        listaAdaptadores = new ArrayList<FiltrosRecyclerAdapter>();

        Button botonFiltrar = root.findViewById(R.id.botonFiltrar);
        botonFiltrar.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                ArrayList<String> listaFiltros = new ArrayList<String>();
                for (FiltrosRecyclerAdapter adaptador: listaAdaptadores){
                    listaFiltros.addAll(adaptador.getDatos());
                }
                //Buscar resultados y abrir activity
                Intent intent = new Intent(getActivity(), FiltrosRestauranteActivity.class);
                intent.putStringArrayListExtra("query", listaFiltros);
                startActivity(intent);
            }
        });
        /**
         * CUIDADO: EL ORDEN DE INSERCION DE LOS FILTROS ES IMPORTANTE PARA LA QUERY
         * */
        crearRecyclerFiltros(root, filtrosLocal, R.id.recyclerFiltrosLocal, 3);
        crearRecyclerFiltros(root, filtrosComida, R.id.recyclerFiltrosPais, 3);
        crearRecyclerFiltros(root, filtrosPais, R.id.recyclerFiltros3, 1);

        return root;
    }

    private void crearRecyclerFiltros(final View root, String[] filtros, int id, int nColums){
        //Crear el recycler
        RecyclerView recyclerView = root.findViewById(id);
        recyclerView.setLayoutManager(new GridLayoutManager(root.getContext(), nColums));
        //Crear adapter
        FiltrosRecyclerAdapter adapterTipos = new FiltrosRecyclerAdapter(filtros, false);
        listaAdaptadores.add(adapterTipos);
        //Set the adapter
        recyclerView.setAdapter(listaAdaptadores.get(listaAdaptadores.size() - 1));
    }
}