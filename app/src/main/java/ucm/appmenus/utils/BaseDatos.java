package ucm.appmenus.utils;

import android.util.Log;

import androidx.annotation.NonNull;

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
import java.util.List;

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
        getReseniasRestaurante(resenia.getIdRestaurante());
    }

    /**
     * Añade filtros a un restaurante.
     * Los objetos activos actualmente en la app no se modifican.
     * @param idRestaurante el id de OpenStreetMap del restaurante
     * @param filtros los filtros nuevos
     */
    public void addFiltrosRestaurante(String idRestaurante, List<String> filtros){
        //TODO: obtener los datos de los restaurantes de la BD y luego actualizarlos con los filtros nuevos
        databaseRestaurantes.child(idRestaurante).child(FILTROS_NO_APROBADOS).setValue(filtros);
    }

    /**
     * Añade restaurantes favoritos al usuario actual.
     * Los objetos activos actualmente en la app no se modifican.
     * @param restaurantes el id de OpenStreetMap del restaurante
     */
    public void addFavoritosUsuario(List<String> restaurantes){
        databaseUsuarios.child(FAVORITOS).setValue(restaurantes);
    }

    public List<Resenia> getReseniasRestaurante(String idRestaurante){
        List<Resenia> resenias = new ArrayList<>();
        databaseRestaurantes.child(idRestaurante).child(RESENIAS).get().addOnCompleteListener(task -> {
            if(task.isSuccessful()){
                Log.i("res", "entro");
                for (DataSnapshot d: task.getResult().getChildren()) {
                    databaseResenias.child(String.valueOf(d.getValue())).get().addOnCompleteListener(task1 -> {
                        DataSnapshot res = task1.getResult();
                        resenias.add(new Resenia(String.valueOf(res.child("idRestaurante").getValue()),
                                String.valueOf(res.child("idUsuario").getValue()),
                                String.valueOf(res.child("usuarioNombre").getValue()),
                                String.valueOf(res.child("titulo").getValue()),
                                String.valueOf(res.child("texto").getValue()),
                                Double.valueOf(String.valueOf(res.child("valoracion").getValue()))));
                    });
                }
            }
            else
                Log.i("res","error");
        });
        return resenias;
    }

    public void getFiltrosRestaurante(String idRestaurante){
        //TODO: obtener los datos de los filtros
    }

    public void getDatosUsuario(){
        databaseUsuarios.get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                if (!task.isSuccessful()) {
                    Log.e("firebase", "Error getting data", task.getException());
                }
                else {
                    Log.d("firebase", String.valueOf(task.getResult().getValue()));
                }
            }
        });
    }
}
