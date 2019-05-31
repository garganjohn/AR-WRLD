package org.pursuit.ar_wrld.database;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.pursuit.ar_wrld.GameInformation;
import org.pursuit.ar_wrld.login.SignUpActivity;
import org.pursuit.ar_wrld.login.UserHomeScreenActivity;

import java.util.HashMap;
import java.util.Map;

import static org.pursuit.ar_wrld.login.SignUpActivity.USERNAME_KEY;

public class DatabaseActivity extends AppCompatActivity {

    private static final String TAG = "DB";
    private SharedPreferences sharedPreferences;
    private String playerName;
    private int playerScore;

    FirebaseAuth firebaseAuth;
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference myRef = database.getReference("mARtians");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        sharedPreferences = getSharedPreferences(GameInformation.SHARED_PREF_KEY, MODE_PRIVATE);
        sharedPreferences = getSharedPreferences(SignUpActivity.MYSHAREDPREF, MODE_PRIVATE);

        if(sharedPreferences.contains(USERNAME_KEY)){
            playerName = sharedPreferences.getString(USERNAME_KEY, "");
        }
        if(sharedPreferences.contains(GameInformation.USER_SCORE_KEY)){
            playerScore = sharedPreferences.getInt(GameInformation.USER_SCORE_KEY, 0);
        }


//        FirebaseDatabase.getInstance().getReference("mARtians")
//                .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
//                .setValue("User").addOnCompleteListener(new OnCompleteListener<Void>() {
//            @Override
//            public void onComplete(@NonNull Task<Void> task) {
//
//            }
//        });
        //        sharedPreferences = getSharedPreferences(SignUpActivity.MYSHAREDPREF, Context.MODE_PRIVATE);

    }



    public void saveScore(){

        //get username from userhomescreenactivity
        //get userscore from argame play

//        String name = "";
//        String score = "";
//
//        Map<String, Object> note = new HashMap<>();
//        note.put(USER_NAME, name);
//        note.put(USER_SCORE, score);


    }

    public void loadScore(){
    }
}
