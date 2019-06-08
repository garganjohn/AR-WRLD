package org.pursuit.ar_wrld;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.pursuit.ar_wrld.database.FirebaseDatabaseHelper;
import org.pursuit.ar_wrld.login.UserHomeScreenActivity;
import org.pursuit.ar_wrld.usermodel.UserInformation;

import java.util.List;

public class ResultPage extends AppCompatActivity {

    private TextView nameTextView;
    private TextView scoreTextView;
    private TextView titleForScore;
    private Button playAgainButton;
    private FirebaseDatabaseHelper firebaseDatabaseHelper;
    private SharedPreferences sharedPreferences;
    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference databaseReference;
    private FirebaseAuth firebaseAuth;
    private String userID;
    private long newScore;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_resultpage);
        sharedPreferences = getApplicationContext().getSharedPreferences(GameInformation.SHARED_PREF_KEY, MODE_PRIVATE);

        nameTextView = findViewById(R.id.player_name);
        titleForScore = findViewById(R.id.title_for_player_score);
        scoreTextView = findViewById(R.id.player_score);
        playAgainButton = findViewById(R.id.playagain_button);

        retrieveUserNameAndScore();


        playAgainButton.setOnClickListener(v -> {
            startActivity(new Intent(ResultPage.this, UserHomeScreenActivity.class));
            finish();
        });

    }

    public void retrieveUserNameAndScore() {
        String playerName = sharedPreferences.getString(GameInformation.USERNAME_KEY, "");
        nameTextView.setText(playerName);

        final long userScore = sharedPreferences.getLong(GameInformation.USER_SCORE_KEY, 0);
        scoreTextView.setText(String.valueOf(userScore));


        firebaseAuth = FirebaseAuth.getInstance();
        FirebaseUser user = firebaseAuth.getCurrentUser();
        userID = user.getUid();
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference("mARtians");

        firebaseDatabaseHelper.updateScore(playerName, userScore);

    }

}

