package ucm.appmenus.entities;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Parcel;
import android.os.Parcelable;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;

import ucm.appmenus.utils.Constantes;
import ucm.appmenus.utils.WebScrapping;

public class Restaurante implements Parcelable {

    private final String idRestaurante;
    private final String nombre;
    private final String url;
    private String direccion;
    private final int telefono;
    private final String horarios;
    private final double valoracion;

    private MutableLiveData<ArrayList<Bitmap>> listaImagenes;
    private MutableLiveData<HashSet<String>> listaFiltros;

    private WebScrapping ws;

    public Restaurante(String idRestaurante, String nombre, String url, String direccion,
                       int telefono, String horarios, double valoracion, ArrayList<String> filtrosIni){
        this.idRestaurante = idRestaurante;
        this.nombre = nombre;
        this.url = url;
        this.direccion = direccion;
        this.telefono = telefono;
        this.horarios = horarios;
        this.valoracion = valoracion;
        this.listaImagenes = new MutableLiveData<ArrayList<Bitmap>>();

        //Parsea los filtros, separandolos por ";"
        HashSet<String> filtrosAux = new HashSet<>();
        if(filtrosIni != null) {
            for (String s: filtrosIni) {
                filtrosAux.addAll(Arrays.asList(s.split(";")));
            }
        }
        this.listaFiltros = new MutableLiveData<>(filtrosAux);

        //Importante que vaya despues de iniciar los filtros
        ws = new WebScrapping(url, listaFiltros, listaImagenes);
        if(url != null){
            ws.setFiltros(Collections.singletonList(Constantes.filtrosPais));
            ws.setImagenPrincipal();
        }
        else{
            //TODO: HAcer accion por defecto como poner una imagen vacia o que no hay filtros
        }
    }

    public String getIdRestaurante(){return idRestaurante;}
    public String getNombre() {
        return nombre;
    }
    public String getStringURL() { return url; }
    public String getDireccion() { return direccion; }
    public int getTelefono() { return telefono; }
    public String getHorarios() { return horarios; }
    public double getValoracion() {
        return valoracion;
    }
    public ArrayList<Bitmap> getListaImagenes() { return listaImagenes.getValue(); }
    public LiveData<ArrayList<Bitmap>> getliveDataImagen() {
        return listaImagenes;
    }
    public HashSet<String> getListaFiltros() { return listaFiltros.getValue(); }
    public LiveData<HashSet<String>> getLivedataFiltros() {return this.listaFiltros;}

    public void setDireccion(String direccion) { this.direccion = direccion; }
    public void updateImagenes(){ ws.setImagenes(); }
    public void updateFiltros(){
        ArrayList<List<String>> listOfLists = new ArrayList<List<String>>();
        listOfLists.add(Constantes.filtrosPais);
        listOfLists.add(Constantes.filtrosLocal);
        listOfLists.add(Constantes.filtrosPostres);
        ws.setFiltros(listOfLists);
    }

    @Override
    public String toString(){
        String s = "";
        s+="Id: " + idRestaurante + "\n";
        s+="Nombre: " + nombre + "\n";
        s+="Valoracion: " + valoracion + "\n";
        s+="Imagen principal: " + listaImagenes + "\n";
        return s;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.idRestaurante);
        dest.writeString(this.nombre);
        dest.writeString(this.url);
        dest.writeString(this.direccion);
        dest.writeInt(this.telefono);
        dest.writeString(this.horarios);
        dest.writeDouble(this.valoracion);
        dest.writeStringList(new ArrayList<String>(this.listaFiltros.getValue()));
    }

    protected Restaurante(Parcel in) {
        idRestaurante = in.readString();
        nombre = in.readString();
        url = in.readString();
        direccion = in.readString();
        telefono = in.readInt();
        horarios = in.readString();
        valoracion = in.readDouble();
        listaImagenes.setValue(new ArrayList<>());
        listaFiltros.setValue(new HashSet<>(in.createStringArrayList()));
    }

    public static final Creator<Restaurante> CREATOR = new Creator<Restaurante>() {
        @Override
        public Restaurante createFromParcel(Parcel in) {
            return new Restaurante(in);
        }

        @Override
        public Restaurante[] newArray(int size) {
            return new Restaurante[size];
        }
    };
}
