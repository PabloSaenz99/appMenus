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
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import ucm.appmenus.R;
import ucm.appmenus.recyclers.RecyclerAdapter;
import ucm.appmenus.recyclers.ViewHolderFiltros;
import ucm.appmenus.ui.inicio.InicioFragment;
import ucm.appmenus.utils.Pair;

public class FiltrosFragment extends Fragment {

    //https://wiki.openstreetmap.org/wiki/Category:Food_and_beverages
    public static final ArrayList<String> filtrosLocal = new ArrayList<String>(){{
        add("bar"); add("cafe"); add("fast+food"); add("nightclub"); add("pub"); add("restaurant");}};
    //Las 3 siguientes (comida, pais y postres) se podrian poner en una misma lista
    //https://wiki.openstreetmap.org/wiki/Key:cuisine
    public static final  ArrayList<String> filtrosComida = new ArrayList<String>(){{
        add("barbecue"); add("brazilian"); add("burger"); add("chicken"); add( "curry");
        add("dessert"); add("fish"); add("friture"); add("hot+dog"); add("ice-cream"); add("kebab");
        add("noodle"); add("pasta"); add("pizza"); add("ramen"); add("sandwich"); add("sausage");
        add("seafood"); add("soap"); add("steak-house"); add("sushi");  add("tapas");}};
    public static final ArrayList<String> filtrosPais = new ArrayList<String>();
    public static final String[] filtrosPostres = {};
    //https://wiki.openstreetmap.org/wiki/Key:drink
    public static final String[] filtrosBebida = {};
    //https://wiki.openstreetmap.org/wiki/Key:diet
    public static final String[] filtrosDieta = {};

    private FiltrosViewModel filtrosViewModel;
    private ArrayList<ViewHolderFiltros> listaAdaptadores;

    public View onCreateView(@NonNull final LayoutInflater inflater,
                             final ViewGroup container, Bundle savedInstanceState) {
        filtrosViewModel =
                ViewModelProviders.of(this).get(FiltrosViewModel.class);
        View root = inflater.inflate(R.layout.fragment_filtros, container, false);

        listaAdaptadores = new ArrayList<ViewHolderFiltros>();

        Button botonFiltrar = root.findViewById(R.id.botonFiltrar);
        botonFiltrar.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                ArrayList<String> listaFiltros = new ArrayList<String>();
                for (ViewHolderFiltros adaptador: listaAdaptadores){
                    if(adaptador.getDatos().getSegundo())
                        listaFiltros.add(adaptador.getDatos().getPrimero());
                }
                //Buscar resultados y abrir activity
                Intent intent = new Intent(getActivity(), InicioFragment.class);
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

    private void crearRecyclerFiltros(final View root, ArrayList<String> filtros, int id, int nColums){
        RecyclerView recyclerViewRestaurantes = root.findViewById(id);
        recyclerViewRestaurantes.setLayoutManager(new GridLayoutManager(root.getContext(), nColums));

        RecyclerAdapter<ViewHolderFiltros, Pair<String, Boolean>> adapterRestaurantes =
                new RecyclerAdapter<ViewHolderFiltros, Pair<String, Boolean>>(
                        transform(filtros, true), R.layout.recycler_filtros, ViewHolderFiltros.class);
        recyclerViewRestaurantes.setAdapter(adapterRestaurantes);
    }

    public static ArrayList<Pair<String, Boolean>> transform(ArrayList<String> filtros, boolean modo){
        ArrayList<Pair<String, Boolean>> datos = new ArrayList<>();
        for (String s: filtros) {
            datos.add(new Pair<String, Boolean>(s, modo));
        }
        return datos;
    }
}