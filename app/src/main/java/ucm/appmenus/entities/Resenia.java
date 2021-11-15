package ucm.appmenus.entities;

import ucm.appmenus.entities.Restaurante;

public class Resenia {

    private final Restaurante restaurante;
    private final String titulo;
    private final String texto;
    private double valoracion;

    public Resenia(Restaurante restaurante, String titulo, String texto, double valoracion){
        this.restaurante = restaurante;
        this.titulo = titulo;
        this.texto = texto;
        this.valoracion = valoracion;
    }

    public Restaurante getRestaurante() { return restaurante; }
    public String getTitulo() { return titulo; }
    public String getTexto() { return texto; }
    public double getValoracion() { return valoracion; }

    public void setValoracion(double valoracion) { this.valoracion = valoracion; }
}