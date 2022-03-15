package ucm.appmenus.entities;

import android.os.Parcel;
import android.os.Parcelable;

public class Resenia implements Parcelable {

    private final String restauranteID;
    private final String titulo;
    private final String texto;
    private final double valoracion;

    public Resenia(String restauranteID, String titulo, String texto, double valoracion){
        this.restauranteID = restauranteID;
        this.titulo = titulo;
        this.texto = texto;
        this.valoracion = valoracion;
    }

    public String getRestauranteID() { return restauranteID; }
    public String getTitulo() { return titulo; }
    public String getTexto() { return texto; }
    public double getValoracion() { return valoracion; }

    protected Resenia(Parcel in) {
        restauranteID = in.readString();
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
        parcel.writeString(titulo);
        parcel.writeString(texto);
        parcel.writeDouble(valoracion);
    }
}