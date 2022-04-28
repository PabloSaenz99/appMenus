package ucm.appmenus.utils;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import ucm.appmenus.R;
import ucm.appmenus.entities.Restaurante;

//https://www.tutorialspoint.com/web-scrapping-in-android-application
public class WebScrapping {

    private static int CONTADOR = 0;

    private final String url;
    private final MutableLiveData<Set<String>> listaFiltros;
    private final MutableLiveData<List<Bitmap>> listaImagenes;

    /**
     * Clase que realiza una busqueda mediante web scrapping. Todas las busquedas se realiazn en Threads
     * creados por las propias funciones llamadas.
     * Preferible crear un objeto por clase que deba realizar web screpping y que mantenga vivo este
     * objeto durante toda la vida del objeto interesado en realziar la bsuqueda.
     * @param url: pagina web sobre la que realizar la busqueda
     * @param listaFiltros: lista de filtros en la que se guardara el resultado de la busqueda.
     *                    Es un MutableLiveData para poder actualizar la interfaz en tiempo real.
     * @param listaImagenes: lista de imagenes en la que se guardara el resultado de la busqueda.
     *                    Es un MutableLiveData para poder actualizar la interfaz en tiempo real.
     */
    public WebScrapping(String url, MutableLiveData<Set<String>> listaFiltros,
                        MutableLiveData<List<Bitmap>> listaImagenes) {
        this.url= url;
        this.listaFiltros = listaFiltros;
        this.listaImagenes = listaImagenes;
    }

    /**
     * Busca la imagen principal del lugar. En caso de no haber, no modifica la de por defecto.
     * Utilizada para establecer unicamente una imagen y asi no consumir mucho tiempo, debe llamarse
     *      en la bsuqueda general de restaurantes.
     * Crea su propio Thread.
     */
    public void setImagenPrincipal() {
        Thread th = new Thread(new Runnable() {
            @Override
            public void run() {
                int aux0 = ++CONTADOR;
                try {
                    Document document = Jsoup.connect(url).get();
                    Elements imagenes = document.getElementsByTag("img");
                    if(imagenes != null){
                        //Distintos tipos de busqueda de imagenes
                        InputStream is;
                        if(!imagenes.get(0).attr("src").startsWith("http")) {
                            try {
                                is = new URL(url + imagenes.get(0).attr("src")).openStream();
                            }catch (IOException e){
                                is = new URL(imagenes.get(1).attr("src")).openStream();
                            }
                        }
                        else
                            is = new URL(imagenes.get(0).attr("src")).openStream();
                        InputStream finalIs = is;
                        listaImagenes.postValue(new ArrayList<Bitmap>(){{add(BitmapFactory.decodeStream(finalIs));}});
                    }
                } catch (Exception ignored) {}
                //Log.d("CONTADOR IMG:", aux0 + ", " + --CONTADOR);
            }
        });
        th.start();
    }
    /**
     * Busca y guarda todas las imagenes del lugar. En caso de no haber, deja la de por defecto.
     * Utilizada para establecer todas las imagenes, debe llamarse en la bsuqueda de un lugar
     *      especifico, no en la general.
     * Crea su propio Thread.
     */
    public void setImagenes() {
        Thread th = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Document document = Jsoup.connect(url).get();
                    Elements imagenes = document.getElementsByTag("img");
                    if(imagenes != null){
                        ArrayList<Bitmap> imagenesAux = new ArrayList<>();
                        for (Element element : imagenes) {
                            try {
                                InputStream is = new URL(element.attr("src")).openStream();
                                imagenesAux.add(BitmapFactory.decodeStream(is));
                            } catch (IOException ignored){}
                        }
                        listaImagenes.postValue(imagenesAux);
                    }
                } catch (Exception ignored) {}
            }
        });
        th.start();
    }

    /**
     * Establece los filtros del lugar.
     * @param filtros: lista con todos los filtros a buscar.
     * Crea su propio Thread.
     */
    public void setFiltros(final List<List<String>> filtros) {
        //Necesario el thread para no lanzar excepciones y no sobrecargar el hilo principal
        Thread th = new Thread(new Runnable() {
            @Override
            public void run() {
                int aux0 = ++CONTADOR;
                try {
                    //Busca si existen palabras como "menu" o "carta"
                    Document document = Jsoup.connect(url).get();
                    Element elementoMenu = document.select("a:contains(menu)").first();   //Buscar tambien sin tilde
                    Element elementoCarta = document.select("a:contains(order)").first();  //Buscar tambien por "carta"

                    //Accede a dichos parametros, cargando su enlace (href)
                    String textoURL = "";
                    if(elementoMenu != null)
                        textoURL += obtenerTextoMenu(url, elementoMenu.attr("href"));
                    else if(elementoCarta != null) //TODO: hacerlo solo else?
                        textoURL += obtenerTextoMenu(url, elementoCarta.attr("href"));
                    listaFiltros.getValue().addAll(buscarTags(textoURL, filtros));
                    buscarPrecios(textoURL);

                    if(elementoMenu != null || elementoCarta!= null) {
                        listaFiltros.postValue(listaFiltros.getValue());
                    }
                } catch (Exception ignored) {}
                //Log.d("CONTADOR FIL:", aux0 + ", " + --CONTADOR);
            }
        });
        th.start();
    }

    /**
     * Transforma la url de la carta en un texto legible
     * @param url url del establecimiento
     * @param urlCarta url del elemento (boton generalmente) de la carta/menu
     * @return el html de la carta en modo texto
     */
    private String obtenerTextoMenu(String url, String urlCarta) {
        if(urlCarta.startsWith("/"))
            urlCarta = url + urlCarta;
        try {
            return Jsoup.connect(urlCarta).get().text().toLowerCase();
        } catch (IOException e) {
            return "";
        }
    }

    /**
     * Busca en la url todos los filtros del parametro filtros
     * @param texto string del texto de la URL del restaurante (llamar antes a {@link #obtenerTextoMenu(String, String)}
     * @param filtros los filtros a buscar en la pagina web
     * @return un HashSet con todos los filtros encontrados en la web, sin repetidos
     */
    @NonNull
    private HashSet<String> buscarTags(String texto, List<List<String>> filtros){
        HashSet<String> nuevosFiltros = new HashSet<>();
        for (List<String> l: filtros)
            for (String s: l)
                if(texto.contains(s))
                    nuevosFiltros.add(s);
        return nuevosFiltros;
    }

    private List<Double> buscarPrecios(String texto){
        List<Double> res = new ArrayList<>();
        try {
            //Pattern original: (?=\\$)*\\d+(,|.)\\d+
            Pattern pattern = Pattern.compile("\\$\\d+(,|.)\\d");
            Matcher matcher = pattern.matcher(texto);
            while (matcher.find()) {
                res.add(Double.valueOf(matcher.group().replace("$", "")));
            }
            Log.i("res", res.toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return res;
    }
}