
package ucm.appmenus.ui.filtros;


import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
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

    private int metros = 0;

    private FiltrosViewModel filtrosViewModel;
    private ArrayList<RecyclerAdapter<ViewHolderFiltros, Pair<String, Boolean>>> listaRecyclers;

    public View onCreateView(@NonNull final LayoutInflater inflater,
                             final ViewGroup container, Bundle savedInstanceState) {
        filtrosViewModel = new ViewModelProvider(this).get(FiltrosViewModel.class);
        root = inflater.inflate(R.layout.fragment_filtros, container, false);

        calcularMetros(50);
        SeekBar barraDistancia = root.findViewById(R.id.seekBar);
        TextView textDistancia = root.findViewById(R.id.textDistancia);
        textDistancia.setText(metros + "m");
        barraDistancia.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                calcularMetros(i);
                textDistancia.setText(metros+ "m");
            }
            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {}
            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {}
        });

        //Crea los recyclers para los filtros, 3 recyclers para 3 tipos distintos de filtros
        listaRecyclers = new ArrayList<>();
        crearRecyclerFiltros(root, Constantes.traducirAlEsp(Constantes.filtrosLocalIngles()), R.id.recyclerFiltrosLocal, 3);
        crearRecyclerFiltros(root, Constantes.traducirAlEsp(Constantes.filtrosDietaOSMIngles()), R.id.recyclerDietaEspecial, 3);
        crearRecyclerFiltros(root, Constantes.traducirAlEsp(Constantes.filtrosPaisIngles()), R.id.recyclerFiltrosPais, 3);
        crearRecyclerFiltros(root, Constantes.traducirAlEsp(Constantes.filtrosComidaIngles()), R.id.recyclerFiltrosComida, 3);

        //Carga los filtros y realiza la busqueda
        Button botonFiltrar = root.findViewById(R.id.botonFiltrar);
        botonFiltrar.setOnClickListener(v -> {
            accionBoton(savedInstanceState);
            botonFiltrar.setBackgroundColor(root.getResources().getColor(R.color.botonDesactivado));
            botonFiltrar.setBackgroundColor(root.getResources().getColor(R.color.botonActivado));
        });

        /*
        Button botonAbrirMapa = root.findViewById(R.id.botonVerMapa);
        botonAbrirMapa.setOnClickListener(view -> {
            RadioButton rb = root.findViewById(R.id.radioButtonMapa);
            rb.setChecked(true);
            startActivity(new Intent(view.getContext(), MapActivity.class));
            botonAbrirMapa.setBackgroundColor(root.getResources().getColor(R.color.botonDesactivado));
            botonAbrirMapa.setBackgroundColor(root.getResources().getColor(R.color.botonActivado));
        });
         */

        //Si la clase que contiene este fragment es las clasde de AñadirFiltros, entonces se cambian algunas vistas
        if(getActivity() instanceof AniadirFiltrosActivity){
            TextView info = root.findViewById(R.id.textInfoFiltros);
            info.setText("Selecciona un filtro para añadirlo");
            botonFiltrar.setText("Añadir filtros");
            root.findViewById(R.id.layoutSeekbar).setVisibility(View.GONE);
        }

        return root;
    }

    private void calcularMetros(int i){
        metros = 500 + (2500*i)/100;
    }

    public static ArrayList<Pair<String, Boolean>> transform(Set<String> filtros, boolean modo){
        ArrayList<Pair<String, Boolean>> datos = new ArrayList<>();
        for (String s: filtros) {
            if(s != null)
                datos.add(new Pair<String, Boolean>(s, modo));
        }
        return datos;
    }

    public static ArrayList<Pair<String, Boolean>> transform(List<String> filtros, boolean modo){
        ArrayList<Pair<String, Boolean>> datos = new ArrayList<>();
        for (String s: filtros) {
            if(s != null)
                datos.add(new Pair<>(s, modo));
        }
        return datos;
    }

    private void crearRecyclerFiltros(final View root, List<String> filtros, int id, int nColums){
        listaRecyclers.add(RecyclerAdapter.crearRecyclerGrid(FiltrosFragment.transform(filtros, true),
                ViewHolderFiltros.class, id, R.layout.recycler_filtros, root, nColums));
    }

    private void accionBoton(Bundle savedInstanceState){
        //Primero busca los tipos de local
        ArrayList<String> tiposLocal = new ArrayList<>();
        RecyclerAdapter<ViewHolderFiltros, Pair<String, Boolean>> vhLocal = listaRecyclers.get(0);
        for (int i = 0; i < vhLocal.size(); i++) {
            if (vhLocal.get(i).getDatos().getSegundo()) {
                tiposLocal.add(vhLocal.get(i).getDatos().getPrimero());
            }
        }

        //Segundo busca los tipos de dieta
        ArrayList<String> tiposDieta = new ArrayList<>();
        RecyclerAdapter<ViewHolderFiltros, Pair<String, Boolean>> vhDieta = listaRecyclers.get(1);
        for (int i = 0; i < vhDieta.size(); i++) {
            if (vhDieta.get(i).getDatos().getSegundo()) {
                tiposDieta.add(vhDieta.get(i).getDatos().getPrimero());
            }
        }

        //Luego recorre los demas recyclers que tienen los tipos de comida
        ArrayList<String> tiposCocina = new ArrayList<>();
        for (int i = 2; i < listaRecyclers.size(); i++) {
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
            Log.i("res", Constantes.traducirAlEsp(tiposLocal).toString());
            Log.i("res2", tiposLocal.toString());
            b.putStringArrayList(Constantes.TIPOS_DIETA, Constantes.traducirAlIngles(tiposDieta));
            b.putStringArrayList(Constantes.TIPOS_LOCAL, Constantes.traducirAlIngles(tiposLocal));
            b.putStringArrayList(Constantes.TIPOS_COCINA, Constantes.traducirAlIngles(tiposCocina));
            b.putBoolean(Constantes.ACTUALIZAR_INTENT, true);
            b.putInt(Constantes.AREA, metros);
            b.putString(Constantes.FILTROS_BUSQUEDA, tiposLocal + tiposCocina.toString() + "<" + metros + ">");

            Navigation.findNavController(root).navigate(R.id.navigation_inicio, b);
        } else if(act instanceof AniadirFiltrosActivity) {      //Guarda los filtros en la BD
            Toast.makeText(act, "Filtros añadidos", Toast.LENGTH_SHORT).show();
            tiposLocal.addAll(Constantes.traducirAlEsp(tiposCocina));
            BaseDatos.getInstance().addFiltrosRestaurante(getArguments().getString(Constantes.RESTAURANTE), tiposLocal);
            act.finish();
        }
        //TODO: hacer para si se llama desde el login
        else{

        }
    }
}