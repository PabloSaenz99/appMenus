package ucm.appmenus.ui.inicio;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import ucm.appmenus.utils.BaseDatos;
import ucm.appmenus.R;

public class InicioFragment extends Fragment {

    private InicioViewModel inicioViewModel;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        inicioViewModel =
                ViewModelProviders.of(this).get(InicioViewModel.class);
        //Inflater se usa cuando se quiere mostrar en bucle una cosa, por ejemplo para poner 100 fotos
        //En este caso se usa ya que hay 3 fragments(inicio, filtros y perfil), pero no es lo tipico
        View root = inflater.inflate(R.layout.fragment_inicio, container, false);

        BaseDatos bd = BaseDatos.getInstance();
        //Usado para que cargue la query inicial
        /*
        ManejadorFicheros mf = new ManejadorFicheros();
        String query = "";
        try {
            mf.abrirFicheroEntrada("");
            query = mf.leerLinea();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
         */

        TextView texto = root.findViewById(R.id.textoDescripcionFiltrosAplicados);
        texto.setText("Restaurantes cerca de ti");
        //Crear el recycler de los restaurantes
        RecyclerView recyclerViewRestaurantes = root.findViewById(R.id.recyclerRestauranteInicio);
        recyclerViewRestaurantes.setLayoutManager(new LinearLayoutManager(
                this.getContext(), LinearLayoutManager.VERTICAL, false));
        //Crear el adapter y asignarlo
        RestauranteRecyclerAdapter adapterRestaurantes = new RestauranteRecyclerAdapter(bd.cargarRestaurantes());
        recyclerViewRestaurantes.setAdapter(adapterRestaurantes);

        return root;
    }
}