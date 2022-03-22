package ucm.appmenus.ui.inicio;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import ucm.appmenus.R;
import ucm.appmenus.ui.filtros.FiltrosFragment;

public class AniadirFiltrosActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //Utiliza el fragment de los filtros ya que basicamente hace lo mismo
        setContentView(R.layout.activity_aniadir_filtros);
        getSupportFragmentManager().beginTransaction()
                .setReorderingAllowed(true)
                .add(R.id.fragmentContainerView, FiltrosFragment.class, null)
                .commit();
    }
}