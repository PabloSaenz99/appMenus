package ucm.appmenus.ui.filtros;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

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


        //Crear el recycler de los filtros
        RecyclerView recyclerView = root.findViewById(R.id.recyclerFiltros);
        recyclerView.setLayoutManager(new LinearLayoutManager(
                this.getContext(), LinearLayoutManager.HORIZONTAL, false));
        //Create adapter
        //Arraylist de ejemplo
        ArrayList<String> ejemplo = new ArrayList<String>();
        ejemplo.add("Mexicana");
        ejemplo.add("Asiatica");
        ejemplo.add("Australiana");
        FiltrosRecyclerAdapter adapter = new FiltrosRecyclerAdapter(ejemplo);
        //Set the adapter
        recyclerView.setAdapter(adapter);

        return root;
    }
}