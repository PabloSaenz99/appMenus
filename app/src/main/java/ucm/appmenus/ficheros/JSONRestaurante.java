package ucm.appmenus.ficheros;

import android.content.Context;
import android.os.Environment;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;

import java.io.InputStreamReader;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;

import ucm.appmenus.Foto;
import ucm.appmenus.R;
import ucm.appmenus.Restaurante;

public class JSONRestaurante {

    private Context context;
    private String inFile;
    private String outFile;
    private JSONArray arrayRestaurantesWrite;

    /**
     * Lee los JSON que contienen los restaurantes favoritos, para no tener que llamar a places cada vez
     */

    public JSONRestaurante(Context context, String inFile, String outFile) {
        this.outFile = outFile;
        this.context = context;
        this.inFile = inFile;
        try {
            arrayRestaurantesWrite = new JSONObject(ManejadorFicheros.fileToString(context, inFile))
                    .getJSONArray("restaurantes");
        }
        catch (IOException e){
            //Crea el fichero vacio
            ManejadorFicheros.crearFichero(context.getFilesDir(),
                    context.getString(R.string.ucm_appmenus_restaurantesFavoritos),
                    "{\"restaurantes\":[]}");
            try {
                //Carga de nuevo el array
                arrayRestaurantesWrite = new JSONObject(ManejadorFicheros.fileToString(context, inFile))
                        .getJSONArray("restaurantes");
            } catch (JSONException | IOException ex) {
                ex.printStackTrace();
            }
        }
        catch (JSONException e) {
            e.printStackTrace();
        }
    }
    /**
     * Carga la lista de restaurantes del fichero de entrada proporcionado al constructor
     * */
    public ArrayList<Restaurante> readRestaurantesJSON(){
        ArrayList<Restaurante> restaurantes = new ArrayList<Restaurante>();
        try {
            for (int k = 0; k < arrayRestaurantesWrite.length(); k++) {
                JSONObject jObject = arrayRestaurantesWrite.getJSONObject(k);
                //Informacion general
                String id = jObject.getString("idRestaurante");
                String nombre = jObject.getString("nombre");
                String url = jObject.getString("url");
                float valoracion = jObject.getLong("valoracion");
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
        }
        catch (JSONException e) {
            e.printStackTrace();
        }
        return restaurantes;
    }

    /**
     * Escribe la lista de restaurantes en el fichero de salida proporcionado al constructor
     * */
    public void writeRestaurantesJSON(ArrayList<Restaurante> restaurantes){
        try {
            FileOutputStream out = context.openFileOutput(outFile, Context.MODE_PRIVATE);
            String p = "{\"restaurantes\":";
            //arrayRestaurantesWrite = new JSONArray();
            //Iterar sobre los restaurantes
            for (Restaurante r: restaurantes) {
                writeRestaurante(r);
            }
            p+=arrayRestaurantesWrite+"}";
            out.write(p.getBytes());
            out.close();
        } catch (JSONException | IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Aniade un restaurante al fichero de salida proporcionado al constructor
     * */
    public void writeRestauranteJSON(Restaurante r){
        ArrayList<Restaurante> restaurantes = readRestaurantesJSON();
        restaurantes.add(r);
        writeRestaurantesJSON(restaurantes);
    }

    private void writeRestaurante(Restaurante r) throws JSONException {
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
        arrayRestaurantesWrite.put(jRestaurante);
    }
}
