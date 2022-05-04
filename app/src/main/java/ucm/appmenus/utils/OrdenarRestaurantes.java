package ucm.appmenus.utils;

import android.util.Log;

import java.util.Comparator;
import java.util.List;

import ucm.appmenus.entities.Restaurante;

public class OrdenarRestaurantes {

    public static void ordenarPorPrecio(List<Restaurante> restaurantes){
        Log.i("ordeno", "precio");
        restaurantes.sort(Comparator.comparing(Restaurante::getPrecioMediana, Comparator.reverseOrder()));
    }

    public static void ordenarPorApertura(List<Restaurante> restaurantes) {
        Log.i("ordeno", "apertura");
        restaurantes.sort(Comparator.comparing(Restaurante::getAbierto, Comparator.reverseOrder()));
    }

    public static void ordenarPorVegano(List<Restaurante> restaurantes){
        Log.i("ordeno", "vegano");
        restaurantes.sort(Comparator.comparing(Restaurante::getVegano, Comparator.reverseOrder()));
    }
}
