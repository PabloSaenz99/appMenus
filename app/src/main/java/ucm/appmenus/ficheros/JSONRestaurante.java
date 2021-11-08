package ucm.appmenus.ficheros;

import android.content.Context;
import android.net.Uri;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;

import ucm.appmenus.Foto;
import ucm.appmenus.Restaurante;

/**
 * Lee los JSON devueltos por:
 *
 * https://developers.google.com/maps/documentation/places/web-service/search-find-place
 * */
public class JSONRestaurante {

    /**
     * Al buscar places usar las etiquetas: bar, cafe, restaurant (meal_delivery, meal_takeaway)
     *
     * https://developers.google.com/maps/documentation/places/web-service/supported_types
     */

    private OutputStream out;
    //private InputStream in;

    public JSONRestaurante(Context context, String file) throws JSONException, IOException {
        out = file == null ? System.out : new FileOutputStream(new File(file));
        //in = file == null ? System.in : new FileInputStream(new File(file));
    }

    public void writeRestaurantesJSON(ArrayList<Restaurante> restaurantes){
        try {
            PrintStream p = new PrintStream(out);
            p.println("{");

            JSONArray arrayRestaurantes = new JSONArray("restaurantes");
            //Iterar sobre los restaurantes
            for (Restaurante r: restaurantes) {
                JSONObject jRestaurante = new JSONObject();
                jRestaurante.put("idRestaurante", r.getIdRestaurante());
                jRestaurante.put("nombre", r.getNombre());
                jRestaurante.put("url", r.getStringURL());
                jRestaurante.put("valoracion", r.getValoracion());
                jRestaurante.put("imagenPrincDir", r.getimagenPrincDir());
                //Array de filtros:
                    JSONArray jArrayFiltros = new JSONArray();
                    for(String filtro: r.getFiltros()) {
                        jArrayFiltros.put(filtro);
                    }
                    jRestaurante.put("filtros", jArrayFiltros);
                //Array de fotos:
                    JSONArray jArrayFotos = new JSONArray();
                    for(Foto foto: r.getFotos()) {
                        jArrayFiltros.put(foto.getJSONObject());
                    }
                    jRestaurante.put("fotos", jArrayFotos);
                //Añadir el restaurante al array
                arrayRestaurantes.put(jRestaurante);
            }
            //TODO: Guardar (puede que ya esté(?))
            p.println("}");
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public ArrayList<Restaurante> readRestaurantesJSON(){
        ArrayList<Restaurante> restaurantes = new ArrayList<Restaurante>();
        try {
            //TODO: Pasar bien el argumento
            JSONObject jo = new JSONObject(new JSONTokener("in"));
            JSONArray jArray = jo.getJSONArray("restaurantes");
            for (int k = 0; k < jArray.length(); k++) {
                JSONObject jObject = jArray.getJSONObject(k);
                //Informacion general
                String id = jObject.getString("place_id");
                String nombre = jObject.getString("name");
                String url = jObject.getString("url");
                float valoracion = jObject.getLong("rating");
                String imagenPrincDir = jObject.getString("imagenPrincDir");
                //Filtros
                ArrayList<String> filtros = new ArrayList<String>();
                JSONArray jArrayFiltros = jObject./*getJSONObject("filtros").*/getJSONArray("filtros");
                for (int i = 0; i < jArrayFiltros.length(); i++) {
                    filtros.add(jArrayFiltros.getString(i));
                }
                //Fotos
                ArrayList<Foto> fotos = new ArrayList<Foto>();
                JSONArray jArrayFotos = jObject.getJSONArray("fotos");
                for (int i = 0; i < jArrayFotos.length(); i++) {
                    fotos.add(new Foto(jArrayFotos.getJSONObject(i)));
                }
                restaurantes.add(new Restaurante(id, nombre, url, valoracion, imagenPrincDir, filtros, fotos));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return restaurantes;
    }
}
