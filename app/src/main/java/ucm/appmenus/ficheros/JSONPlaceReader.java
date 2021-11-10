package ucm.appmenus.ficheros;

import android.content.Context;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

import ucm.appmenus.Foto;
import ucm.appmenus.Restaurante;

public class JSONPlaceReader {

    /**
     * Al buscar places usar las etiquetas: bar, cafe, restaurant (meal_delivery, meal_takeaway)
     *
     * https://developers.google.com/maps/documentation/places/web-service/supported_types
     */

    private JSONObject init;

    /**
     * Lee los JSON devueltos por:
     *
     * https://developers.google.com/maps/documentation/places/web-service/search-find-place
     * */
    public JSONPlaceReader(Context context, String src) throws JSONException, IOException {
        init = new JSONObject(ManejadorFicheros.fileToString(context, src));
    }
    /**
     * Para buscar hay que:
     * 1- Buscar Current Place para que devuelva una lista de las cosas cercanas
     * 2- Una vez tienes los id (place_id):
     *      - almacenar en fichero de texto o algo el id
     *      - almacenar datos utiles (¿en BD?)
     *      - almacenar todos los datos utiles de un restaurante en favoritos de forma local
     *      y refrescarlo por ejemplo cada mes para ahorrar busquedas
     *      puedes buscar por place details la info nueva que necesites
     * */
    public void parsePlaceDetailsJSON(){
        try {
            JSONObject jObject = init.getJSONObject("result");

            //Informacion general
            String id = jObject.getString("place_id");
            String nombre = jObject.getString("name");
            float rating = jObject.getLong("rating");
            String url = jObject.getString("url");
            String website = jObject.getString("website");
            String direccion = jObject.getString("formatted_address");
            String abierto = jObject.getString("business_status");
            String numeroTelefono = jObject.getString("formatted_phone_number");
            float lat = jObject.getJSONObject("geometry").getJSONObject("location").getLong("lat");
            float lng = jObject.getJSONObject("geometry").getJSONObject("location").getLong("lng");
            String icono = jObject.getString("icon");

            //Direccion extensa (idk, creo que no vale)
            JSONArray datosDirecciones = jObject.getJSONArray("address_components");
            for(int i = 0; i < datosDirecciones.length(); i++) {

            }

            //Horarios de apertura
            ArrayList<String> horarios = new ArrayList<String>();
            JSONArray datosHorarios = jObject.getJSONObject("opening_hours").getJSONArray("weekday_text");
            for (int i = 0; i < datosHorarios.length(); i++){
                horarios.add(datosHorarios.getString(i));
            }
            //Fotos
            ArrayList<Foto> fotos = new ArrayList<Foto>();
            JSONArray datosFotos = jObject.getJSONArray("photos");
            for (int i = 0; i < datosFotos.length(); i++){
                String dir = datosFotos.getJSONObject(i).getString("photo_reference");
                int h = datosFotos.getJSONObject(i).getInt("height");
                int w = datosFotos.getJSONObject(i).getInt("width");
                fotos.add(new Foto(dir, h, w));
            }

            Restaurante r = new Restaurante(id, nombre, abierto, rating, icono, null, fotos);
            System.out.println(r);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void parseFindPlaceJSON(){
        try {
            JSONObject jObject = init.getJSONObject("result");
            JSONArray resultados = new JSONArray("nombre de la variable");

            for(int i = 0; i < resultados.length(); i++) {
                String direccion = resultados.getJSONObject(i).getString("formatted_address");
                float lat = resultados.getJSONObject(i).getJSONObject("geometry").getLong("lat");
                float lng = resultados.getJSONObject(i).getJSONObject("geometry").getLong("lng");
                String nombre = resultados.getJSONObject(i).getString("name");
                float valoracion = resultados.getJSONObject(i).getLong("rating");
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    //TODO: Hacer parse de text search
    public void parseTextSearchPlaceJSON(String s){

    }
}
