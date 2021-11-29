package ucm.appmenus.ficheros;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import ucm.appmenus.entities.Foto;
import ucm.appmenus.entities.Restaurante;

public class JSONOpenStreetReader {

    public ArrayList<Restaurante> parsearResultado(final String s){
        ArrayList<Restaurante> restaurantes = new ArrayList<Restaurante>();
        try {
            JSONArray jArray = new JSONObject(s).getJSONArray("elements");
            for (int k = 0; k < jArray.length(); k++) {
                JSONObject jObject = jArray.getJSONObject(k);
                JSONObject info = jObject.getJSONObject("tags");
                //Informacion general
                String id = getStringFor(jObject, "id");

                String nombre = getStringFor(info, "name");
                String url =  getStringFor(info, "website");
                String dir =  getStringFor(info, "addr:street");
                dir += getStringFor(info, "addr:housenumber");
                int telefono = getIntFor(info,"contact:phone");
                String horario = getStringFor(info, "opening_hours");

                //Filtros
                ArrayList<String> filtros = new ArrayList<String>();
                filtros.add(getStringFor(info, "amenity"));
                filtros.add(getStringFor(info, "cuisine"));

                //TODO: Datos que no existen en OpenStreet, ver como obtenerlos
                //float valoracion = jObject.getLong("valoracion");
                //String imagenPrincDir = jObject.getString("imagenPrincDir");
                float valoracion = 0;
                String imagenPrincDir = "";

                restaurantes.add(new Restaurante(id, nombre, url, dir, telefono, horario,
                        valoracion, imagenPrincDir, filtros, null));
                System.out.println(restaurantes.get(k));
            }
        }
        catch (JSONException e) {
            e.printStackTrace();
        }
        return restaurantes;
    }

    private String getStringFor(JSONObject jo, String s){
        try{
            return jo.getString(s);
        }
        catch (JSONException e) {
            return "";
        }
    }

    private int getIntFor(JSONObject jo, String s){
        try{
            return jo.getInt(s);
        }
        catch (JSONException e) {
            return 0;
        }
    }
}
