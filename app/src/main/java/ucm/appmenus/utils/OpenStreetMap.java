package ucm.appmenus.utils;

import android.util.Log;

import androidx.lifecycle.MutableLiveData;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import java.net.HttpURLConnection;
import java.net.URL;

import java.util.ArrayList;
import java.util.stream.Collectors;

import ucm.appmenus.entities.Restaurante;
import ucm.appmenus.ficheros.JSONOpenStreetReader;

public class OpenStreetMap {

    //Maximo 10mb de resultado de query y en formato json
    private static final String URL_FIND_PLACES = "https://overpass-api.de/api/interpreter?data=[maxsize:10737418][out:json]";
    private static final String URL_FIND_STREET_BY_ID = "https://nominatim.openstreetmap.org/lookup?format=json&";
    private static final String URL_FIND_STREET_BY_COORD = "https://nominatim.openstreetmap.org/reverse?format=json&";

    public void setPlaces(final MutableLiveData<ArrayList<Restaurante>> actualizable,
                          ArrayList<String> tiposRestaurantes, ArrayList<String> tiposCocina,
                          int area, final Localizacion loc) {
        final String query = construirQueryRestaurante(25, tiposRestaurantes,
                tiposCocina, area, loc.longitude, loc.latitude);
        //Necesario el thread para no lanzar excepciones y no sobrecargar el hilo principal
        Thread th = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    //Obtiene los resultados y los guarda en la cola para que se publiquen en cuanto sea posible
                    JSONOpenStreetReader reader = new JSONOpenStreetReader();
                    actualizable.postValue(reader.parsearResultado(getURLData(
                            construirQueryRestaurante(10,
                                    new ArrayList<String>(){{add("restaurant");}},
                                    new ArrayList<String>(),
                                    1500, loc.longitude, loc.latitude))));
                } catch (Exception ignored) {}
            }
        });
        th.start();
    }

    public void setDireccion(Restaurante r, long id){
        updateDireccionRestaurante(r, construirQueryDireccionPorID(id));
    }

    public void setDireccion(Restaurante r, double lat, double lon){
        updateDireccionRestaurante(r, construirQueryDireccionPorCoord(lat, lon));
    }

    private void updateDireccionRestaurante(final Restaurante r, final String query){
        Thread th = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    //Obtiene los resultados y los guarda en la cola para que se publiquen en cuanto sea posible
                    r.setDireccion(getURLData(query));
                } catch (Exception ignored) {}
            }
        });
        th.start();
    }

    private String getURLData(String query){
        try{
            StringBuilder content = new StringBuilder();

            URL url = new URL(query);

            HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestMethod("GET");
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
            urlConnection.connect();

            Log.d("Fin de busqueda", "Fin del hilo de OpenStreetMap");
            return bufferedReader.lines().collect(Collectors.joining());
        } catch (IOException e) {
            return "";
        }
    }

    private String construirQueryRestaurante(int timeout, ArrayList<String> tiposRestaurantes,
                                             ArrayList<String> tiposCocina,
                                             int area, double longitud, double latitud){
        String res = URL_FIND_PLACES;
        if(timeout < 10 && timeout > 1){
            res+="[timeout:" + timeout + "];";
        }
        else{
            res+="[timeout:" + 10 + "];";
        }
        //Tipos restaurantes
        if(tiposRestaurantes.size() == 1){
            res+="(node[%22amenity%22=%22" + tiposRestaurantes.get(0) + "%22]";
        }
        else{
            //Poner lo de tiposCocina.size()>1???
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
        if(area < 500){
            area = 1500;
        }
        else{
            res+="(around:" + area +"," + latitud + "," + longitud + ");";
        }

        res+=");out;";
        System.out.println(res);
        return res;
    }

    public String construirQueryDireccionPorID(long id){
        return URL_FIND_STREET_BY_ID + "osm_ids=N" + id;
    }

    public String construirQueryDireccionPorCoord(double lat, double lon){
        return URL_FIND_STREET_BY_COORD + "lat=" + lat + "&lon=" + lon;
    }
}