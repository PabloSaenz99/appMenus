package ucm.appmenus.utils;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

import ucm.appmenus.entities.Resenia;
import ucm.appmenus.entities.Restaurante;
import ucm.appmenus.entities.Usuario;

//https://firebase.google.com/docs/database/android/read-and-write#java_5
public class BaseDatos {


    private static final String RESENIAS = "Resenias";
    private static final String FAVORITOS = "Favoritos";
    private static final String RESTAURANTES = "Restaurantes";
    private static final String FILTROS_APROBADOS = "FiltrosAprobados";
    private static final String FILTROS_NO_APROBADOS = "FiltrosNoAprobados";


    private static BaseDatos instance;
    private final DatabaseReference databaseUsuarios, databaseRestaurantes, databaseResenias;
    private String userId;

    public static BaseDatos getInstance(){
        if(instance == null)
            instance = new BaseDatos();
        return instance;
    }

    private BaseDatos(){
        FirebaseUser rUser = FirebaseAuth.getInstance().getCurrentUser();
        String userId = rUser.getUid();
        databaseUsuarios = FirebaseDatabase.getInstance().getReference("Users").child(userId);
        databaseRestaurantes = FirebaseDatabase.getInstance().getReference(RESTAURANTES);
        databaseResenias = FirebaseDatabase.getInstance().getReference(RESENIAS);
    }

    /**
     * Añade una reseña a la Base de Datos. Se añade una nueva entrada en Reseñas y se añade su id en el
     * restaurante al cual pertenece y en el usaurio que la creó.
     * Los objetos activos actualmente en la app no se modifican.
     * @param resenia la reseña a añadir
     */
    public void addResenia(Resenia resenia){
        databaseResenias.child(resenia.getIdResenia()).setValue(resenia);
        databaseRestaurantes.child(resenia.getIdRestaurante()).child(RESENIAS).push().setValue(resenia.getIdResenia());
        databaseUsuarios.child(RESENIAS).push().setValue(resenia.getIdResenia());
    }

    /**
     * Añade filtros a un restaurante.
     * Los objetos activos actualmente en la app no se modifican.
     * @param idRestaurante el id de OpenStreetMap del restaurante
     * @param filtros los filtros nuevos
     */
    public void addFiltrosRestaurante(String idRestaurante, List<String> filtros){
        //Transforma los filtros a un mapa
        Map<String, Long> valoresNuevos = new HashMap<>();
        for (String s: filtros)
            valoresNuevos.put(s, 1L);
        //Obtiene los filtros guardados en la BD
        databaseRestaurantes.child(idRestaurante).child(FILTROS_NO_APROBADOS).get().addOnCompleteListener(task -> {
            if(task.isSuccessful()){
                Map<String, Long> valoresAntiguos = new HashMap<>();
                for (DataSnapshot d: task.getResult().getChildren()) {
                    Log.i("obtenido", d.toString());
                    valoresAntiguos.put(String.valueOf(d.getKey()), (Long) d.getValue());
                }
                //Mezcla ambos mapas y suma el contenido de sus valores
                valoresAntiguos.forEach((k, v) -> valoresNuevos.merge(k, v, Long::sum));
                //Actualiza la BD
                databaseRestaurantes.child(idRestaurante).child(FILTROS_NO_APROBADOS).setValue(valoresNuevos);
            }
        });
    }

    /**
     * Añade restaurantes favoritos al usuario actual.
     * Los objetos activos actualmente en la app no se modifican.
     * @param restaurantes el id de OpenStreetMap del restaurante
     */
    public void addFavoritosUsuario(List<String> restaurantes){
        databaseUsuarios.child(FAVORITOS).setValue(restaurantes);
    }

    /**
     *
     * @param idRestaurante
     * @return
     */
    public List<Resenia> getReseniasRestaurante(String idRestaurante){
        List<Resenia> resenias = new ArrayList<>();
        //Obtiene la lista de ids de las reseñas de ese restaurante
        databaseRestaurantes.child(idRestaurante).child(RESENIAS).get().addOnCompleteListener(task -> {
            if(task.isSuccessful()){
                for (DataSnapshot d: task.getResult().getChildren()) {
                    //Obtiene los datos de la reseña (busca el id de cada reseña)
                    databaseResenias.child(String.valueOf(d.getValue())).get().addOnCompleteListener(task1 -> {
                        //Añade las reseñas
                        resenias.add(parseResenia(task1.getResult()));
                    });
                }
            }
        });
        return resenias;
    }

    /**
     * //TODO: quiza hay que ponerlo como getReseniasRestaurante
     * Actualiza los filtros de un restaurante (añade los nuevos).
     * Esta funcion no deberia llamarse con la lista de filtros obtenidos de OpenStreetMap, sino con una distinta.
     * @param idRestaurante id del restaurante en OpenStreetMap
     * @param actualizable variable donde se almacenarán los nuevos filtros
     */
    public void setFiltrosRestaurante(String idRestaurante, MutableLiveData<Set<String>> actualizable){
        databaseRestaurantes.child(idRestaurante).child(FILTROS_NO_APROBADOS).get().addOnCompleteListener(task -> {
           if(task.isSuccessful()){
               Set<String> aux = actualizable.getValue();
               for (DataSnapshot d: task.getResult().getChildren())
                   aux.add(String.valueOf(d.getKey()));
               actualizable.postValue(aux);
           }
        });
    }

    /**
     * Actualiza los datos del usuario.
     * Actualiza el nombre, las reseñas y los favoritos.
     */
    public void setDatosUsuario(){
        databaseUsuarios.get().addOnCompleteListener(task -> {
            if(task.isSuccessful()){
                //Obtener nombre usuario
                Usuario.getUsuario().setNombre(String.valueOf(task.getResult().child("usuarioNombre").getValue()));
                //Obtener reseñas
                for (DataSnapshot d: task.getResult().child(RESENIAS).getChildren()) {
                    databaseResenias.child(String.valueOf(d.getValue())).get().addOnCompleteListener(task1 -> {
                        //Añadir las reseñas
                        Usuario.getUsuario().addResenia(parseResenia(task1.getResult()));
                    });
                }
                for (DataSnapshot d: task.getResult().child(FAVORITOS).getChildren()) {
                    //TODO: obtener datos de los restaurantes (sacarlos de openstreetmap)
                }
            }
        });
    }

    private Resenia parseResenia(DataSnapshot res) {
        return new Resenia(String.valueOf(res.child("idRestaurante").getValue()),
                String.valueOf(res.child("idUsuario").getValue()),
                String.valueOf(res.child("usuarioNombre").getValue()),
                String.valueOf(res.child("titulo").getValue()),
                String.valueOf(res.child("texto").getValue()),
                Double.parseDouble(String.valueOf(res.child("valoracion").getValue())));
    }
}
