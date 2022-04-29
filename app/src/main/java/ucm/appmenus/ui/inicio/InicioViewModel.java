package ucm.appmenus.ui.inicio;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.SavedStateHandle;
import androidx.lifecycle.ViewModel;

import java.util.ArrayList;

import ucm.appmenus.entities.Restaurante;
import ucm.appmenus.utils.Constantes;

/**
 * Clase que gaurda la informacion de los restaurantes buscados rpeviamente, para no tener que realizar
 * una busqueda cada vez que se cambia de fragment (por ejemplo al hacer swipe)
 * */
public class InicioViewModel extends ViewModel {

    private static MutableLiveData<ArrayList<Restaurante>> restaurantes;

    public InicioViewModel() {
        restaurantes = new MutableLiveData<ArrayList<Restaurante>>();
    }
    public InicioViewModel(SavedStateHandle savedStateHandle) {
        restaurantes = savedStateHandle.getLiveData(Constantes.LISTA_RESTAURANTES);
    }

    public MutableLiveData<ArrayList<Restaurante>> getRestaurantes() { return restaurantes; }
}