package ucm.appmenus.utils;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

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

import ucm.appmenus.entities.Restaurante;

//https://www.tutorialspoint.com/web-scrapping-in-android-application
public class WebScrapping {

    private final String url;
    private final MutableLiveData<HashSet<String>> listaFiltros;
    private final MutableLiveData<ArrayList<Bitmap>> listaImagenes;

    public WebScrapping(String url, MutableLiveData<HashSet<String>> listaFiltros,
                        MutableLiveData<ArrayList<Bitmap>> listaImagenes) {
        this.url= url;
        this.listaFiltros = listaFiltros;
        this.listaImagenes = listaImagenes;
    }

    public void setImagenPrincipal() {
        Thread th = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Document document = Jsoup.connect(url).get();
                    Elements imagenes = document.getElementsByTag("img");
                    if(imagenes != null){
                        InputStream is = new URL(imagenes.get(0).attr("src")).openStream();
                        listaImagenes.postValue(new ArrayList<Bitmap>(){{add(BitmapFactory.decodeStream(is));}});
                    }
                } catch (Exception ignored) {}
            }
        });
        th.start();
    }

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
                            //Log.i("src: ", element.attr("src"));
                            InputStream is = new URL(element.attr("src")).openStream();
                            imagenesAux.add(BitmapFactory.decodeStream(is));
                        }
                        //InputStream is = new URL(imagenes.get(0).attr("src")).openStream();
                        //listaImagenes.postValue(BitmapFactory.decodeStream(is));
                        listaImagenes.postValue(imagenesAux);
                    }
                } catch (Exception ignored) {}
            }
        });
        th.start();
    }

    public void setFiltros(final List<List<String>> buscar) {
        //Necesario el thread para no lanzar excepciones y no sobrecargar el hilo principal
        Thread th = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    //Log.i("URL", url);
                    Document document = Jsoup.connect(url).get();
                    Element elementoMenu = document.select("a:contains(menu)").first();   //Buscar tambien sin tilde
                    Element elementoCarta = document.select("a:contains(order)").first();  //Buscar tambien por "carta"

                    if(elementoMenu != null)
                        listaFiltros.getValue().addAll(buscarTags(url, elementoMenu.attr("href"), buscar));
                    if(elementoCarta != null)
                        listaFiltros.getValue().addAll(buscarTags(url, elementoCarta.attr("href"), buscar));
                    if(elementoMenu != null || elementoCarta!= null) {
                        listaFiltros.postValue(listaFiltros.getValue());
                    }
                } catch (Exception ignored) {}
            }
        });
        th.start();
    }

    @NonNull
    private HashSet<String> buscarTags(String url, String urlCarta, List<List<String>> filtros){
        HashSet<String> nuevosFiltros = new HashSet<>();
        if(urlCarta.startsWith("/")) urlCarta = url + urlCarta;
        try {
            String res = Jsoup.connect(urlCarta).get().text().toLowerCase();
            //TODO: Hacer con todos los filtros
            //nuevosFiltros.add("WEBSCRAPPING");
            for (List<String> l: filtros) {
                for (String s: l)
                    if(res.contains(s))
                        nuevosFiltros.add(s);
            }
        } catch (IOException e) {
            //e.printStackTrace();
        }
        return nuevosFiltros;
    }
}

    /*extends AsyncTask<Void,Void, ArrayList<String>> {

    String  url;
    ArrayList<String> filtros;
    MutableLiveData<ArrayList<String>> liveFiltros;

    public WebScrapping(String url, ArrayList<String> filtros,  MutableLiveData<ArrayList<String>> live){
        this.url = url;
        this.filtros = filtros;
        this.liveFiltros = live;
        this.execute();
    }

    @Override
    protected ArrayList<String> doInBackground(Void ... voids) {
        ArrayList<String> filtros = new ArrayList<String>();
        try {
            Document document = Jsoup.connect(url).get();
            Element m1 = document.select("a:contains(menu)").first();   //Buscar tambien sin tilde
            Element m2 = document.select("a:contains(carta)").first();
            if(m1 != null)
                filtros.addAll(buscarTags(m1.attr("href")));

        } catch (IOException | IllegalArgumentException e) {
            Log.e("Error", e.toString());
        }
        return filtros;
    }

    @Override
    protected void onPostExecute(ArrayList<String> res) {
        if(!res.isEmpty()) {
            Log.i("Filtros base", filtros.toString());
            filtros.addAll(res);
            liveFiltros.postValue(filtros);
            Log.i("Filtros final", filtros.toString()); //Falta que los muestre idk
        }
    }

    private ArrayList<String> buscarTags(String urlCarta){
        ArrayList<String> filtros = new ArrayList<String>();
        if(urlCarta.startsWith("/")) urlCarta = url + urlCarta;
        try {
            String res = Jsoup.connect(urlCarta).get().text().toLowerCase();
            Log.i("Salida sin menu", url);
            Log.i("Salida con menu", urlCarta);
            Log.i("Res", res);
            //TODO: Hacer con todos los filtros
            for (String s: Constantes.filtrosComida)
                if(res.contains(s)) filtros.add(s);
        } catch (IOException e) {
            e.printStackTrace();
        }
        Log.i("Filtros", filtros.toString());
        return filtros;
    }
}*/