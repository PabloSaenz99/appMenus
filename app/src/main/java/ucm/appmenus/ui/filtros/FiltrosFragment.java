package ucm.appmenus.ui.filtros;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
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


        //Crear el recycler de los filtros por comidas (incluye en el xml un scrollbar)
        RecyclerView recyclerViewComidas = root.findViewById(R.id.recyclerFiltrosComida);
        recyclerViewComidas.setLayoutManager(new LinearLayoutManager(
                this.getContext(), LinearLayoutManager.VERTICAL, false));
        //Create adapter
        //Arraylist de ejemplo
        ArrayList<String> ejemplo = new ArrayList<String>();
        ejemplo.add("Mexicana");
        ejemplo.add("China");
        ejemplo.add("Japonesa");
        ejemplo.add("Vietnamita");
        ejemplo.add("Argentina");
        ejemplo.add("Peruana");
        ejemplo.add("Alemana");
        ejemplo.add("Italiana");
        ejemplo.add("Hungara");
        FiltrosRecyclerAdapter adapter = new FiltrosRecyclerAdapter(ejemplo, false);
        //Set the adapter
        recyclerViewComidas.setAdapter(adapter);

        //Crear el recycler de los filtros por tipo de comida
        RecyclerView recyclerViewTipos = root.findViewById(R.id.recyclerFiltrosTipo);
        recyclerViewTipos.setLayoutManager(new LinearLayoutManager(
                this.getContext(), LinearLayoutManager.HORIZONTAL, false));
        //Create adapter
        //Arraylist de ejemplo
        ArrayList<String> ejemplo2 = new ArrayList<String>();
        ejemplo2.add("Vegana");
        ejemplo2.add("Vegetariana");
        ejemplo2.add("Sin gluten");
        FiltrosRecyclerAdapter adapterTipos = new FiltrosRecyclerAdapter(ejemplo2, false);
        //Set the adapter
        recyclerViewTipos.setAdapter(adapterTipos);

        return root;
    }
}