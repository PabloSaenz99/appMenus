package ucm.appmenus.entities;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.Arrays;
import java.util.Objects;

public class Resenia implements Parcelable {

    private final String idResenia;
    private final String idRestaurante;
    private final String idUsuario;
    //TODO: usuario no vale para nada porque el nombre puede cambiar?
    private final String usuarioNombre;
    private final String titulo;
    private final String texto;
    private final double valoracion;

    public Resenia(String idRestaurante, String idUsuario, String usuarioNombre, String titulo, String texto, double valoracion){
        this.idRestaurante = idRestaurante;
        this.idUsuario = idUsuario;
        this.usuarioNombre = usuarioNombre;
        this.titulo = titulo;
        this.texto = texto;
        this.valoracion = valoracion;

        this.idResenia = String.valueOf(this.hashCode());
    }

    public String getIdResenia() { return idResenia; }
    public String getIdRestaurante() { return idRestaurante; }
    public String getIdUsuario() { return idUsuario; }
    public String getUsuarioNombre() { return usuarioNombre; }
    public String getTitulo() { return titulo; }
    public String getTexto() { return texto; }
    public double getValoracion() { return valoracion; }

    /**
     * Redefine el hash del objeto para que si los datos son iguales siempre sea el mismo
     * @return hash del objeto
     */
    @Override
    public int hashCode() {
        Object[] x = {idResenia, idRestaurante, idUsuario, titulo, texto, String.valueOf(valoracion)};
        return Arrays.hashCode(x);
    }

    //--------Las funciones siguientes se usan para poder pasar la clase entre Actividades----------
    protected Resenia(Parcel in) {
        idResenia = in.readString();
        idRestaurante = in.readString();
        idUsuario = in.readString();
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
        parcel.writeString(idResenia);
        parcel.writeString(idRestaurante);
        parcel.writeString(idUsuario);
        parcel.writeString(usuarioNombre);
        parcel.writeString(titulo);
        parcel.writeString(texto);
        parcel.writeDouble(valoracion);
    }
}