package ucm.appmenus.utils;

import android.os.AsyncTask;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;

//https://www.tutorialspoint.com/web-scrapping-in-android-application
public class WebScrapping {

    public void setFiltros(final String url, final MutableLiveData<HashSet<String>> listaFiltros) {
        //Necesario el thread para no lanzar excepciones y no sobrecargar el hilo principal
        Thread th = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Log.i("URL", url);
                    Document document = Jsoup.connect(url).get();
                    Element elementoMenu = document.select("a:contains(menu)").first();   //Buscar tambien sin tilde
                    Element elementoCarta = document.select("a:contains(order)").first();  //Buscar tambien por "carta"
                    Elements imagenes = document.getElementsByTag("img");
                    if(imagenes != null){
                        /*
                        for (Element element : imagenes) {
                            Log.i("src: ", element.attr("src"));
                        }
                        */
                    }
                    if(elementoMenu != null)
                        listaFiltros.getValue().addAll(buscarTags(url, elementoMenu.attr("href")));
                    if(elementoCarta != null)
                        listaFiltros.getValue().addAll(buscarTags(url, elementoCarta.attr("href")));
                    if(elementoMenu != null || elementoCarta!= null) {
                        listaFiltros.postValue(listaFiltros.getValue());
                    }
                } catch (Exception ignored) {}
            }
        });
        th.start();
    }

    @NonNull
    private HashSet<String> buscarTags(String url, String urlCarta){
        HashSet<String> nuevosFiltros = new HashSet<>();
        if(urlCarta.startsWith("/")) urlCarta = url + urlCarta;
        try {
            String res = Jsoup.connect(urlCarta).get().text().toLowerCase();
            //TODO: Hacer con todos los filtros
            nuevosFiltros.add("WEBSCRAPPING");
            for (String s: Constantes.filtrosComida)
                if(res.contains(s))
                    nuevosFiltros.add(s);
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