package ucm.appmenus.ui.filtros;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import ucm.appmenus.MainActivity;
import ucm.appmenus.R;
import ucm.appmenus.recyclers.RecyclerAdapter;
import ucm.appmenus.recyclers.ViewHolderFiltros;
import ucm.appmenus.ui.inicio.InicioFragment;
import ucm.appmenus.utils.Pair;

public class FiltrosFragment extends Fragment {

    //https://wiki.openstreetmap.org/wiki/Category:Food_and_beverages
    public static final ArrayList<String> filtrosLocal = new ArrayList<String>(){{
        add("bar"); add("cafe"); add("fast+food"); add("nightclub"); add("pub"); add("restaurant");
    }};
    //Las 3 siguientes (comida, pais y postres) se podrian poner en una unica lista
    //https://wiki.openstreetmap.org/wiki/Key:cuisine
    public static final  ArrayList<String> filtrosComida = new ArrayList<String>(){{
        add("barbecue"); add("burger"); add("chicken"); add( "curry"); add("fish"); add("hot+dog");
        add("kebab"); add("noodle"); add("pasta"); add("pizza"); add("ramen"); add("sandwich");
        add("seafood"); add("steak-house"); add("sushi");  add("tapas");
    }};
    public static final ArrayList<String> filtrosPais = new ArrayList<String>(){{
        add("asian"); add("brazilian"); add("greek"); add("indian"); add("indonesian");
        add("italian"); add("japanese"); add("korean"); add("mediterranean"); add("mexican");
        add("spanish"); add("thai"); add("traditional");
    }};
    public static final ArrayList<String> filtrosPostres = new ArrayList<String>(){{
        add("cake"); add("coffe+shop"); add("crepe"); add("dessert"); add("ice+cream");
        add("waffle"); add("teahouse");
    }};
    //https://wiki.openstreetmap.org/wiki/Key:drink
    public static final String[] filtrosBebida = {};
    //https://wiki.openstreetmap.org/wiki/Key:diet
    public static final String[] filtrosDieta = {};

    private View root;

    private FiltrosViewModel filtrosViewModel;
    private ArrayList<RecyclerAdapter<ViewHolderFiltros, Pair<String, Boolean>>> listaRecyclers;

    public View onCreateView(@NonNull final LayoutInflater inflater,
                             final ViewGroup container, Bundle savedInstanceState) {
        filtrosViewModel = ViewModelProviders.of(this).get(FiltrosViewModel.class);
        root = inflater.inflate(R.layout.fragment_filtros, container, false);

        listaRecyclers = new ArrayList<RecyclerAdapter<ViewHolderFiltros, Pair<String, Boolean>>>();

        crearRecyclerFiltros(root, filtrosLocal, R.id.recyclerFiltrosLocal, 3);
        crearRecyclerFiltros(root, filtrosPais, R.id.recyclerFiltrosPais, 3);
        crearRecyclerFiltros(root, filtrosComida, R.id.recyclerFiltrosComida, 3);

        //Carga los filtros y realiza la busqueda
        Button botonFiltrar = root.findViewById(R.id.botonFiltrar);
        botonFiltrar.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                realizarBusqueda();
            }
        });

        return root;
    }

    public static ArrayList<Pair<String, Boolean>> transform(ArrayList<String> filtros, boolean modo){
        ArrayList<Pair<String, Boolean>> datos = new ArrayList<>();
        for (String s: filtros) {
            datos.add(new Pair<String, Boolean>(s, modo));
        }
        return datos;
    }

    private void crearRecyclerFiltros(final View root, ArrayList<String> filtros, int id, int nColums){
        RecyclerView recyclerViewRestaurantes = root.findViewById(id);
        recyclerViewRestaurantes.setLayoutManager(new GridLayoutManager(root.getContext(), nColums));

        RecyclerAdapter<ViewHolderFiltros, Pair<String, Boolean>> adapterFiltros =
                new RecyclerAdapter<ViewHolderFiltros, Pair<String, Boolean>>(
                        transform(filtros, true), R.layout.recycler_filtros, ViewHolderFiltros.class);
        recyclerViewRestaurantes.setAdapter(adapterFiltros);
        listaRecyclers.add(adapterFiltros);
    }

    private void realizarBusqueda(){
        MainActivity main = (MainActivity) getActivity();
        if(main != null) {
            //Obtiene la distancia
            RadioGroup rg = root.findViewById(R.id.radioGroupDistancia);
            RadioButton but = root.findViewById(rg.getCheckedRadioButtonId());
            int area = Integer.parseInt(but.getText().toString());

            //Primero busca los tipos de local
            ArrayList<String> tiposLocal = new ArrayList<String>();
            RecyclerAdapter<ViewHolderFiltros, Pair<String, Boolean>> vh = listaRecyclers.get(0);
            for (int i = 0; i < vh.size(); i++) {
                if (vh.get(i).getDatos().getSegundo()) {
                    tiposLocal.add(vh.get(i).getDatos().getPrimero());
                }
            }
            ArrayList<String> tiposCocina = new ArrayList<String>();
            //Luego recorre los demas recyclers que tienen los tipos de comida
            for (int i = 1; i < listaRecyclers.size(); i++) {
                RecyclerAdapter<ViewHolderFiltros, Pair<String, Boolean>> aux = listaRecyclers.get(i);
                for (int j = 0; j < aux.size(); j++) {
                    if (aux.get(j).getDatos().getSegundo()) {
                        tiposCocina.add(aux.get(j).getDatos().getPrimero());
                    }
                }
            }
            //Buscar resultados y abrir activity
            Bundle b = new Bundle();
            b.putBoolean("actualizar", true);
            b.putStringArrayList("tiposLocal", tiposLocal);
            b.putStringArrayList("tiposCocina", tiposCocina);
            b.putInt("area", area);

            main.changeFragment(R.id.navigation_inicio, b);
        }
    }
}