package ucm.appmenus.login;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import ucm.appmenus.R;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Toolbar;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.jetbrains.annotations.NotNull;

public class StartActivity extends AppCompatActivity {
private TextView userName;
private FirebaseUser firebaseUser;
private FirebaseAuth firebaseAuth;
private DatabaseReference databaseReference;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);
        Toolbar toolbar= findViewById(R.id.toolbar);

        getSupportActionBar().setTitle("");
    userName= findViewById(R.id.username);


    firebaseAuth= firebaseAuth.getInstance();
    firebaseUser=firebaseAuth.getCurrentUser();
databaseReference = FirebaseDatabase.getInstance().getReference("Users").child(firebaseUser.getUid());
databaseReference.addValueEventListener(new ValueEventListener() {
    @Override
    public void onDataChange(@NonNull @NotNull DataSnapshot dataSnapshot) {
UserData  userData=dataSnapshot.getValue(UserData.class);
assert userData != null;
userName.setText(userData.getUserName());

    }

    @Override
    public void onCancelled(@NonNull @NotNull DatabaseError databaseError) {
        Toast.makeText(StartActivity.this,databaseError.getMessage(),Toast.LENGTH_SHORT).show();
    }
});
    }

}