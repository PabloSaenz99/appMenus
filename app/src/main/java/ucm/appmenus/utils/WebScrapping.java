package ucm.appmenus.utils;

import android.os.AsyncTask;
import android.util.Log;

import androidx.lifecycle.MutableLiveData;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.io.IOException;
import java.util.ArrayList;

//https://www.tutorialspoint.com/web-scrapping-in-android-application
public class WebScrapping {

    public void setFiltros(final String url, final MutableLiveData<ArrayList<String>> actualizable) {
        final ArrayList<String> filtros = new ArrayList<String>();
        //Necesario el thread para no lanzar excepciones y no sobrecargar el hilo principal
        Thread th = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Document document = Jsoup.connect(url).get();
                    Element m1 = document.select("a:contains(menu)").first();   //Buscar tambien sin tilde
                    //Element m2 = document.select("a:contains(carta)").first();
                    if(m1 != null)
                        filtros.addAll(buscarTags(url, m1.attr("href")));
                    actualizable.postValue(filtros);
                    if(actualizable.getValue() != null)
                        Log.i("Filtros post", actualizable.getValue().toString());
                    else
                        Log.i("Filtros post", "NULL");
                } catch (Exception ignored) {}
            }
        });
        th.start();
    }

    private ArrayList<String> buscarTags(String url, String urlCarta){
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