package ucm.appmenus.entities;

import android.graphics.Bitmap;
import android.os.Parcel;
import android.os.Parcelable;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import ucm.appmenus.utils.BaseDatos;
import ucm.appmenus.utils.Constantes;
import ucm.appmenus.utils.OpenStreetMap;
import ucm.appmenus.utils.Precios;
import ucm.appmenus.utils.WebScraping;

public class Restaurante implements Parcelable {

    private final String idRestaurante;
    private final String nombre;
    private final String url;
    private final int telefono;
    private final String horarios;
    private int abierto;

    //MutableLiveData para poder actualizar en tiempo real sobre la interfaz
    private final MutableLiveData<Double> valoracion;
    private final MutableLiveData<String> direccion;
    private final MutableLiveData<Precios> precios;
    private final MutableLiveData<List<Bitmap>> listaImagenes;
    private final MutableLiveData<Set<String>> listaFiltros;
    private final MutableLiveData<Set<String>> listaFiltrosBD;
    private final MutableLiveData<List<Resenia>> listaResenias;

    //Utilizado para hacer webscrapping y poder cargar datos extra cuando se accede a la vista con detalles
    private final WebScraping ws;

    public Restaurante(String idRestaurante, String nombre, String url, String direccion, double lat, double lon,
                       int distanciaEnMetros, int telefono, String horarios, double valoracion, ArrayList<String> filtrosIni){
        this.idRestaurante = idRestaurante;
        this.nombre = nombre;
        this.url = url;
        this.telefono = telefono;
        this.horarios = horarios;
        updateAbierto();
        this.valoracion = new MutableLiveData<>(valoracion);
        this.direccion = new MutableLiveData<>(" [" + distanciaEnMetros + "m]");
        this.precios = new MutableLiveData<>(new Precios());
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
        ws = new WebScraping(url, listaFiltros, listaImagenes, precios);
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

    /**
     * Devuelve 1 si está abierto, 0 si no se sabe y -1 si cerrado
     * @return el esatdo de apertura del local
     */
    public int getAbierto(){ return this.abierto; }
    public int getVegano() {
        return (listaFiltros.getValue().stream().anyMatch(Constantes.filtrosDietaOSMIngles()::contains) ||
        listaFiltros.getValue().stream().anyMatch(Constantes.filtrosDietaWSIngles()::contains)) ? 1 : -1;
    }
    //TODO: ordenar bien porque esto solo ordena los que tienen precio arriba y los que no abajo
    public double getPrecioMediana() { return this.precios.getValue().esCorrecto() ? this.precios.getValue().mediana : 0; }

    public LiveData<Double> getLiveDataValoracion() { return valoracion; }
    public LiveData<String> getLiveDataDireccion() { return direccion; }
    public LiveData<Precios> getLiveDataPrecios() { return precios; }
    public LiveData<List<Bitmap>> getliveDataImagen() { return listaImagenes; }
    public LiveData<Set<String>> getLivedataFiltros() {return this.listaFiltros;}
    public LiveData<Set<String>> getLivedataFiltrosBD() {return this.listaFiltrosBD;}
    public LiveData<List<Resenia>> getLiveDataResenia() { return this.listaResenias;}

    public void updateResenias(){ BaseDatos.getInstance().getReseniasRestaurante(idRestaurante, listaResenias);}
    public void updateImagenes(){ ws.setImagenes(); }
    public void updateFiltros(){
        List<Set<String>> filtros = filtrosBasicos();
        filtros.add(Constantes.filtrosLocalIngles());
        filtros.add(Constantes.filtrosPostresIngles());
        ws.setFiltros(filtros);
        BaseDatos.getInstance().getFiltrosRestaurante(idRestaurante, listaFiltrosBD);
    }
    public void updateAbierto(){
        abierto = -1;
        if(horarios.contentEquals(""))
            abierto = 0;
        DateFormat df = new SimpleDateFormat("HH:mm");
        Pattern pattern = Pattern.compile("\\d{2}:\\d{2}");
        Matcher matcher = pattern.matcher(horarios);
        try {
            Date apertura = matcher.find() ? df.parse(matcher.group()) : null;
            Date cierre = matcher.find() ? df.parse(matcher.group()) : null;

            Calendar calendario = Calendar.getInstance();
            Date ahora = df.parse(calendario.get(Calendar.HOUR_OF_DAY) + ":" + calendario.get(Calendar.MINUTE));
            if(apertura != null && cierre != null && ahora != null &&
                    //Si la hora de "ahora" es mayor a la del compareTo, devuelve 1, sino -1 (igual si es la misma)
                    (ahora.compareTo(apertura) > 0 && ahora.compareTo(cierre) < 0)) {
                abierto = 1;
            }
        } catch (ParseException ignore) {}
    }

    private List<Set<String>> filtrosBasicos() {
        ArrayList<Set<String>> listOfLists = new ArrayList<>();
        listOfLists.add(Constantes.filtrosPaisIngles());
        listOfLists.add(Constantes.filtrosDietaWSIngles());
        listOfLists.add(Constantes.filtrosDietaOSMIngles());
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
        dest.writeInt(this.abierto);
        dest.writeDouble(this.valoracion.getValue());
        dest.writeStringList(new ArrayList<>(this.listaFiltros.getValue()));
    }

    protected Restaurante(Parcel in) {
        idRestaurante = in.readString();
        nombre = in.readString();
        url = in.readString();
        direccion = new MutableLiveData<>(in.readString());
        telefono = in.readInt();
        horarios = in.readString();
        abierto = in.readInt();
        valoracion = new MutableLiveData<>(in.readDouble());
        precios = new MutableLiveData<>(new Precios());
        listaImagenes = new MutableLiveData<>(new ArrayList<>());
        listaResenias = new MutableLiveData<>(new ArrayList<>());
        listaFiltros = new MutableLiveData<>(new HashSet<>(in.createStringArrayList()));
        listaFiltrosBD = new MutableLiveData<>(new HashSet<>());
        ws = new WebScraping(url, listaFiltros, listaImagenes, precios);
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
