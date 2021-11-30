package ucm.appmenus.ui.inicio;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.ArrayList;

import ucm.appmenus.BuildConfig;
import ucm.appmenus.entities.Restaurante;

public class InicioViewModel extends ViewModel {

    private MutableLiveData<ArrayList<Restaurante>> restaurantes;

    public InicioViewModel() {
        restaurantes = new MutableLiveData<ArrayList<Restaurante>>();
    }

    public MutableLiveData<ArrayList<Restaurante>> getRestaurantes() { return restaurantes; }
}