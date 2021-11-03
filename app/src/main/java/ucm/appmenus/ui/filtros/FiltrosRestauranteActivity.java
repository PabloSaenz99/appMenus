package ucm.appmenus.ui.filtros;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.widget.TextView;

import java.util.ArrayList;

import ucm.appmenus.utils.BaseDatos;
import ucm.appmenus.R;
import ucm.appmenus.ui.inicio.RestauranteRecyclerAdapter;

public class FiltrosRestauranteActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_inicio);


        TextView texto = findViewById(R.id.textoDescripcionFiltrosAplicados);
        texto.setText("Filtros aplicados: ");

        //Datos psados desde la actividad creadora de esat
        ArrayList<String> query;
        if(getIntent().getExtras() != null &&
                (query = getIntent().getExtras().getStringArrayList("query")) != null) {
            BaseDatos bd = BaseDatos.getInstance();
            //Crear el recycler de los restaurantes
            RecyclerView recyclerViewRestaurantes = findViewById(R.id.recyclerRestauranteInicio);
            recyclerViewRestaurantes.setLayoutManager(new LinearLayoutManager(
                    getApplicationContext(), LinearLayoutManager.VERTICAL, false));
            //Crear el adapter y asignarlo
            RestauranteRecyclerAdapter adapterRestaurantes = new RestauranteRecyclerAdapter(bd.cargarRestaurantes());
            recyclerViewRestaurantes.setAdapter(adapterRestaurantes);
        }
    }
}