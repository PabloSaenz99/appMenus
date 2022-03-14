package ucm.appmenus.utils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Constantes {

    public static final String ACTUALIZAR_INTENT = "Actualizar intent";
    public static final String TIPOS_LOCAL = "Tipos de local";
    public static final String TIPOS_COCINA = "Tipos de cocina";
    public static final String AREA = "Area de busqueda";
    public static final String LISTA_RESTAURANTES = "Lista de restaurantes";

    /**
     * Filtros
     * */
    //https://wiki.openstreetmap.org/wiki/Category:Food_and_beverages
    public static final List<String> filtrosLocal = Collections.unmodifiableList(new ArrayList<String>(){{
        add("bar"); add("cafe"); add("fast+food"); add("nightclub"); add("pub"); add("restaurant");
    }});

    //Las 3 siguientes (comida, pais y postres) se podrian poner en una unica lista
    //https://wiki.openstreetmap.org/wiki/Key:cuisine
    public static final  List<String> filtrosComida = Collections.unmodifiableList(new ArrayList<String>(){{
        add("barbecue"); add("burger"); add("chicken"); add( "curry"); add("fish"); add("hot+dog");
        add("kebab"); add("noodle"); add("pasta"); add("pizza"); add("ramen"); add("sandwich");
        add("seafood"); add("steak-house"); add("sushi");  add("tapas");
    }});
    public static final List<String> filtrosPais = Collections.unmodifiableList(new ArrayList<String>(){{
        add("asian"); add("brazilian"); add("greek"); add("indian"); add("indonesian");
        add("italian"); add("japanese"); add("korean"); add("mediterranean"); add("mexican");
        add("regional"); add("spanish"); add("thai"); add("traditional");
    }});
    public static final List<String> filtrosPostres = Collections.unmodifiableList(new ArrayList<String>(){{
        add("cake"); add("coffe+shop"); add("crepe"); add("dessert"); add("ice+cream");
        add("waffle"); add("teahouse");
    }});

    //https://wiki.openstreetmap.org/wiki/Key:drink
    public static final String[] filtrosBebida = {};

    //https://wiki.openstreetmap.org/wiki/Key:diet
    public static final String[] filtrosDieta = {};
}
