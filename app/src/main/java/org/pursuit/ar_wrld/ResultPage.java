package org.pursuit.ar_wrld;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

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
    private String dbKey;


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

        moveBackHome();


    }

    public void moveBackHome(){
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

//        firebaseAuth = FirebaseAuth.getInstance();
//        FirebaseUser user = firebaseAuth.getCurrentUser();
//        userID = user.getUid();
//        Log.d("user", "USER" + userID);
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference("mARtians");
        DatabaseReference currentRef = databaseReference.child(playerName);

        Log.d("DB", "db" + databaseReference);

        currentRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String keys = "";
                for (DataSnapshot datas : dataSnapshot.getChildren()) {
                    keys = datas.getKey();
                }
                    if (dataSnapshot.child(keys).exists()) {

                        Log.d("FINDME", "chekcing user" + dataSnapshot.child(playerName));

//                    Log.d("FINDME", "onDataChange: " + dataSnapshot.getValue().toString());
//                    dataSnapshot.child(playerName).child("game 1").getValue();
//                    Log.d("FINDME", "getting game number" + dataSnapshot.child(playerName).child("game 1").getValue());
//                    currentRef.child("game 1").setValue(100);
                        currentRef.child(keys).setValue(userScore);
                        // databaseReference.child("score").setValue(userScore);
                    } else {
                        UserInformation userInformation = new UserInformation();
                        userInformation.setUsername(playerName);
                        userInformation.setUserscore(userScore);

                        new FirebaseDatabaseHelper().addUser(userInformation, new FirebaseDatabaseHelper.DataStatus() {
                            @Override
                            public void dataIsLoaded(List<UserInformation> userInformations, List<String> keys) {

                            }

                            @Override
                            public void dataIsInserted() {
                                Toast.makeText(ResultPage.this, "Score is saved successfully!", Toast.LENGTH_SHORT).show();

                            }

                        });

                    }
                }


//                long previousScore = dataSnapshot.getValue(Long.class);
//                long newScore = userScore + previousScore;
//                databaseReference.child(playerName).setValue(newScore);
//                Log.e("Key from db", "previous " + previousScore);
//                Log.e("Key from db", "current " + userScore);
//                Log.e("Key from db", "updated " + newScore);

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

}

