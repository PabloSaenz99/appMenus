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

import ucm.appmenus.MainActivity;
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
                    long startTime = System.nanoTime();

                    //Obtiene los resultados y los guarda en la cola para que se publiquen en cuanto sea posible
                    JSONOpenStreetReader reader = new JSONOpenStreetReader();
                    actualizable.postValue(reader.parsearResultado(getURLData(query), attr.latitud, attr.longitud));

                    MainActivity.medirTiempo("OSM set places", startTime, System.nanoTime());
                } catch (Exception ignored) {}
            }
        });
        th.setName("OSM Set Places");
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
        th.setName("PLACES by ID");
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
        th.setName("OSM Dir Restaurante");
        th.start();
    }

    /**
     * Funcion que realiza una busqueda en OpenStreetMap. Debe llamarse desde un thread nuevo o lanzará una excepcion.
     * @param query: la query a realizar
     * @return: el resultado de la query
     */
    private String getURLData(String query){
        try{
            URL url = new URL(query);

            HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestMethod("GET");
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
            urlConnection.connect();
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
        String res = URL_FIND_PLACES + "[timeout:" + attr.timeout + "];";
        /*
        Parsea cada una de las listas y añade todos los parámetros de búsqueda proporcionados,
        como el tipo de local (restauran, bar...), los tipos de dieta (vegana, sin gluten etc...)
        o tipos de cocina (italiana, india, hamburguesas, ensaladas...)
         */
        res += "(node";
        res += parseLista(attr.tiposLocal, "amenity");
        res += parseLista(attr.tiposCocina, "cuisine");
        if(attr.tiposDieta.size() == 1){
            res+="[%22diet:" + attr.tiposDieta.get(0) + "%22=%22yes%22]";
        } else if(attr.tiposDieta.size() > 1){
            for (int i = 0; i < attr.tiposDieta.size() - 1; i++) {
                res += "[%22diet:" + attr.tiposDieta.get(i) + "%22=%22yes%22|";
            }
            res += attr.tiposDieta.get(attr.tiposDieta.size() - 1) + "%22=%22yes%22]";
        }
        //Distancia
        res+="(around:" + attr.area +"," + attr.latitud + "," + attr.longitud + ");";
        res+=");out+30;";

        Log.d("QUERY OSM", res);
        return res;
    }

    /**
     * Obtiene un string parseando la lista
     * @param lista lista con los filtros a parsear
     * @param filtro tipo de filtro (amenity, cuisine...)
     * @return resultado del parseo usable por OSM
     */
    private String parseLista(ArrayList<String> lista, String filtro){
        String res = "";
        if (lista.size() == 1) {
            res += "[%22" + filtro + "%22~%22" + lista.get(0) + "%22]";
        } else if(lista.size() > 1) {
            res += "[%22" + filtro + "%22~%22";
            for (int i = 0; i < lista.size() - 1; i++) {
                res += lista.get(i) + "|";
            }
            res += lista.get(lista.size() - 1) + "%22]";
        }
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
        public final ArrayList<String> tiposDieta, tiposLocal, tiposCocina;
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
        public OpenStreetAttributes(int timeout, ArrayList<String> tiposDieta, ArrayList<String> tiposLocal,  ArrayList<String> tiposCocina,
                                    int area, double latitud, double longitud){
            this.timeout = (timeout < 10 && timeout > 5 ? timeout : 7);
            this.area = (area < 500 ? 1500 : area);
            this.tiposDieta = tiposDieta;
            this.tiposLocal = tiposLocal;
            this.tiposCocina = tiposCocina;
            this.latitud = latitud;
            this.longitud = longitud;

            if(tiposLocal.isEmpty())
                tiposLocal.add("restaurant");
        }

        public OpenStreetAttributes(ArrayList<String> tiposDieta, ArrayList<String> tiposLocal,  ArrayList<String> tiposCocina,
                                    int area, double latitud, double longitud){
            this(7, tiposDieta, tiposLocal, tiposCocina, area, latitud, longitud);
        }
    }
}