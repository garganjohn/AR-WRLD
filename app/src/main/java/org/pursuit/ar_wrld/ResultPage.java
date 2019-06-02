package org.pursuit.ar_wrld;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import org.pursuit.ar_wrld.login.SignInActivity;
import org.pursuit.ar_wrld.login.SignUpActivity;
import org.pursuit.ar_wrld.login.UserHomeScreenActivity;

import static org.pursuit.ar_wrld.login.SignUpActivity.USERNAME_KEY;

public class ResultPage extends AppCompatActivity {

    private TextView nameTextView;
    private TextView scoreTextView;
    private TextView titleForScore;
    private Button playAgainButton;
    FirebaseAuth firebaseAuth;
    FirebaseAuth.AuthStateListener authStateListener;
    FirebaseUser user;
    private SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_resultpage);

        nameTextView = findViewById(R.id.player_name);
        titleForScore = findViewById(R.id.title_for_player_score);
        scoreTextView = findViewById(R.id.player_score);
        playAgainButton = findViewById(R.id.playagain_button);

        retrieveUserScore();

        playAgainButton.setOnClickListener(v -> {
            startActivity(new Intent(ResultPage.this, UserHomeScreenActivity.class));
            finish();
        });

    }

    private void retrieveUsername() {
        sharedPreferences = getSharedPreferences(SignUpActivity.MYSHAREDPREF, Context.MODE_PRIVATE);
        if (sharedPreferences.contains(USERNAME_KEY)) {
            nameTextView.setText(sharedPreferences.getString(USERNAME_KEY, ""));
        }
    }

    public void retrieveUserScore() {
        sharedPreferences = getApplicationContext().getSharedPreferences(GameInformation.SHARED_PREF_KEY, MODE_PRIVATE);
        int userScore = sharedPreferences.getInt(GameInformation.USER_SCORE_KEY, -1);
        scoreTextView.setText(String.valueOf(userScore));

    }

}

