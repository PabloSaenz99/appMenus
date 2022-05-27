package ucm.appmenus.recyclers;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import androidx.annotation.IdRes;
import androidx.annotation.LayoutRes;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;

import ucm.appmenus.R;
import ucm.appmenus.utils.Pair;

/**
 * Clase usada para mostrar un conjunto de datos, usada para generar vistas en tiempo de ejecucion.
 * @param <ELEMENT> Los datos que se van a almacenar y mostar en bucle
 *      @see IReclycerElement
 * @param <ViewHolder> La clase que almacena los datos de ELEMENT, debe extender
 *      a RecyclerView.ViewHolder e implementar IReclycerElement
 *      IMPORTANTE: Su unico constructor debe tener por unico parametro View, para añadir datos
 *          extra, pasarlos por setDatos
 *      @see ViewHolderNULL para un ejemplo muy basico
 * IMPORTANTE: Esta clase no debe ser modificada
 * */
public class RecyclerAdapter<ViewHolder extends RecyclerView.ViewHolder & IReclycerElement<ELEMENT>, ELEMENT>
        extends RecyclerView.Adapter<ViewHolder> {

    /**
     * Clase estatica que crea y devuelve un recycler lineal (creada para no repetir codigo)
     * @param elementos lista con los elementos a mostrar en el recycler
     * @param viewHolder clase encargada de soportar los datos del recycler
     * @param idRecycler identificador del recycler en la vista donde se llama a esta funcion
     * @param idLayout identificador del layout donde se llama a esta funcion
     * @param v vista de la clase donde se llama a esta funcion
     * @param orientacion orientacion del layout, vertical u horizontal
     * @param <T> ViewHolder encargado de gestionar los datos proporcionados
     * @param <ELEMENT> tipo de dato proporcionado
     * @return el adaptador creado
     */
    public static <T extends RecyclerView.ViewHolder & IReclycerElement<ELEMENT>, ELEMENT> RecyclerAdapter<T, ELEMENT> crearRecyclerLineal(
            List<ELEMENT> elementos, Class<T> viewHolder, @IdRes int idRecycler, @LayoutRes int idLayout, View v,
            @RecyclerView.Orientation int orientacion){
        RecyclerView recyclerView = v.findViewById(idRecycler);
        recyclerView.setLayoutManager(new LinearLayoutManager(v.getContext(), orientacion, false));

        RecyclerAdapter<T, ELEMENT> adapter = new RecyclerAdapter<T, ELEMENT>(elementos, idLayout, viewHolder, recyclerView);
        recyclerView.setAdapter(adapter);
        //return recyclerView;
        return adapter;
    }

    /**
     * Clase estatica que crea y devuelve un recycler de cuadricula(creada para no repetir codigo)
     * @param elementos lista con los elementos a mostrar en el recycler
     * @param viewHolder clase encargada de soportar los datos del recycler
     * @param idRecycler identificador del recycler en la vista donde se llama a esta funcion
     * @param idLayout identificador del layout donde se llama a esta funcion
     * @param view vista de la clase donde se llama a esta funcion
     * @param nColums numero de columnas que tendrá el layout
     * @param <T> ViewHolder encargado de gestionar los datos proporcionados
     * @param <ELEMENT> tipo de dato proporcionado
     * @return el adaptador creado
     */
    public static <T extends RecyclerView.ViewHolder & IReclycerElement<ELEMENT>, ELEMENT> RecyclerAdapter<T, ELEMENT> crearRecyclerGrid(
            List<ELEMENT> elementos, Class<T> viewHolder, @IdRes int idRecycler, @LayoutRes int idLayout, View view,
            int nColums){
        RecyclerView recyclerView = view.findViewById(idRecycler);
        recyclerView.setLayoutManager(new GridLayoutManager(view.getContext(), nColums));

        RecyclerAdapter<T, ELEMENT> adapter = new RecyclerAdapter<>(elementos, idLayout, viewHolder, recyclerView);
        recyclerView.setAdapter(adapter);
        //return recyclerView;
        return adapter;
    }

    private final int viewID;
    private final RecyclerView recyclerView;
    private final List<ELEMENT> listaDatos;
    private final List<ViewHolder> holders;
    private final Class<ViewHolder> clase;

    /**
     * @param dataSet : lista con los datos que se quieren mostar en bucle
     * @param viewID : el id (R.layout...) del elemento a representar en bucle
     * @param clase : la clase que implementa  RecyclerView.ViewHolder y extiende IReclycerElement
     * */
    private RecyclerAdapter(List<ELEMENT> dataSet, int viewID, Class<ViewHolder> clase, RecyclerView recyclerView) {
        this.listaDatos = dataSet;
        this.viewID = viewID;
        this.clase = clase;
        this.recyclerView = recyclerView;
        this.holders = new ArrayList<>();
    }

    /**
     * Obtiene el ViewHolder de la posición i
     * @param i posicioón (salta una excepción si no existe)
     * @return ViewHolder en esa posición
     */
    public ViewHolder get(int i){ return holders.get(i); }

    /**
     * @return número de viewHolders en este recycler
     */
    public int size(){ return holders.size(); }

    /**
     * Establece el máximo tamaño del recycler
     * ViewGroup.LayoutParams.WRAP_CONTENT para ajustarlo al ancho
     * ViewGroup.LayoutParams.MATCH_PARENT para ajustarlo al tamaño máximo del layout
     * @param width ancho máximo del recycler
     * @param height alto máximo del recycler
     */
    public void setMax(int width, int height){
        /*RecyclerView.LayoutParams params = new RecyclerView.LayoutParams(width, height);
        recyclerView.setLayoutParams(params);*/
        if(recyclerView.getLayoutParams() != null) {
            recyclerView.getLayoutParams().height = height;
            recyclerView.getLayoutParams().width = width;
        }
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(viewID, parent, false);
        try {
            ViewHolder v = clase.getConstructor(View.class).newInstance(view);
            holders.add(v);
            return v;
        } catch (IllegalAccessException | InstantiationException |
                InvocationTargetException | NoSuchMethodException e) {
            e.printStackTrace();
            return (ViewHolder) new ViewHolderNULL(view);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.setDatos(listaDatos.get(position));
    }

    @Override
    public int getItemCount() {
        return listaDatos.size();
    }

    private static class ViewHolderNULL extends
            RecyclerView.ViewHolder implements IReclycerElement<Object> {

        public ViewHolderNULL(@NonNull View itemView) {
            super(itemView);
        }

        @Override
        public void setDatos(Object data) {}
        @Override
        public Object getDatos() {
            return null;
        }
    }
}