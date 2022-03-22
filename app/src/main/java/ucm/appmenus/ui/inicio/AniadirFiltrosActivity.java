package ucm.appmenus.ui.inicio;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import java.util.ArrayList;

import ucm.appmenus.R;
import ucm.appmenus.recyclers.RecyclerAdapter;
import ucm.appmenus.recyclers.ViewHolderFiltros;
import ucm.appmenus.ui.filtros.FiltrosFragment;
import ucm.appmenus.utils.Constantes;
import ucm.appmenus.utils.Pair;

public class AniadirFiltrosActivity extends AppCompatActivity {

    private ArrayList<RecyclerAdapter<ViewHolderFiltros, Pair<String, Boolean>>> listaRecyclers;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_aniadir_filtros);

        //Crea los recyclers para los filtros, 3 recyclers para 3 tipos distintos de filtros
        View v = getWindow().getDecorView().getRootView();
        listaRecyclers = new ArrayList<>();
        listaRecyclers.add(RecyclerAdapter.crearRecyclerGrid(FiltrosFragment.transform(Constantes.filtrosLocal, true),
                ViewHolderFiltros.class, R.id.recyclerFiltrosLocal, R.layout.recycler_filtros, v, 3));
        listaRecyclers.add(RecyclerAdapter.crearRecyclerGrid(FiltrosFragment.transform(Constantes.filtrosPais, true),
                ViewHolderFiltros.class, R.id.recyclerFiltrosPais, R.layout.recycler_filtros, v, 3));
        listaRecyclers.add(RecyclerAdapter.crearRecyclerGrid(FiltrosFragment.transform(Constantes.filtrosComida, true),
                ViewHolderFiltros.class, R.id.recyclerFiltrosComida, R.layout.recycler_filtros, v, 3));

        Button botonFiltros = findViewById(R.id.botonEditarFiltros);
        botonFiltros.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //TODO: coger los filtros y añadirlos a la bd como experimentales
                finish();
            }
        });
    }
}