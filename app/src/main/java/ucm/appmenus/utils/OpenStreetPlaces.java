package ucm.appmenus.utils;

import androidx.recyclerview.widget.RecyclerView;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.util.ArrayList;
import java.util.stream.Collectors;

import ucm.appmenus.entities.Restaurante;
import ucm.appmenus.ficheros.JSONOpenStreetReader;
import ucm.appmenus.recyclers.RestauranteRecyclerAdapter;
import ucm.appmenus.ui.inicio.InicioFragment;

public class OpenStreetPlaces{

    private static final String URL_STRING = "https://overpass-api.de/api/interpreter?data=[out:json]";
    private InicioFragment inicioFragment;

    public OpenStreetPlaces(InicioFragment ini){
        this.inicioFragment = ini;
    }

    public void getPlaces(final ArrayList<Restaurante> restaurantes, ArrayList<String> tiposRestaurantes,
                          ArrayList<String> tiposCocina, int area, Localizacion loc) {
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                try{
                    StringBuilder content = new StringBuilder();

                    URL url = new URL("https://overpass-api.de/api/interpreter?data=[out:json][timeout:25];(node[%22amenity%22=%22restaurant%22][%22cuisine%22=%22italian%22](around:500,%2040.41676,%20-3.70329););out;");
                    //URL url = new URL(construirQuery(0, tiposRestaurantes, tiposCocina, area, loc.longitude, loc.latitude));

                    HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
                    urlConnection.setRequestMethod("GET");

                    BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
                    urlConnection.connect();
                    JSONOpenStreetReader reader = new JSONOpenStreetReader();
                    restaurantes.addAll(reader.parsearResultado(bufferedReader.lines().collect(Collectors.joining())));

                    System.out.println("He acabado!!!!!----------------------------------------");
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
        thread.start();
    }

    private static String construirQuery(int timeout, ArrayList<String> tiposRestaurantes, ArrayList<String> tiposCocina,
                                         int area, double longitud, double latitud){
        String res = URL_STRING;
        if(timeout > 0){
            res+="[timeout:" + timeout + "]";
        }
        else{
            res+="[timeout:" + 25 + "]";
        }
        //Tipos restaurantes
        if(tiposRestaurantes.size() == 1){
            res+="(node[%22amenity%22=%22" + tiposRestaurantes.get(0) + "%22]";
        }
        else{
            //POner lo de tiposCocina.size()>1???
        }
        //Tipos cocina
        if(tiposCocina.size() == 1){
            res+="[%22cuisine%22=%22" + tiposCocina.get(0) + "%22]";
        }
        else if(tiposCocina.size() > 1){
            res+="[%22cuisine%22~%22";
            for(int i = 0; i < tiposCocina.size() - 1; i++) {
                res += tiposCocina.get(i) + "|";
            }
            res += tiposCocina.get(tiposCocina.size() - 1) + "%22]";
        }
        //Distancia
        if(area == 0){
            area = 1500;
        }
        else{
            res+="(around:" + area +"," + longitud + "," + latitud + ");";
        }

        res+=");out;";
        return res;
    }
}
