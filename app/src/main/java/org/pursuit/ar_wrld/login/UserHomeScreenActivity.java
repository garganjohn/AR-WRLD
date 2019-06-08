package org.pursuit.ar_wrld.login;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.jakewharton.rxbinding.view.RxView;

import org.pursuit.ar_wrld.GameInformation;
import org.pursuit.ar_wrld.MainActivity;
import org.pursuit.ar_wrld.R;
import org.pursuit.ar_wrld.SplashActivity;
import org.pursuit.ar_wrld.database.FirebaseDatabaseHelper;
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
    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference databaseReference;
    private long updatedScore;
    private FirebaseDatabaseHelper firebaseDatabaseHelper;

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
        userPerkFromSharedPref = sharedPreferences.getString(GameInformation.GAME_PERK_KEY, "Choose a perk");
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
        String userScoreText = getString(R.string.user_score, retrieveUserScore());
        userscoreTextView.setText(userScoreText);
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
        changeStatusBarColor();
    }
    private void changeStatusBarColor() {
        // getWindow().setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS); //Makes both status and navbar transparent
        getWindow().setNavigationBarColor(ContextCompat.getColor(this, R.color.purple_app_color)); // Navigation bar the soft bottom of some phones like nexus and some Samsung note series
        getWindow().setStatusBarColor(ContextCompat.getColor(this, R.color.purple_app_color)); //status bar or the time bar at the top
    }

    public String retrieveUsername() {
        return sharedPreferences.getString(GameInformation.USERNAME_KEY, "");
    }

    private long retrieveUserScore() {
        try {
            firebaseDatabase = FirebaseDatabase.getInstance();
            databaseReference = firebaseDatabase.getReference("mARtians");
            String playName = retrieveUsername();
            DatabaseReference updatedRef = databaseReference.child(playName);
            updatedRef.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    long updatedScore = dataSnapshot.getValue(Long.class);
                    userscoreTextView.setText(String.valueOf(updatedScore));

                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
        } catch (Exception e) {
            Log.d("MURICA", "retrieveUserScore: " + e.toString());
        }
        return updatedScore;
    }



    private void setPerkInfo() {
        perkImage.setImageDrawable(setUserPerk());
    }

    private Drawable setUserPerk() {
        switch (userPerkFromSharedPref) {
            case GameInformation.MORE_AMMO_PERK:
                setUserPerkText(getString(R.string.more_ammo_perk_name));
                return getDrawable(R.drawable.ammo_perk);
            case GameInformation.MORE_DAMAGE_PERK:
                setUserPerkText(getString(R.string.more_damage_perk_name));
                return getDrawable(R.drawable.more_damage_perk_image);
            case GameInformation.MORE_TIME_PERK:
                setUserPerkText(getString(R.string.more_time_perk_name));
                return getDrawable(R.drawable.more_time_perk_image);
            case GameInformation.SLOW_TIME_PERK:
                setUserPerkText(getString(R.string.slow_time_perk_name));
                return getDrawable(R.drawable.slow_time_perk);
            default:
                perkChosen.setText(getString(R.string.no_perk_text));
                return getDrawable(R.drawable.noperk_chosen_image);
        }
    }

    private void setUserPerkText(String perk){
        perkChosen.setText(getString(R.string.perk_selected_text, perk));
    }

}
