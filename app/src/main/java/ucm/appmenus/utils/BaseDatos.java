package ucm.appmenus.utils;

import android.app.Activity;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import ucm.appmenus.MainActivity;
import ucm.appmenus.entities.Resenia;
import ucm.appmenus.entities.Usuario;

//https://firebase.google.com/docs/database/android/read-and-write#java_5
public class BaseDatos {


    private static final String RESENIAS = "Resenias";
    private static final String RESTAURANTES = "Restaurantes";
    private static final String VALORACION = "Valoracion";
    private static final String VALORACION_TOTAL = "Valoracion_total";
    private static final String NUM_VALORACIONES = "Numero_valoraciones";
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
        long ini = System.nanoTime();
        //TODO: añadir tiempo Log.i("BD", Calendar.getInstance().getTime().toString());
        databaseResenias.child(resenia.getIdResenia()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(!snapshot.exists()){                      //No existe una reseña, por lo que se añade a todas partes
                    databaseRestaurantes.child(resenia.getIdRestaurante()).child(RESENIAS).push().setValue(resenia.getIdResenia());
                    databaseUsuarios.child(RESENIAS).push().setValue(resenia.getIdResenia());
                }
                databaseResenias.child(resenia.getIdResenia()).setValue(resenia);
                MainActivity.medirTiempo("Añadir reseña", ini, System.nanoTime());
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) { }
        });
    }

    /**
     * Añade filtros a un restaurante en la BD.
     * Los objetos activos actualmente en la app no se modifican.
     * @param idRestaurante el id de OpenStreetMap del restaurante
     * @param filtros los filtros nuevos
     */
    public void addFiltrosRestaurante(String idRestaurante, List<String> filtros){
        long ini = System.nanoTime();
        //Transforma los filtros a un mapa
        Map<String, Long> valoresNuevos = new HashMap<>();
        for (String s: filtros)
            valoresNuevos.put(s, 1L);
        //Obtiene los filtros guardados en la BD
        databaseRestaurantes.child(idRestaurante).child(FILTROS_NO_APROBADOS).get().addOnCompleteListener(task -> {
            if(task.isSuccessful()){
                Map<String, Long> valoresAntiguos = new HashMap<>();
                for (DataSnapshot d: task.getResult().getChildren()) {
                    valoresAntiguos.put(String.valueOf(d.getKey()), (Long) d.getValue());
                }
                //Mezcla ambos mapas y suma el contenido de sus valores
                valoresAntiguos.forEach((k, v) -> valoresNuevos.merge(k, v, Long::sum));
                //Actualiza la BD
                databaseRestaurantes.child(idRestaurante).child(FILTROS_NO_APROBADOS).setValue(valoresNuevos);
                MainActivity.medirTiempo("Añadir filtros", ini, System.nanoTime());
            }
        });
    }

    /**
     * Añade restaurantes favoritos al usuario actual.
     * Los objetos activos actualmente en la app no se modifican.
     * @param restaurantes el id de OpenStreetMap del restaurante
     */
    public void addFavoritosUsuario(List<String> restaurantes){
        databaseUsuarios.child(RESTAURANTES).setValue(restaurantes);
    }

    /**
     * Obtiene las reseñas de un restaurante almacenadas en la BD
     * @param idRestaurante id del resaturante del cual buscar las reseñas
     * @param res lista donde se guaradrá el resultado
     */
    public void getReseniasRestaurante(String idRestaurante, MutableLiveData<List<Resenia>> res){
        long ini = System.nanoTime();
        //Obtiene la lista de ids de las reseñas de ese restaurante
        databaseRestaurantes.child(idRestaurante).child(RESENIAS).get().addOnCompleteListener(task -> {
            if(task.isSuccessful()){
                List<Resenia> resenias = res.getValue();
                for (DataSnapshot d: task.getResult().getChildren()) {
                    //Obtiene los datos de la reseña (busca el id de cada reseña)
                    databaseResenias.child(String.valueOf(d.getValue())).get().addOnCompleteListener(task1 -> {
                        //Añade las reseñas
                        if(task1.isSuccessful()) {
                            resenias.add(parseResenia(task1.getResult()));
                            res.postValue(resenias);
                        }
                    });
                }
                MainActivity.medirTiempo("Get Reseñas restaurante", ini, System.nanoTime());
            }
        });
    }

    /**
     * Actualiza los filtros de un restaurante (añade los nuevos).
     * Esta funcion no deberia llamarse con la lista de filtros obtenidos de OpenStreetMap, sino con una distinta.
     * @param idRestaurante id del restaurante en OpenStreetMap
     * @param actualizable el set donde se guardara el resultado. Hace el post value.
     */
    public void getFiltrosRestaurante(String idRestaurante, MutableLiveData<Set<String>> actualizable){
        databaseRestaurantes.child(idRestaurante).child(FILTROS_NO_APROBADOS).get().addOnCompleteListener(task -> {
           if(task.isSuccessful()){
               Set<String> filtros = actualizable.getValue();
               for (DataSnapshot d: task.getResult().getChildren())
                   filtros.add(String.valueOf(d.getKey()));
               actualizable.postValue(filtros);
           }
        });
    }

    public void getValoracionRestaurante(String idRestaurante, MutableLiveData<Double> actualizable){
        databaseRestaurantes.child(idRestaurante).child(VALORACION).child(VALORACION_TOTAL).get().addOnCompleteListener(task -> {
            if(task.isSuccessful() && task.getResult().getValue() != null) {
                actualizable.postValue(task.getResult().getValue(Double.class));
            }
        });
    }

    //TODO creo que aqui deberia haber hecho que las reseñas no se puedan cambiar de valoración, sino que al añadir o modificar una se cambie la valoracion del restaurante
    public void setValoracionRestaurante(String idRestaurante, double nuevaVal){
        databaseRestaurantes.child(idRestaurante).child(VALORACION).get().addOnCompleteListener(task -> {
            if(task.isSuccessful()) {
                if (task.getResult().getValue() != null) {
                    double antVal = task.getResult().child(VALORACION_TOTAL).getValue(Double.class);
                    double numVal = task.getResult().child(NUM_VALORACIONES).getValue(Double.class);
                    double aux = (antVal * numVal + nuevaVal);
                    numVal++;
                    aux /= (numVal + 1);
                    databaseRestaurantes.child(idRestaurante).child(VALORACION).child(VALORACION_TOTAL).setValue(aux);
                    databaseRestaurantes.child(idRestaurante).child(VALORACION).child(NUM_VALORACIONES).setValue(numVal);
                }
                else {
                    databaseRestaurantes.child(idRestaurante).child(VALORACION).child(VALORACION_TOTAL).setValue(nuevaVal);
                    databaseRestaurantes.child(idRestaurante).child(VALORACION).child(NUM_VALORACIONES).setValue(1);
                }
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
                for (DataSnapshot d: task.getResult().child(RESTAURANTES).getChildren()) {
                    //Añadir los restaurantes
                    new OpenStreetMap().setPlaceById(Usuario.getUsuario().getRestaurantesFavoritos(), String.valueOf(d.getValue()));
                }
            }
        });
    }

    public void cambiarNombreUsuario(String nombre, Activity activity){
        databaseUsuarios.child("usuarioNombre").setValue(nombre).addOnCompleteListener(task -> {
            if(task.isSuccessful()) {
                Usuario.getUsuario().setNombre(nombre);
                Toast.makeText(activity, "Nombre cambiado correctamente.", Toast.LENGTH_SHORT).show();
            }
            else{
                Toast.makeText(activity, "Ha habido un error al cambiar el nombre.", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void cambiarPassword(String password, Activity activity){
        FirebaseAuth.getInstance().getCurrentUser().updatePassword(password).addOnCompleteListener(task -> {
            if(task.isSuccessful())
                Toast.makeText(activity, "Password cambiada correctamente.", Toast.LENGTH_SHORT).show();
            else
                Toast.makeText(activity, "Ha habido un error al cambiar la password", Toast.LENGTH_SHORT).show();
        });
    }

    public void borrarDatos(Activity activity){
        HashMap<String, String> hashMap = new HashMap<>();
        hashMap.put("usuarioId", Usuario.getUsuario().getIdUsuario());
        hashMap.put("usuarioNombre", Usuario.getUsuario().getNombre());
        hashMap.put("usuarioEmail", Usuario.getUsuario().getEmail());
        databaseUsuarios.setValue(hashMap).addOnCompleteListener(task -> {
            if(task.isSuccessful()) {
                Toast.makeText(activity, "Datos borrados correctamente.", Toast.LENGTH_SHORT).show();
                Usuario.getUsuario().borrarDatos();
            }
            else {
                Toast.makeText(activity, "Ha habido un error al borrar los datos.", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void borrarCuenta(Activity activity){
        databaseUsuarios.removeValue();
        FirebaseAuth.getInstance().getCurrentUser().delete().addOnCompleteListener(task -> {
            if(task.isSuccessful()){
                Usuario.cerrarSesion(activity);
            }
        });
    }

    private Resenia parseResenia(DataSnapshot res) {
        return new Resenia(String.valueOf(res.child("idRestaurante").getValue()),
                String.valueOf(res.child("idUsuario").getValue()),
                String.valueOf(res.child("usuarioNombre").getValue()),
                String.valueOf(res.child("titulo").getValue()),
                String.valueOf(res.child("descripcion").getValue()),
                Double.parseDouble(String.valueOf(res.child("valoracion").getValue())));
    }
}
