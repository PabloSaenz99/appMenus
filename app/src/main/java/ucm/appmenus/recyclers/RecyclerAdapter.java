package ucm.appmenus.recyclers;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;

import ucm.appmenus.R;
import ucm.appmenus.utils.Pair;

/**
 * Clase usada para mostrar un bucle de datos, usada para generar vistas en tiempo de ejecucion.
 * @param <ELEMENT> Los datos que se van a almacenar y mostar en bucle
 *      @see IReclycerElement
 * @param <ViewHolder> La clase que almacena los datos de ELEMENT, debe extender
 *      a RecyclerView.ViewHolder e implementar IReclycerElement
 *      @see ViewHolderNULL para un ejemplo muy basico
 * IMPORTANTE: Esta clase no debe ser modificada
 * */
public class RecyclerAdapter<ViewHolder extends
        RecyclerView.ViewHolder & IReclycerElement<ELEMENT>, ELEMENT>
        extends RecyclerView.Adapter<ViewHolder> {

    private int viewID;
    private ArrayList<ELEMENT> listaDatos;
    private Class<ViewHolder> clase;

    /**
     * @param dataSet : lista con los datos que se quieren mostar en bucle
     * @param viewID : el id (R.layout...) del elemento a representar en bucle
     * @param clase : la clase que implementa  RecyclerView.ViewHolder y extiende IReclycerElement
     * */
    public RecyclerAdapter(ArrayList<ELEMENT> dataSet, int viewID, Class<ViewHolder> clase) {
        this.listaDatos = dataSet;
        this.viewID = viewID;
        this.clase = clase;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(viewID, parent, false);
        try {
            return clase.getConstructor(View.class).newInstance(view);
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

/* OTRA IMPLEMENTACION
package ucm.appmenus.recyclers;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;

public class RecyclerAdapter<ELEMENT, ViewHolder extends AbstractViewHolder<ELEMENT>> extends
        RecyclerView.Adapter<ViewHolder> {

    Class<ViewHolder> clase;
    private int viewID;
    private ArrayList<ELEMENT> listaDatos;

    public RecyclerAdapter(ArrayList<ELEMENT> dataSet, int viewID, Class<ViewHolder> clase) {
        this.listaDatos = dataSet;
        this.viewID = viewID;
        this.clase = clase;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(viewID, parent, false);
        //Testear (no va):
        try {
            return clase.getConstructor(View.class).newInstance(view);
        } catch (IllegalAccessException | InstantiationException |
                InvocationTargetException | NoSuchMethodException e) {
            e.printStackTrace();
            return (ViewHolder) new AbstractViewHolder.NullViewHolder(parent);
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
}

public abstract class AbstractViewHolder<ELEMENT> extends RecyclerView.ViewHolder{

    private ELEMENT datos;

    public AbstractViewHolder(@NonNull View itemView) {
        super(itemView);
    }

    public abstract void setDatos(ELEMENT data);
    public final ELEMENT getDatos(){
        return datos;
    }

    public static class NullViewHolder extends  AbstractViewHolder<Object> {
        public NullViewHolder(@NonNull View itemView) {
            super(itemView);
        }

        @Override
        public void setDatos(Object data) {}
    }
}
*/