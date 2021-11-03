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
import ucm.appmenus.Restaurante;

public class FiltrosFragment extends Fragment {

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
        ArrayList<String> ejemplo2 = new ArrayList<String>();
        ejemplo2.add("Vegana");
        ejemplo2.add("Vegetariana");
        ejemplo2.add("Sin gluten");
        crearRecyclerFiltros(root, ejemplo2, R.id.recyclerFiltrosTipo, 3);

        ArrayList<String> ejemplo = new ArrayList<String>();
        ejemplo.add("Mexicana");
        ejemplo.add("China");
        ejemplo.add("Japonesa");
        ejemplo.add("Vietnamita");
        ejemplo.add("Argentina");
        ejemplo.add("Peruana");
        ejemplo.add("Alemana");
        ejemplo.add("Italiana");
        ejemplo.add("Vietnamita");
        crearRecyclerFiltros(root, ejemplo, R.id.recyclerFiltrosComida, 3);

        ArrayList<String> ejemplo3 = new ArrayList<String>();
        ejemplo3.add("Filtro1");ejemplo3.add("Filtro2");ejemplo3.add("Filtro3");ejemplo3.add("Filtro4");
        ejemplo3.add("Filtro5");ejemplo3.add("Filtro6");ejemplo3.add("Filtro7");ejemplo3.add("Filtro8");
        ejemplo3.add("Filtro9");ejemplo3.add("Filtro10");ejemplo3.add("Filtro11");ejemplo3.add("Filtro12");
        crearRecyclerFiltros(root, ejemplo3, R.id.recyclerFiltros3, 1);

        return root;
    }

    private void crearRecyclerFiltros(final View root, ArrayList<String> filtros, int id, int nColums){
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