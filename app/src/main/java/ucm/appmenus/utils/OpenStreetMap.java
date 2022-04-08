package ucm.appmenus.utils;

import android.util.Log;

import androidx.lifecycle.MutableLiveData;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import java.net.HttpURLConnection;
import java.net.URL;

import java.util.ArrayList;
import java.util.Set;
import java.util.stream.Collectors;

import ucm.appmenus.entities.Restaurante;
import ucm.appmenus.entities.Usuario;
import ucm.appmenus.ficheros.JSONOpenStreetReader;

public class OpenStreetMap {

    //Maximo 10mb de resultado de query y en formato json
    private static final String URL_FIND_PLACES = "https://overpass-api.de/api/interpreter?data=[maxsize:10737418][out:json]";
    private static final String URL_FIND_STREET_BY_ID = "https://nominatim.openstreetmap.org/lookup?format=json&";
    private static final String URL_FIND_STREET_BY_COORD = "https://nominatim.openstreetmap.org/reverse?format=jsonv2&";

    /**
     * Funcion que realiza una busqueda en OpenStreetMap para encontrar restaurantes
     * @param actualizable: el parametro donde se guardara el resultado de la busqueda.
     *                    Es un MutableLiveData para poder actualizar la vista en tiempo real.
     * @param attr: parametros necesarios para realizar la busqueda.
     *            Dicha busqueda se realiza de forma automatica, solo hay que suministrar los parametros.
     */
    public void setPlaces(final MutableLiveData<ArrayList<Restaurante>> actualizable,
                          OpenStreetAttributes attr) {
        final String query = construirQueryRestaurante(attr);
        //Necesario el thread para no lanzar excepciones y no sobrecargar el hilo principal
        Thread th = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    //Obtiene los resultados y los guarda en la cola para que se publiquen en cuanto sea posible
                    JSONOpenStreetReader reader = new JSONOpenStreetReader();
                    actualizable.postValue(reader.parsearResultado(getURLData(query), attr.latitud, attr.longitud));
                } catch (Exception ignored) {}
            }
        });
        th.start();
    }

    /**
     * Funcion que realiza una busqueda en OpenStreetMap para encontrar la direccion del restaurante
     * @param actualizable: el parametro donde se guardara el resultado de la busqueda.
     *                    Es un MutableLiveData para poder actualizar la vista en tiempo real.
     * @param idRestaurante: el identificador unico del restaurante en OpenStreetMap
     */
    public void setDireccion(MutableLiveData<String> actualizable, long idRestaurante){
        updateDireccionRestaurante(actualizable, construirQueryDireccionPorID(idRestaurante));
    }

    /**
     * Funcion que realiza una busqueda en OpenStreetMap para encontrar una direccion en base a una
     * latitud y longitud
     * @param actualizable: el parametro donde se guardara el resultado de la busqueda.
     * @param lat: latitud del nodo a buscar
     * @param lon: longitud del nodo a buscar
     */
    public void setDireccion(MutableLiveData<String> actualizable, double lat, double lon){
        updateDireccionRestaurante(actualizable, construirQueryDireccionPorCoord(lat, lon));
    }

    public void setPlaceById(Set<Restaurante> actualizable, String id){
        String query = URL_FIND_PLACES + ";(node(" + id + "););out;";
        Log.i("query", query);
        Thread th = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    //Obtiene los resultados y los guarda en la cola para que se publiquen en cuanto sea posible
                    Usuario u = Usuario.getUsuario();
                    JSONOpenStreetReader reader = new JSONOpenStreetReader();
                    actualizable.addAll(reader.parsearResultado(getURLData(query),
                            u.getLocalizacion().latitude, u.getLocalizacion().longitude));
                } catch (Exception ignored) {}
            }
        });
        th.start();
    }

    private void updateDireccionRestaurante(final MutableLiveData<String> d, final String query){
        Thread th = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    //Obtiene los resultados y los guarda en la cola para que se publiquen en cuanto sea posible
                    JSONOpenStreetReader reader = new JSONOpenStreetReader();
                    d.postValue(reader.parsearDireccion(getURLData(query)) + d.getValue());
                } catch (Exception ignored) {}
            }
        });
        th.start();
    }

    /**
     * Funcion que realiza una busqueda en OpenStreetMap. Debe llamarse desde un thread nuevo o lanzará una excepcion.
     * @param query: la query a realizar
     * @return: el resultado de la query
     */
    private String getURLData(String query){
        try{
            StringBuilder content = new StringBuilder();

            URL url = new URL(query);

            HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestMethod("GET");
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
            urlConnection.connect();

            //Log.d("Fin", "Fin del hilo de OpenStreetMap");
            return bufferedReader.lines().collect(Collectors.joining());
        } catch (IOException e) {
            return "";
        }
    }

    /**
     * Construye la query para buscar los restaurantes en base a los filtros
     * @param attr filtros que se utilizaran para realizar la query
     * @return un string con la query final, lista para ser utilizada por OpenStreetMap
     */
    private String construirQueryRestaurante(OpenStreetAttributes attr){
        String res = URL_FIND_PLACES;
        if(attr.timeout < 10 && attr.timeout > 5){
            res+="[timeout:" + attr.timeout + "];";
        }
        else{
            res+="[timeout:" + 7 + "];";
        }
        //Tipos restaurantes
        //TODO: la query falla cuando hay varios tipos de local porque hay que poner el around para cada uno
        if(attr.tiposLocal.isEmpty())       //Si no hay, se selecciona restaurante por defecto
            attr.tiposLocal.add(Constantes.filtrosLocal.get(5));
        for (String local: attr.tiposLocal) {
            res += "(node[%22amenity%22=%22" + local + "%22]";
            //Tipos cocina
            if (attr.tiposCocina.size() == 1) {
                res += "[%22cuisine%22=%22" + attr.tiposCocina.get(0) + "%22]";
            } else if (attr.tiposCocina.size() > 1) {
                res += "[%22cuisine%22~%22";
                for (int i = 0; i < attr.tiposCocina.size() - 1; i++) {
                    res += attr.tiposCocina.get(i) + "|";
                }
                res += attr.tiposCocina.get(attr.tiposCocina.size() - 1) + "%22]";
            }
        }
        //Distancia
        if(attr.area < 500){
            res+="(around:" + 1500 +"," + attr.latitud + "," + attr.longitud + ");";
        }
        else{
            res+="(around:" + attr.area +"," + attr.latitud + "," + attr.longitud + ");";
        }

        res+=");out+30;";
        Log.d("QUERY OSM", res);
        return res;
    }

    private String construirQueryDireccionPorID(long id){
        return URL_FIND_STREET_BY_ID + "osm_ids=N" + id;
    }

    private String construirQueryDireccionPorCoord(double lat, double lon){
        return URL_FIND_STREET_BY_COORD + "lat=" + lat + "&lon=" + lon;
    }

    public static class OpenStreetAttributes {
        public final int timeout, area;
        public final ArrayList<String> tiposLocal, tiposCocina;
        public final double latitud, longitud;

        /**
         * Clase interna que contiene los atributos necesarios para la query.
         * @param timeout: tiempo maximo que tardara la query.
         * @param tiposLocal: lista con los tipos de local (restaurante, bar...)
         * @param tiposCocina: lista con los tipos de comida (italiana, española, hamburguesa, pasta...)
         * @param area: radio maximo en el cual buscar.
         * @param latitud: latitud desde la que se realiza la busqueda (con un radio establecido por {@link #area})
         * @param longitud: longitud desde la que se realiza la busqueda (con un radio establecido por {@link #area})
         */
        public OpenStreetAttributes(int timeout, ArrayList<String> tiposLocal,  ArrayList<String> tiposCocina,
                                    int area, double latitud, double longitud){
            this.timeout = timeout;
            this.area = area;
            this.tiposLocal = tiposLocal;
            this.tiposCocina = tiposCocina;
            this.latitud = latitud;
            this.longitud = longitud;
        }

        public OpenStreetAttributes(ArrayList<String> tiposLocal,  ArrayList<String> tiposCocina,
                                    int area, double latitud, double longitud){
            this(7, tiposLocal, tiposCocina, area, latitud, longitud);
        }
    }
}