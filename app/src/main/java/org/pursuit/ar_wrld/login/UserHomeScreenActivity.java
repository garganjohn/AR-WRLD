package org.pursuit.ar_wrld.login;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.jakewharton.rxbinding.view.RxView;

import org.pursuit.ar_wrld.GameInformation;
import org.pursuit.ar_wrld.MainActivity;
import org.pursuit.ar_wrld.R;
import org.pursuit.ar_wrld.SplashActivity;
import org.pursuit.ar_wrld.perks.PerkPickForUser;

import java.util.concurrent.TimeUnit;

import static org.pursuit.ar_wrld.login.SignUpActivity.USERNAME_KEY;

public class UserHomeScreenActivity extends AppCompatActivity {

    private Spinner levelSpinner;
    private Button practiceButton;
    private Button playButton;
    private Button logoutButton;
    private Button pickAPerkButton;
    private TextView usernameTextView;
    private TextView userscoreTextView;
    private TextView userTitleTextView;
    private String userPerkChosen;
    private TextView perkChosen;
    private ImageView perkImage;
    private SharedPreferences sharedPreferences;
    private String userPerkFromSharedPref;
    private RecyclerView recyclerView;
    private String perkChosenSharedPref;

    public static final String EASY_STRING = "EASY";
    public static final String MEDIUM_STRING = "MEDIUM";
    public static final String HARD_STRING = "HARD";
    public static final String BOSS_LEVEL = "BOSS";


    FirebaseAuth firebaseAuth;
    FirebaseAuth.AuthStateListener authStateListener;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_home_screen);
        firebaseAuth = FirebaseAuth.getInstance();
        findViews();
        sharedPreferences = getApplicationContext().getSharedPreferences(GameInformation.SHARED_PREF_KEY, MODE_PRIVATE);
        //TODO issue with perk not being set if user is a first time user
//        if (userPerkFromSharedPref == null) {
//            sharedPreferences.edit().putString(GameInformation.GAME_PERK_KEY, GameInformation.MORE_AMMO_PERK).apply();
//        }
        userPerkFromSharedPref = sharedPreferences.getString(GameInformation.GAME_PERK_KEY, "-1");
        userPerkChosen = getString(R.string.perk_selected_text, userPerkFromSharedPref);


        RxView.clicks(playButton).throttleFirst(2000, TimeUnit.MILLISECONDS).subscribe(empty -> {
            String spinnerValue = levelSpinner.getSelectedItem().toString();
            Intent playIntent = new Intent(UserHomeScreenActivity.this, MainActivity.class);
            sharedPreferences.edit().putString(GameInformation.GAME_DIFFICULTY, spinnerValue).apply();
            startActivity(playIntent);
            finish();

        });

        pickAPerkButton.setOnClickListener(v -> {
            Intent perkIntent = new Intent(UserHomeScreenActivity.this, PerkPickForUser.class);
            startActivity(perkIntent);
            finish();
        });

        RxView.clicks(logoutButton).throttleFirst(2000, TimeUnit.MILLISECONDS).subscribe(empty -> {
            firebaseAuth.signOut();
            startActivity(new Intent(UserHomeScreenActivity.this, SignInActivity.class));
            finish();
        });
        setPerkInfo();
        usernameTextView.setText(retrieveUsername());
        userscoreTextView.setText(String.valueOf(retrieveUserScore()));
    }

    private void findViews() {
        usernameTextView = findViewById(R.id.user_name);
        userscoreTextView = findViewById(R.id.user_score);
        userTitleTextView = findViewById(R.id.user_title);
        recyclerView = findViewById(R.id.top_play_recyclerview);
        levelSpinner = findViewById(R.id.level_spinner);
        pickAPerkButton = findViewById(R.id.pick_a_perk);
        practiceButton = findViewById(R.id.practice_button);
        playButton = findViewById(R.id.play_button);
        logoutButton = findViewById(R.id.logout_button);
        perkChosen = findViewById(R.id.perk_selected);
        perkImage = findViewById(R.id.perk_selected_image);
    }

    private String retrieveUsername() {
        return sharedPreferences.getString(GameInformation.USERNAME_KEY, "");
    }

    private long retrieveUserScore() {
        long longVal = 0;
        try {
             longVal = (long) sharedPreferences.getLong(GameInformation.USER_SCORE_KEY, 0);
        } catch (Exception e) {
            Log.d("MURICA", "retrieveUserScore: " + e.toString());
        }
        return longVal;
    }

    private void setPerkInfo() {
        perkChosen.setText(userPerkChosen);
        perkImage.setImageDrawable(setUserPerk());
    }

    private Drawable setUserPerk() {
        switch (userPerkFromSharedPref) {
            case GameInformation.MORE_AMMO_PERK:
                return getDrawable(R.drawable.ammo_perk);
            case GameInformation.MORE_DAMAGE_PERK:
                return getDrawable(R.drawable.more_damage_perk_image);
            case GameInformation.MORE_TIME_PERK:
                return getDrawable(R.drawable.more_time_perk_image);
            case GameInformation.SLOW_TIME_PERK:
                return getDrawable(R.drawable.slow_time_perk);
            default:
                return getDrawable(R.drawable.noperk_chosen_image);
        }
    }

//    public  void updateUI (FirebaseUser user){
//        if(user != null){
//            String name = user.getDisplayName();
//            usernameTextView.setText(name);
//        }
//    }
}
