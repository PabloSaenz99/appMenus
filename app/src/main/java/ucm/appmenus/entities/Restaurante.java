package ucm.appmenus.entities;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.Base64;
import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;

import ucm.appmenus.utils.WebScrapping;

public class Restaurante implements Parcelable {

    private final String idRestaurante;
    private final String nombre;
    private final String url;
    private String direccion;
    private final int telefono;
    private final String horarios;
    private final double valoracion;
    private final MutableLiveData<Bitmap> imagenPrincDir;

    private MutableLiveData<HashSet<String>> filtros;
    //private final ArrayList<Foto> imagenesDir;

    public Restaurante(String idRestaurante, String nombre, String url, String direccion,
                       int telefono, String horarios, double valoracion, String imagenPrincDir,
                       ArrayList<String> filtrosIni, ArrayList<Foto> imagenesDir){
        this.idRestaurante = idRestaurante;
        this.nombre = nombre;
        this.url = url;
        this.direccion = direccion;
        this.telefono = telefono;
        this.horarios = horarios;
        this.valoracion = valoracion;
        this.imagenPrincDir = new MutableLiveData<>(BitmapFactory.decodeFile(imagenPrincDir));

        //Parsea los filtros, separandolos por ";"
        HashSet<String> filtrosAux = new HashSet<>();
        if(filtrosIni != null) {
            for (String s: filtrosIni) {
                filtrosAux.addAll(Arrays.asList(s.split(";")));
            }
        }
        this.filtros = new MutableLiveData<>(filtrosAux);

        //Importante que vaya despues de iniciar los filtros
        if(url != null){
            new WebScrapping().setFiltros(url, this.filtros, this.imagenPrincDir);
        }

        //if(imagenesDir == null) this.imagenesDir = new ArrayList<Foto>();
        //else this.imagenesDir = imagenesDir;
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
    public Bitmap getImagenPrincDir() { return imagenPrincDir.getValue(); }
    public LiveData<Bitmap> getliveDataImagen() {
        return imagenPrincDir;
    }
    public HashSet<String> getFiltros() { return filtros.getValue(); }
    public LiveData<HashSet<String>> getLivedataFiltros() {return this.filtros;}
    //public ArrayList<Foto> getFotos() {return imagenesDir;}

    public void setDireccion(String direccion) { this.direccion = direccion; }

    @Override
    public String toString(){
        String s = "";
        s+="Id: " + idRestaurante + "\n";
        s+="Nombre: " + nombre + "\n";
        s+="Valoracion: " + valoracion + "\n";
        s+="Imagen principal: " + imagenPrincDir + "\n";
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
        dest.writeValue(this.imagenPrincDir.getValue());
        dest.writeStringList(new ArrayList<String>(this.filtros.getValue()));
    }

    protected Restaurante(Parcel in) {
        idRestaurante = in.readString();
        nombre = in.readString();
        url = in.readString();
        direccion = in.readString();
        telefono = in.readInt();
        horarios = in.readString();
        valoracion = in.readDouble();
        imagenPrincDir = new MutableLiveData<Bitmap>((Bitmap) in.readParcelable(Bitmap.class.getClassLoader()));
        filtros.setValue(new HashSet<String>(in.createStringArrayList()));
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
