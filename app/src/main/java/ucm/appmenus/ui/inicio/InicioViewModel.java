package ucm.appmenus.ui.inicio;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.SavedStateHandle;
import androidx.lifecycle.ViewModel;

import java.util.ArrayList;

import ucm.appmenus.BuildConfig;
import ucm.appmenus.entities.Restaurante;

public class InicioViewModel extends ViewModel {

    private static MutableLiveData<ArrayList<Restaurante>> restaurantes;

    public InicioViewModel() {
        restaurantes = new MutableLiveData<ArrayList<Restaurante>>();
    }
    public InicioViewModel(SavedStateHandle savedStateHandle) {
        Log.i("ESTADO", "Recuperando-ViewModel");
        restaurantes = savedStateHandle.getLiveData("listaResaturantes");
    }


    public MutableLiveData<ArrayList<Restaurante>> getRestaurantes() { return restaurantes; }
}