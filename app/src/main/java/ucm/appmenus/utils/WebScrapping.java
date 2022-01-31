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
public class WebScrapping extends AsyncTask<Void,Void, ArrayList<String>> {

    String  url;
    ArrayList<String> filtros;

    public WebScrapping(String url, ArrayList<String> filtros){
        this.url = url;
        this.filtros = filtros;
        this.execute();
    }

    @Override
    protected ArrayList<String> doInBackground(Void... params) {
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
}