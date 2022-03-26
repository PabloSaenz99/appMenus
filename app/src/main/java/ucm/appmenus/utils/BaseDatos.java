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

//https://firebase.google.com/docs/database/android/read-and-write#java_5
public class BaseDatos {

    private static BaseDatos instance;
    private final FirebaseAuth firebaseAuth;
    private final DatabaseReference databaseReference;
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
        databaseReference = FirebaseDatabase.getInstance().getReference("Users").child(userId);
    }

    public void addResenia(Resenia resenia){
        databaseReference.child("Resenias").setValue(resenia);
    }

    public List<Resenia> getResenias(String restauranteID){
        List<Resenia> resenias = new ArrayList<>();
        databaseReference.child("restaurantes").child(restauranteID).get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
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
        databaseReference.child("restaurantes").child(restauranteID).setValue(filtros);
    }

    public void addFavoritosUsuario(List<String> restaurantes){
        databaseReference.child("users").child(userId).child("favoritos").setValue(restaurantes);
    }

    public void getDatosusuario(){
        databaseReference.child("users").child(userId).get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
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
