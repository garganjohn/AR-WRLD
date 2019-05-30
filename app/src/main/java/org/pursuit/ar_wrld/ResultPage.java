package org.pursuit.ar_wrld;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class ResultPage extends AppCompatActivity {

    private TextView nameTextView;
    private TextView scoreTextView;
    FirebaseAuth firebaseAuth;
    FirebaseAuth.AuthStateListener authStateListener;
    FirebaseUser user;

    @Override
    protected void onStart() {
        super.onStart();
        firebaseAuth.addAuthStateListener(authStateListener);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_resultpage);

        firebaseAuth = FirebaseAuth.getInstance();

        nameTextView = findViewById(R.id.player_name);
        scoreTextView = findViewById(R.id.player_score);


        if(firebaseAuth.getCurrentUser() != null ){
            user = firebaseAuth.getCurrentUser();
            updateUI(user);
        }

    }

    public  void updateUI (FirebaseUser user){
        if(user != null){
            String name = user.getDisplayName();
            nameTextView.setText(name);
        }
    }
}
