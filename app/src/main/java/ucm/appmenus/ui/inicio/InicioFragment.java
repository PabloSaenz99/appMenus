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

        //Ejemplo: asigno la imagen, texto y checkbox a una bariable y objeto
        ImageView imagenEjemplo = root.findViewById(R.id.ejemploImageView);
        TextView textoEjemplo = root.findViewById(R.id.ejemploTextView);
        CheckBox checkBoxEjemplo = root.findViewById(R.id.ejemploCheckBox);
        
        textoEjemplo.setText("Texto 1");

        return root;
    }
}