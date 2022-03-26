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
import java.util.HashSet;
import java.util.List;

import ucm.appmenus.entities.Resenia;
import ucm.appmenus.entities.Restaurante;
import ucm.appmenus.entities.Usuario;

//https://firebase.google.com/docs/database/android/read-and-write#java_5
public class BaseDatos {

    private static BaseDatos instance;
    private final FirebaseAuth firebaseAuth;
    private final DatabaseReference databaseUsuarios, databaseResenias, databaseRestaurantes;
    private String userId;

    public static BaseDatos getInstance(){
        if(instance == null)
            instance = new BaseDatos();
        return instance;
    }

    private BaseDatos(){
        firebaseAuth = FirebaseAuth.getInstance();
        FirebaseUser rUser = firebaseAuth.getCurrentUser();
        String userId = rUser.getUid();
        databaseUsuarios = FirebaseDatabase.getInstance().getReference("Users").child(userId);
        databaseResenias = FirebaseDatabase.getInstance().getReference("Resenias").child(userId);
        databaseRestaurantes = FirebaseDatabase.getInstance().getReference("Restaurantes").child(userId);
    }

    /**
     * Añade una reseña a la Base de Datos. Se añade una nueva entrada en Reseñas y se añade su id en el
     * restaurante al cual pertenece y en el usaurio que la creó.
     * @param resenia la reseña a añadir
     */
    public void addResenia(Resenia resenia){
        List<String> reseniasusuario = Usuario.getUsuario().getReseniasId();
        reseniasusuario.add(resenia.getReseniaID());
        databaseResenias.push().setValue(resenia);
        databaseRestaurantes.child("resenias").setValue("eedfdsfs");
        databaseUsuarios.child("resenias").setValue(reseniasusuario);
    }

    public List<Resenia> getResenias(String restauranteID){
        List<Resenia> resenias = new ArrayList<>();
        databaseRestaurantes.child(restauranteID).get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                if(task.isSuccessful()){
                    //TODO: obtener las reseñas idk
                }
            }
        });
        return resenias;
    }

    public void addFiltrosRestaurante(String restauranteID, List<String> filtros){
        databaseRestaurantes.child(restauranteID).setValue(filtros);
    }

    public void addFavoritosUsuario(List<String> restaurantes){
        databaseUsuarios.child("favoritos").setValue(restaurantes);
    }

    public void getDatosusuario(){
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
