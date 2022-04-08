
package ucm.appmenus.ui.filtros;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.Navigation;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import ucm.appmenus.MainActivity;
import ucm.appmenus.R;
import ucm.appmenus.recyclers.RecyclerAdapter;
import ucm.appmenus.recyclers.ViewHolderFiltros;
import ucm.appmenus.ui.inicio.AniadirFiltrosActivity;
import ucm.appmenus.utils.BaseDatos;
import ucm.appmenus.utils.Constantes;
import ucm.appmenus.utils.Pair;

public class FiltrosFragment extends Fragment {

    private View root;

    private FiltrosViewModel filtrosViewModel;
    private ArrayList<RecyclerAdapter<ViewHolderFiltros, Pair<String, Boolean>>> listaRecyclers;

    public View onCreateView(@NonNull final LayoutInflater inflater,
                             final ViewGroup container, Bundle savedInstanceState) {
        filtrosViewModel = new ViewModelProvider(this).get(FiltrosViewModel.class);
        root = inflater.inflate(R.layout.fragment_filtros, container, false);

        //Crea los recyclers para los filtros, 3 recyclers para 3 tipos distintos de filtros
        listaRecyclers = new ArrayList<>();
        crearRecyclerFiltros(root, Constantes.filtrosLocal, R.id.recyclerFiltrosLocal, 3);
        crearRecyclerFiltros(root, Constantes.filtrosPais, R.id.recyclerFiltrosPais, 3);
        crearRecyclerFiltros(root, Constantes.filtrosComida, R.id.recyclerFiltrosComida, 3);

        //Carga los filtros y realiza la busqueda
        Button botonFiltrar = root.findViewById(R.id.botonFiltrar);
        botonFiltrar.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                accionBoton(savedInstanceState);
            }
        });

        Button botonAbrirMapa = root.findViewById(R.id.botonVerMapa);
        botonAbrirMapa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                RadioButton rb = root.findViewById(R.id.radioButtonMapa);
                rb.setChecked(true);
                startActivity(new Intent(view.getContext(), MapActivity.class));
            }
        });

        //Si la clase que contiene este fragment es las clasde de AñadirFiltros, entonces se cambian algunas vistas
        if(getActivity() instanceof AniadirFiltrosActivity){
            TextView info = root.findViewById(R.id.textInfoFiltros);
            info.setText("Selecciona un filtro para añadirlo");
            botonFiltrar.setText("Añadir filtros");
            root.findViewById(R.id.radioGroupDistancia).setVisibility(View.GONE);
        }

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
            datos.add(new Pair<>(s, modo));
        }
        return datos;
    }

    private void crearRecyclerFiltros(final View root, List<String> filtros, int id, int nColums){
        listaRecyclers.add(RecyclerAdapter.crearRecyclerGrid(FiltrosFragment.transform(filtros, true),
                ViewHolderFiltros.class, id, R.layout.recycler_filtros, root, nColums));
    }

    private void accionBoton(Bundle savedInstanceState){
        //Obtiene la distancia de los radioButtons (solo permiten seleccionar uno de los tres)
        RadioGroup rg = root.findViewById(R.id.radioGroupDistancia);
        RadioButton but = root.findViewById(rg.getCheckedRadioButtonId());
        int area = 1500;
        if(but.getId() == R.id.radioButtonMapa){
            //TODO: coger los datos del mapa
        }
        else {
            area = Integer.parseInt(but.getText().toString());
        }

        //Primero busca los tipos de local
        ArrayList<String> tiposLocal = new ArrayList<String>();
        RecyclerAdapter<ViewHolderFiltros, Pair<String, Boolean>> vh = listaRecyclers.get(0);
        for (int i = 0; i < vh.size(); i++) {
            if (vh.get(i).getDatos().getSegundo()) {
                tiposLocal.add(vh.get(i).getDatos().getPrimero());
            }
        }

        //Luego recorre los demas recyclers que tienen los tipos de comida
        ArrayList<String> tiposCocina = new ArrayList<>();
        for (int i = 1; i < listaRecyclers.size(); i++) {
            RecyclerAdapter<ViewHolderFiltros, Pair<String, Boolean>> aux = listaRecyclers.get(i);
            for (int j = 0; j < aux.size(); j++) {
                if (aux.get(j).getDatos().getSegundo()) {
                    tiposCocina.add(aux.get(j).getDatos().getPrimero());
                }
            }
        }

        Activity act = getActivity();
        if(act instanceof MainActivity) { //abre el fragment de inicio en modo busqueda (ahi se realiza la busqueda en OpenStreetMap)
            //Guarda los filtros de la busqueda en un bundle
            Bundle b = new Bundle();
            b.putStringArrayList(Constantes.TIPOS_LOCAL, tiposLocal);
            b.putStringArrayList(Constantes.TIPOS_COCINA, tiposCocina);
            b.putBoolean(Constantes.ACTUALIZAR_INTENT, true);
            b.putInt(Constantes.AREA, area);
            b.putString(Constantes.FILTROS_BUSQUEDA, tiposLocal + tiposCocina.toString() + "<" + area + ">");

            Navigation.findNavController(root).navigate(R.id.navigation_inicio, b);
        } else if(act instanceof AniadirFiltrosActivity) {
            //TODO: coger los arrays (tiposLocal y tiposCocina y hacer que se guarden en la BD
            Toast.makeText(act, "Filtros añadidos", Toast.LENGTH_SHORT).show();
            tiposLocal.addAll(tiposCocina);
            BaseDatos.getInstance().addFiltrosRestaurante(getArguments().getString(Constantes.RESTAURANTE), tiposLocal);
            act.finish();
        }
        //TODO: hacer para si se llama desde el login
        else{

        }
    }
}