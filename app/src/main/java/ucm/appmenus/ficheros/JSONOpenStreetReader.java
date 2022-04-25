package ucm.appmenus.ficheros;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import ucm.appmenus.entities.Restaurante;
import ucm.appmenus.utils.BaseDatos;
import ucm.appmenus.utils.Localizacion;
import ucm.appmenus.utils.OpenStreetMap;

public class JSONOpenStreetReader {

    private final ArrayList<String> websites = new ArrayList<String>(){
        {add("website"); add("contact:website"); add("contact:facebook");}
    };

    public ArrayList<Restaurante> parsearResultado(final String s, final double latUsuario, final double lonUsuario){
        ArrayList<Restaurante> restaurantes = new ArrayList<Restaurante>();
        try {
            JSONArray jArray = new JSONObject(s).getJSONArray("elements");
            for (int k = 0; k < jArray.length(); k++) {
                JSONObject jObject = jArray.getJSONObject(k);
                double lat = getDoubleFor(jObject, "lat");
                double lon = getDoubleFor(jObject, "lon");
                JSONObject info = jObject.getJSONObject("tags");
                //Informacion general
                String id = getStringFor(jObject, "id");

                String nombre = getStringFor(info, "name");
                String url =  getStringFor(info, websites);

                String dir = getStringFor(info, "addr:street") + ", " + getStringFor(info, "addr:housenumber");

                int telefono = getIntFor(info,"contact:phone");
                String horario = getStringFor(info, "opening_hours");

                //Filtros
                ArrayList<String> filtros = new ArrayList<>();
                filtros.add(getStringFor(info, "amenity"));
                String cuisineAux = getStringFor(info, "cuisine");
                if(!cuisineAux.equals(""))
                    filtros.add(cuisineAux);
                restaurantes.add(new Restaurante(id, nombre, url, dir, lat, lon,
                        Localizacion.distanciaEnMetros(lat, lon, latUsuario, lonUsuario),
                        telefono, horario, 0, filtros));
            }
        }
        catch (JSONException e) {
            e.printStackTrace();
        }
        return restaurantes;
    }

    public String parsearDireccion(final String res){
        String dir = "";
        try {
            JSONObject info = new JSONObject(res).getJSONObject("address");
            dir = getStringFor(info, "road") + ", " + getStringFor(info, "house_number");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        Log.i("Dir es", dir);
        return dir;
    }

    private String getStringFor(JSONObject jo, ArrayList<String> opciones) {
        for (String s: opciones) {
            try {
                return jo.getString(s);
            } catch (JSONException ignored) {}
        }
        return null;
    }

    private String getStringFor(JSONObject jo, String s){
        try{
            return jo.getString(s);
        }
        catch (JSONException e) {
            return "";
        }
    }

    private int getIntFor(JSONObject jo, String s) {
        try {
            return jo.getInt(s);
        } catch (JSONException e) {
            return 0;
        }
    }

    private double getDoubleFor(JSONObject jo, String s) {
        try {
            return jo.getDouble(s);
        } catch (JSONException e) {
            return 0;
        }
    }
}
