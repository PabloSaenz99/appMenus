package ucm.appmenus.ui.filtros;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.GridLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import ucm.appmenus.R;

public class FiltrosFragment extends Fragment {

    private FiltrosViewModel filtrosViewModel;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        filtrosViewModel =
                ViewModelProviders.of(this).get(FiltrosViewModel.class);
        View root = inflater.inflate(R.layout.fragment_filtros, container, false);
        filtrosViewModel.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                //textView.setText(s);
            }
        });

        Button angryButton = root.findViewById(R.id.botonFiltrar);
        angryButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

            }
        });

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
        crearRecycler(root, ejemplo, R.id.recyclerFiltrosComida, 3);

        ArrayList<String> ejemplo2 = new ArrayList<String>();
        ejemplo2.add("Vegana");
        ejemplo2.add("Vegetariana");
        ejemplo2.add("Sin gluten");
        crearRecycler(root, ejemplo2, R.id.recyclerFiltrosTipo, 3);

        ArrayList<String> ejemplo3 = new ArrayList<String>();
        ejemplo3.add("Filtro1");ejemplo3.add("Filtro2");ejemplo3.add("Filtro3");ejemplo3.add("Filtro4");
        //ejemplo3.add("Filtro5");ejemplo3.add("Filtro6");ejemplo3.add("Filtro7");ejemplo3.add("Filtro8");
        //ejemplo3.add("Filtro9");ejemplo3.add("Filtro10");ejemplo3.add("Filtro11");ejemplo3.add("Filtro12");
        crearRecycler(root, ejemplo3, R.id.recyclerFiltros3, 1);

        return root;
    }

    private void crearRecycler(final View root, ArrayList<String> filtros, int id, int nColums){
        //Crear el recycler
        RecyclerView recyclerView = root.findViewById(id);
        recyclerView.setLayoutManager(new GridLayoutManager(root.getContext(), nColums));
        //Crear adapter
        FiltrosRecyclerAdapter adapterTipos = new FiltrosRecyclerAdapter(filtros, false);
        //Set the adapter
        recyclerView.setAdapter(adapterTipos);
    }
}