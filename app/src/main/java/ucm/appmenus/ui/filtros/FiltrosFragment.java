
package ucm.appmenus.ui.filtros;


import android.os.Bundle;
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
import java.util.List;
import java.util.Set;

import ucm.appmenus.MainActivity;
import ucm.appmenus.R;
import ucm.appmenus.recyclers.RecyclerAdapter;
import ucm.appmenus.recyclers.ViewHolderFiltros;
import ucm.appmenus.utils.Constantes;
import ucm.appmenus.utils.Pair;

public class FiltrosFragment extends Fragment {

    private View root;

    private FiltrosViewModel filtrosViewModel;
    private ArrayList<RecyclerAdapter<ViewHolderFiltros, Pair<String, Boolean>>> listaRecyclers;

    public View onCreateView(@NonNull final LayoutInflater inflater,
                             final ViewGroup container, Bundle savedInstanceState) {
        filtrosViewModel = ViewModelProviders.of(this).get(FiltrosViewModel.class);
        root = inflater.inflate(R.layout.fragment_filtros, container, false);

        //Crea los recyclers para los filtros, 3 recyclers para 3 tipos distintos de filtros
        listaRecyclers = new ArrayList<RecyclerAdapter<ViewHolderFiltros, Pair<String, Boolean>>>();
        crearRecyclerFiltros(root, Constantes.filtrosLocal, R.id.recyclerFiltrosLocal, 3);
        crearRecyclerFiltros(root, Constantes.filtrosPais, R.id.recyclerFiltrosPais, 3);
        crearRecyclerFiltros(root, Constantes.filtrosComida, R.id.recyclerFiltrosComida, 3);

        //Carga los filtros y realiza la busqueda
        Button botonFiltrar = root.findViewById(R.id.botonFiltrar);
        botonFiltrar.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                realizarBusqueda();
            }
        });

        return root;
    }

    public static ArrayList<Pair<String, Boolean>> transform(Set<String> filtros, boolean modo){
        ArrayList<Pair<String, Boolean>> datos = new ArrayList<>();
        for (String s: filtros) {
            datos.add(new Pair<String, Boolean>(s, modo));
        }
        return datos;
    }

    public static ArrayList<Pair<String, Boolean>> transform(List<String> filtros, boolean modo){
        ArrayList<Pair<String, Boolean>> datos = new ArrayList<>();
        for (String s: filtros) {
            datos.add(new Pair<String, Boolean>(s, modo));
        }
        return datos;
    }

    private void crearRecyclerFiltros(final View root, List<String> filtros, int id, int nColums){
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
            //Obtiene la distancia de los radioButtons (solo permiten seleccionar uno de los tres)
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

            //Luego recorre los demas recyclers que tienen los tipos de comida
            ArrayList<String> tiposCocina = new ArrayList<String>();
            for (int i = 1; i < listaRecyclers.size(); i++) {
                RecyclerAdapter<ViewHolderFiltros, Pair<String, Boolean>> aux = listaRecyclers.get(i);
                for (int j = 0; j < aux.size(); j++) {
                    if (aux.get(j).getDatos().getSegundo()) {
                        tiposCocina.add(aux.get(j).getDatos().getPrimero());
                    }
                }
            }
            /*
            Guarda los filtros de la busqueda en un bundle y abre el fragment de inicio en modo
            busqueda (ahi se realiza la busqueda en OpenStreetMap)
             */
            Bundle b = new Bundle();
            b.putBoolean(Constantes.ACTUALIZAR_INTENT, true);
            b.putStringArrayList(Constantes.TIPOS_LOCAL, tiposLocal);
            b.putStringArrayList(Constantes.TIPOS_COCINA, tiposCocina);
            b.putInt(Constantes.AREA, area);

            main.changeFragment(R.id.navigation_inicio, b);
        }
    }
}