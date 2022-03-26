package ucm.appmenus.ui.filtros;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class FiltrosViewModel extends ViewModel {

    private MutableLiveData<String> mText;

    /**
     * Esto no hace nada, podria guardar los filtros de la biquefa realizada previamente, pero no es util de momento
     */
    public FiltrosViewModel() {
        mText = new MutableLiveData<>();
    }

    public LiveData<String> getText() {
        return mText;
    }
}