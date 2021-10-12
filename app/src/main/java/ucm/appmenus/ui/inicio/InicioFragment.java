package ucm.appmenus.ui.inicio;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import ucm.appmenus.R;
import ucm.appmenus.ui.filtros.FiltrosRecyclerAdapter;

public class InicioFragment extends Fragment {

    private InicioViewModel inicioViewModel;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        inicioViewModel =
                ViewModelProviders.of(this).get(InicioViewModel.class);
        //Inflater se usa cuando se quiere mostrar en bucle una cosa, por ejemplo para poner 100 fotos
        //En este caso se usa ya que hay 3 fragments(inicio, filtros y perfil), pero no es lo tipico
        View root = inflater.inflate(R.layout.fragment_inicio, container, false);

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
        FiltrosRecyclerAdapter adapter = new FiltrosRecyclerAdapter(ejemplo);
        //Set the adapter
        recyclerViewComidas.setAdapter(adapter);


        return root;
    }
}