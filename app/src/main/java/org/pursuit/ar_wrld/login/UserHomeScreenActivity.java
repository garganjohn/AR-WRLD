package org.pursuit.ar_wrld.login;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.jakewharton.rxbinding.view.RxView;

import org.pursuit.ar_wrld.GameInformation;
import org.pursuit.ar_wrld.MainActivity;
import org.pursuit.ar_wrld.R;
import org.pursuit.ar_wrld.TutorialActivity;
import org.pursuit.ar_wrld.TutorialFragment;
import org.pursuit.ar_wrld.database.FirebaseDatabaseHelper;
import org.pursuit.ar_wrld.perks.PerkPickForUser;
import org.pursuit.ar_wrld.usermodel.UserInformation;

import java.util.List;
import java.util.concurrent.TimeUnit;

import static java.lang.Long.parseLong;
import static java.lang.Long.valueOf;

public class UserHomeScreenActivity extends AppCompatActivity {
    private static final int PERMISSION_REQUEST_CODE = 200;

    private Spinner levelSpinner;
    private Button tutorialButton;
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
    private String nameShown;
    private UserInformation userInformation;
    private TutorialFragment tutorialFragment;

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
        if (checkPermission()) {
            //main logic or main code

            // . write your main code to execute, It will execute if the permission is already given.
        } else {
            requestPermission();
        }


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

        tutorialButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), TutorialActivity.class);
                startActivity(intent);

            }
        });

        RxView.clicks(logoutButton).throttleFirst(2000, TimeUnit.MILLISECONDS).subscribe(empty -> {
            firebaseAuth.signOut();
            startActivity(new Intent(UserHomeScreenActivity.this, SignInActivity.class));
            finish();
        });
        setPerkInfo();
        usernameTextView.setText(retrieveUsername());
        String userScoreText = getString(R.string.user_score, retrieveScore());
        userscoreTextView.setText(userScoreText);
    }

    private void findViews() {
        usernameTextView = findViewById(R.id.user_name);
        userscoreTextView = findViewById(R.id.user_score);
//        userTitleTextView = findViewById(R.id.user_title);
        levelSpinner = findViewById(R.id.level_spinner);
        pickAPerkButton = findViewById(R.id.pick_a_perk);
        tutorialButton = findViewById(R.id.practice_button);
        playButton = findViewById(R.id.play_button);
        logoutButton = findViewById(R.id.logout_button);
        perkChosen = findViewById(R.id.perk_selected);
        perkImage = findViewById(R.id.perk_selected_image);

        levelSpinner.getBackground().setTint(getColor(R.color.silver_app_color));
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

    private long retrieveScore() {

        final long updatedScore = sharedPreferences.getLong(GameInformation.USER_SCORE_KEY, 0);

        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference("mARtians");
        DatabaseReference currentRef = databaseReference.child(retrieveUsername());

        Log.d("DB", "db" + databaseReference);


        currentRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String keys = "";
                for (DataSnapshot datas : dataSnapshot.getChildren()) {
                    keys = datas.getKey();
                    Log.d("FINDME", "onUserHome: " + updatedScore);
                }
                if (dataSnapshot.child(keys).exists()) {

                    Log.d("FINDME", "chekcing userHome" + dataSnapshot.child(retrieveUsername()).getChildren());

//                    currentRef.child(keys).setValue(updatedScore);
                    String userScore = getString(R.string.user_score, updatedScore);
                    userscoreTextView.setText(userScore);
                } else {
                    UserInformation userInformation = new UserInformation();
                    userInformation.setUserscore(0);

                    currentRef.child(keys).setValue(userInformation);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        return updatedScore;
    }

//    private long retrieveUserScore() {
//
//        try {
//            firebaseDatabase = FirebaseDatabase.getInstance();
//            databaseReference = firebaseDatabase.getReference("mARtians");
//            String playName = retrieveUsername();
//            DatabaseReference updatedRef = databaseReference.child(playName);
//            Log.d("USERHOMESCREEN", "getting the child node" + updatedRef.toString());
//            databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
//                @Override
//                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                    for(DataSnapshot datas: dataSnapshot.getChildren()){
//                        String keys=datas.getKey();
//                        if(dataSnapshot.child(keys).exists()){
//
//                            //long gettingScore = userInformation.getUserscore();
//                            //updatedScore = dataSnapshot.child(keys).child("userscore").getValue();
//                            Log.d("FINDME", "userkey" + keys);
//
//                        } else{
//                            UserInformation userInformation = new UserInformation();
////                            userInformation.setUsername(playName);
//                            userInformation.setUserscore(0);
//                            // databaseReference.child(playName).child("score").setValue(0);
//
//                            new FirebaseDatabaseHelper().addUser(userInformation, new FirebaseDatabaseHelper.DataStatus() {
//                                @Override
//                                public void dataIsLoaded(List<UserInformation> userInformations, List<String> keys) {
//
//                                }
//
//                                @Override
//                                public void dataIsInserted() {
//
//                                }
//                            });
//                        }
//                        userscoreTextView.setText(String.valueOf(updatedScore));
//                    }
//
//
//                }
//
//                @Override
//                public void onCancelled(@NonNull DatabaseError databaseError) {
//
//                }
//            });
//        } catch (Exception e) {
//            Log.d("MURICA", "retrieveUserScore: " + e.toString());
//        }
//        return updatedScore;


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
            case GameInformation.MORE_CLOCKS:
                setUserPerkText(getString(R.string.more_clocks_perk_name));
                return getDrawable(R.drawable.slow_time_perk);
            default:
                perkChosen.setText(getString(R.string.no_perk_text));
                return getDrawable(R.drawable.noperk_chosen_image);
        }
    }

    private void setUserPerkText(String perk) {
        perkChosen.setText(getString(R.string.perk_selected_text, perk));
    }

    private boolean checkPermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA)
                != PackageManager.PERMISSION_GRANTED) {
            // Permission is not granted
            return false;
        }
        return true;
    }

    private void requestPermission() {

        ActivityCompat.requestPermissions(this,
                new String[]{Manifest.permission.CAMERA},
                PERMISSION_REQUEST_CODE);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case PERMISSION_REQUEST_CODE:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(getApplicationContext(), "Permission Granted", Toast.LENGTH_SHORT).show();

                    // main logic
                } else {
                    Toast.makeText(getApplicationContext(), "Permission Denied", Toast.LENGTH_SHORT).show();
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA)
                                != PackageManager.PERMISSION_GRANTED) {
                            showMessageOKCancel("You need to allow access permissions",
                                    new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                                                requestPermission();
                                            }
                                        }
                                    });
                        }
                    }
                }
                break;
        }
    }

    private void showMessageOKCancel(String message, DialogInterface.OnClickListener okListener) {
        new AlertDialog.Builder(this)
                .setMessage(message)
                .setPositiveButton("OK", okListener)
                .setNegativeButton("Cancel", null)
                .create()
                .show();
    }

    @Override
    public void onBackPressed() {
        this.finish();
        super.onBackPressed();
    }
}
