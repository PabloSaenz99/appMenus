package ucm.appmenus.entities;

import android.graphics.Bitmap;
import android.os.Parcel;
import android.os.Parcelable;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import ucm.appmenus.utils.BaseDatos;
import ucm.appmenus.utils.Constantes;
import ucm.appmenus.utils.OpenStreetMap;
import ucm.appmenus.utils.WebScrapping;

public class Restaurante implements Parcelable {

    private final String idRestaurante;
    private final String nombre;
    private final String url;
    private final int telefono;
    private final String horarios;

    //MutableLiveData para poder actualizar en tiempo real sobre la interfaz
    private final MutableLiveData<Double> valoracion;
    private final MutableLiveData<String> direccion;
    private final MutableLiveData<List<Bitmap>> listaImagenes;
    private final MutableLiveData<Set<String>> listaFiltros;
    private final MutableLiveData<Set<String>> listaFiltrosBD;
    private final MutableLiveData<List<Resenia>> listaResenias;

    //Utilizado para hacer webscrapping y poder cargar datos extra cuando se accede a la vista con detalles
    private final WebScrapping ws;

    public Restaurante(String idRestaurante, String nombre, String url, String direccion, double lat, double lon,
                       int distanciaEnMetros, int telefono, String horarios, double valoracion, ArrayList<String> filtrosIni){
        this.idRestaurante = idRestaurante;
        this.nombre = nombre;
        this.url = url;
        this.telefono = telefono;
        this.horarios = horarios;
        this.valoracion = new MutableLiveData<>(valoracion);
        this.direccion = new MutableLiveData<>(" [" + distanciaEnMetros + "m]");
        this.listaImagenes = new MutableLiveData<>();
        this.listaResenias = new MutableLiveData<>(new ArrayList<>());
        this.listaFiltrosBD = new MutableLiveData<>(new HashSet<>());

        //Parsea los filtros, separandolos por ";"
        HashSet<String> filtrosAux = new HashSet<>();
        if(filtrosIni != null)
            for (String s: filtrosIni)
                filtrosAux.addAll(Arrays.asList(s.split(";")));
        this.listaFiltros = new MutableLiveData<>(filtrosAux);

        //Importante que vaya despues de iniciar los filtros
        ws = new WebScrapping(url, listaFiltros, listaImagenes);
        if(url != null){
            ws.setFiltros(filtrosBasicos());
            ws.setImagenPrincipal();
        }
        //Si no hay direccion (", ) entonces la busca mediante las coordenadas
        if (direccion.equals(", ")) {
            new OpenStreetMap().setDireccion(this.direccion, lat, lon);
        } else {
            this.direccion.postValue(direccion + this.direccion.getValue());
        }

        BaseDatos.getInstance().getValoracionRestaurante(idRestaurante, this.valoracion);
    }

    public String getIdRestaurante(){return idRestaurante;}
    public String getNombre() { return nombre; }
    public String getStringURL() { return url; }
    public int getTelefono() { return telefono; }
    public String getHorarios() { return horarios; }

    public LiveData<Double> getLiveDataValoracion() { return valoracion; }
    public LiveData<String> getLiveDataDireccion() { return direccion; }
    public LiveData<List<Bitmap>> getliveDataImagen() { return listaImagenes; }
    public LiveData<Set<String>> getLivedataFiltros() {return this.listaFiltros;}
    public LiveData<Set<String>> getLivedataFiltrosBD() {return this.listaFiltrosBD;}
    public LiveData<List<Resenia>> getLiveDataResenia() { return this.listaResenias;}

    public void updateResenias(){ BaseDatos.getInstance().getReseniasRestaurante(idRestaurante, listaResenias);}
    public void updateImagenes(){ ws.setImagenes(); }
    public void updateFiltros(){
        List<List<String>> filtros = filtrosBasicos();
        filtros.add(Constantes.filtrosLocal);
        filtros.add(Constantes.filtrosPostres);
        ws.setFiltros(filtros);
        BaseDatos.getInstance().getFiltrosRestaurante(idRestaurante, listaFiltrosBD);
    }

    private List<List<String>> filtrosBasicos() {
        ArrayList<List<String>> listOfLists = new ArrayList<>();
        listOfLists.add(Constantes.filtrosPais);
        listOfLists.add(Constantes.filtrosDietaWebScrapping);
        return listOfLists;
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

    //--------Las funciones siguientes se usan para poder pasar la clase entre Actividades----------
    @Override
    public int describeContents() { return 0; }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.idRestaurante);
        dest.writeString(this.nombre);
        dest.writeString(this.url);
        dest.writeString(this.direccion.getValue());
        dest.writeInt(this.telefono);
        dest.writeString(this.horarios);
        dest.writeDouble(this.valoracion.getValue());
        dest.writeStringList(new ArrayList<String>(this.listaFiltros.getValue()));
    }

    protected Restaurante(Parcel in) {
        idRestaurante = in.readString();
        nombre = in.readString();
        url = in.readString();
        direccion = new MutableLiveData<>(in.readString());
        telefono = in.readInt();
        horarios = in.readString();
        valoracion = new MutableLiveData<>(in.readDouble());
        listaImagenes = new MutableLiveData<>(new ArrayList<>());
        listaResenias = new MutableLiveData<>(new ArrayList<>());
        listaFiltros = new MutableLiveData<>(new HashSet<>(in.createStringArrayList()));
        listaFiltrosBD = new MutableLiveData<>(new HashSet<>());
        ws = new WebScrapping(url, listaFiltros, listaImagenes);
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
