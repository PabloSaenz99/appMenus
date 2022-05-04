package ucm.appmenus.utils;

import android.content.Context;
import android.util.Log;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

public class Constantes {

    public static final String ACTUALIZAR_INTENT = "Actualizar intent";
    public static final String TIPOS_DIETA = "Tipos de dieta";
    public static final String TIPOS_LOCAL = "Tipos de local";
    public static final String TIPOS_COCINA = "Tipos de cocina";
    public static final String AREA = "Area de busqueda";
    public static final String LISTA_RESTAURANTES = "Lista de restaurantes";
    public static final String FILTROS_BUSQUEDA = "Filtros aplicados: ";
    public static final String ID_USUARIO = "ID usuario";
    public static final String EMAIL_USUARIO = "Email usuario";
    public static final String NOMBRE_USUARIO = "Nombre usuario";
    public static final String RESTAURANTE = "Restaurante";
    public static final String RESENIA = "Resenia";

    public static final String EMAIL_INVITADO = "invitado@invitado.es";
    public static final String NOMBRE_INVITADO = "invitado";
    public static final String PASSWORD_INVITADO = "invitado";
    public static final String NECESARIO_LOGIN = "Login necesario";


    public static Set<String> filtrosLocalIngles() {return filtrosLocal.keySet();}
    public static Set<String> filtrosComidaIngles() {return filtrosComida.keySet();}
    public static Set<String> filtrosPaisIngles() {return filtrosPais.keySet();}
    public static Set<String> filtrosPostresIngles() {return filtrosPostres.keySet();}
    public static Set<String> filtrosDietaOSMIngles() {return filtrosDietaOSM.keySet();}
    public static Set<String> filtrosDietaWSIngles() {return filtrosDietaWebScrapping.keySet();}

    public static String traducirAlEsp(String ing){ return ingles.get(ing); }
    public static ArrayList<String> traducirAlEsp(Collection<String> ing){
        ArrayList<String> res = new ArrayList<>();
        for (String s: ing)
            res.add(ingles.get(s));
        return res;
    }

    public static ArrayList<String> traducirAlIngles(Collection<String> esp){
        ArrayList<String> res = new ArrayList<>();
        for (String s: esp)
            res.add(espaniol.get(s));
        return res;
    }

    private static final Map<String, String> filtrosLocal = Collections.unmodifiableMap(new HashMap<String, String>(){{
        put("bar", "bar"); put("cafe", "cafetería"); put("fast+food", "comida rápida"); put("nightclub", "club nocturno");
        put("pub", "pub"); put("restaurant", "restaurante");
    }});

    private static final  Map<String, String> filtrosComida = Collections.unmodifiableMap(new HashMap<String, String>(){{
        put("barbecue", "barbacoa"); put("burger", "hamburguesa"); put("chicken", "pollo"); put( "curry", "curry");
        put("fish", "pescado"); put("hot+dog", "perrito caliente");
        put("kebab", "kebab"); put("noodle", "fideos"); put("pasta", "pasta"); put("pizza", "pizza"); put("ramen", "ramen"); put("sandwich", "sándwich");
        put("seafood", "marisco"); put("steak-house", "asador"); put("sushi", "sushi");  put("tapas", "tapas");
    }});
    private static final Map<String, String> filtrosPais = Collections.unmodifiableMap(new HashMap<String, String>(){{
        put("asian", "asiático"); put("brazilian", "brasileño"); put("greek", "griego"); put("indian", "indio"); put("indonesian", "indoneso");
        put("italian", "italiano"); put("japanese", "japonés"); put("korean", "coreano"); put("mediterranean", "mediterráneo"); put("mexican", "mexicano");
        put("regional", "regional"); put("spanish", "español"); put("thai", "tailandés"); put("traditional", "tradicional");
    }});
    private static final Map<String, String> filtrosPostres = Collections.unmodifiableMap(new HashMap<String, String>(){{
        put("cake", "tarta"); put("coffe+shop", "café"); put("crepe", "crepe"); put("dessert", "postres"); put("ice+cream", "helados");
        put("waffle", "gofre"); put("teahouse", "té");
    }});

    //https://wiki.openstreetmap.org/wiki/Key:diet
    private static final Map<String, String> filtrosDietaOSM = Collections.unmodifiableMap(new HashMap<String, String>(){{
        //para OSM
        put("vegetarian", "vegetariano"); put("vegan", "vegano"); put("gluten_free", "sin gluten"); put("lactose_free", "sin lactosa");
    }});

    private static final Map<String, String> filtrosDietaWebScrapping = Collections.unmodifiableMap(new HashMap<String, String>(){{
        //Para web Scrapping
        ;put("veggie", "veg"); put("allergen", "alérgenos");
    }});

    private static final Map<String, String> ingles = Collections.unmodifiableMap(new HashMap<String, String>(){{
        putAll(filtrosLocal); putAll(filtrosComida); putAll(filtrosPais); putAll(filtrosPostres); putAll(filtrosDietaOSM); putAll(filtrosDietaWebScrapping);
    }});

    private static final Map<String, String> espaniol = Collections.unmodifiableMap(new HashMap<String, String>(){{
        putAll(ingles.entrySet().stream().collect(Collectors.toMap(Map.Entry::getValue, Map.Entry::getKey)));
    }});
}
