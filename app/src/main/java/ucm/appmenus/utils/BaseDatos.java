package ucm.appmenus.utils;

import java.util.ArrayList;
import java.util.HashSet;

import ucm.appmenus.entities.Restaurante;

public class BaseDatos {

    private static BaseDatos bd = new BaseDatos();
    private static final HashSet<String> arrayOperadores = new HashSet<String>(){
        {add("=");add(">");add("<");add(">=");add("<=");add("<>");add("!=");
            add("AND");add("OR");add("NOT");
            //Tambien valen pero no las usamos
            //add("BETWEEN");add("LIKE");add("IN");
        }};
    private static final HashSet<String> arrayFiltros = new HashSet<String>(){
        {add("precio");add("valoracion");add("vegana");}};
    private static final HashSet<String> arrayOrden = new HashSet<String>(){
        {add("ASC");add("DESC");}};

    private String query;
    private boolean queryCorrecta;

    public static BaseDatos getInstance(){
        return bd;
    }

    private BaseDatos(){
        query = "";
        queryCorrecta = false;
    }

    public void parsearFiltros(ArrayList<String> camposTabla, String nombreTabla, ArrayList<String> filtros,
                               ArrayList<String> operadores, ArrayList<String> ordenacion){
        //Comprueba que los campos de la tabla son correctos
        String campoTabla = "";
        if(camposTabla != null &&
                //O tiene por unico parametro "*" o todos los parametros estan en la lista de filtros
                ((camposTabla.size() == 1 && camposTabla.get(0).equalsIgnoreCase("*")) ||
                arrayFiltros.containsAll(filtros))){
            for (String c: camposTabla) {
                campoTabla += c + ", ";
            }
            //Es posible que falte quitar la "," del final
        }
        else{
            return;
        }
        //Comprueba que el nombre de la tabla sea correcto
        if(!arrayFiltros.contains(nombreTabla)){
            return;
        }

        //Comprueba que los filtros son correctos
        String filtro = "null";
        if(filtros != null && arrayFiltros.containsAll(filtros)){
            filtro = " WHERE ";
            for (String f: filtros) {
                filtro += f + ", ";
            }
        }
        //Comprueba que el orden sea valido
        String orden = "null";
        if(ordenacion != null && arrayOrden.containsAll(ordenacion)){
            orden = " ORDER BY ";
            for (String o: ordenacion) {
                orden = " ORDER BY " + o;
            }
        }

        queryCorrecta = true;
        query = "SELECT " + campoTabla + " FROM " + nombreTabla + filtro + orden;
    }

    /**
     * Devuelve una lista con los restaurantes
     * IMPORTANTE: antes hay que llamar a parsearFiltros o devolverá una lista vacia
     * */
    public ArrayList<Restaurante> cargarRestaurantes(){
        ArrayList<Restaurante> restaurantes = new ArrayList<Restaurante>();
        if(queryCorrecta) {
            /*
            TODO: Tiene que llamar a placedetails y cargar la info del restaurante
            (puede guardar en la bd la informacion de los tipos de comida del restaurante junto con
            el id de maps del restaurante (seria clave primaria))
            */
        }
        else {
            //Aqui habria que devolver un error, mientras devuelvo datos de pruueba
            restaurantes.add(new Restaurante("0","El mexicano", "null",
                    "Calle Mayor", 6950, "Abierto", 3.9f,
                    "/data/data/ucm.appmenus/files/mexicano.jpg",
                    new ArrayList<String>(){{add("Mexicana");add("Tacos");add("Picante");}},
                    //new ArrayList<String>(){{add("img1");add("img2");add("img3");}}
                    null));
            restaurantes.add(new Restaurante("1","La Fabada", "null",
                    "Calle Mayor", 6950, "Abierto",4.4f,
                    "/data/data/ucm.appmenus/files/asturiano.jpg",
                    new ArrayList<String>(){{add("Asturiana");add("Fabada");add("Casera");}},
                    //new ArrayList<String>(){{add("img1");add("img2");add("img3");}}
                    null));
            restaurantes.add(new Restaurante("2","VIPS", "null",
                    "Calle Mayor", 6950, "Abierto",3.7f,
                    "/data/data/ucm.appmenus/files/vips.jpg",
                    new ArrayList<String>(){{add("Hamburguesa");add("Tacos");}},
                    //new ArrayList<String>(){{add("img1");add("img2");}}
                    null));
            restaurantes.add(new Restaurante("3","Kebab", "null",
                    "Calle Mayor", 6950, "Abierto",5f,
                    "/data/data/ucm.appmenus/files/kebab.jpeg",
                    new ArrayList<String>(){{add("Turco");add("Kebab");}},
                    //new ArrayList<String>(){{add("img1");add("img2");}}
                    null));
            restaurantes.add(new Restaurante("4","Telepizza", "null",
                    "Calle Mayor", 6950, "Abierto",3.5f,
                    "/data/data/ucm.appmenus/files/telepizza.png",
                    new ArrayList<String>(){{add("Pizza");}},
                    //new ArrayList<String>(){{add("img1");add("img2");}}
                    null));
        }
        return restaurantes;
    }
}
