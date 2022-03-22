package ucm.appmenus.entities;

import android.os.Parcel;
import android.os.Parcelable;

public class Resenia implements Parcelable {

    private final String restauranteID;
    private final String usuarioEmail;
    private final String usuarioNombre;
    private final String titulo;
    private final String texto;
    private final double valoracion;

    public Resenia(String restauranteID, String usuarioEmail, String usuarioNombre, String titulo, String texto, double valoracion){
        this.restauranteID = restauranteID;
        this.usuarioEmail = usuarioEmail;
        this.usuarioNombre = usuarioNombre;
        this.titulo = titulo;
        this.texto = texto;
        this.valoracion = valoracion;
    }

    public String getRestauranteID() { return restauranteID; }
    public String getUsuarioEmail() { return usuarioEmail; }
    public String getUsuarioNombre() { return usuarioNombre; }
    public String getTitulo() { return titulo; }
    public String getTexto() { return texto; }
    public double getValoracion() { return valoracion; }

    //--------Las funciones siguientes se usan para poder pasar la clase entre Actividades----------
    protected Resenia(Parcel in) {
        restauranteID = in.readString();
        usuarioEmail = in.readString();
        usuarioNombre = in.readString();
        titulo = in.readString();
        texto = in.readString();
        valoracion = in.readDouble();
    }

    public static final Creator<Resenia> CREATOR = new Creator<Resenia>() {
        @Override
        public Resenia createFromParcel(Parcel in) {
            return new Resenia(in);
        }

        @Override
        public Resenia[] newArray(int size) {
            return new Resenia[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(restauranteID);
        parcel.writeString(usuarioEmail);
        parcel.writeString(usuarioNombre);
        parcel.writeString(titulo);
        parcel.writeString(texto);
        parcel.writeDouble(valoracion);
    }
}