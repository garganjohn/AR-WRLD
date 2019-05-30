package org.pursuit.ar_wrld.database;

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

import org.pursuit.ar_wrld.login.UserHomeScreenActivity;

import java.util.HashMap;
import java.util.Map;

public class DatabaseActivity extends AppCompatActivity {

    private static final String TAG = "DB";
    private static final String USER_NAME = "username";
    private static final String USER_SCORE = "userscore";

    FirebaseAuth firebaseAuth;
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference myRef = database.getReference("mARtians");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        saveScore();

//        FirebaseDatabase.getInstance().getReference("mARtians")
//                .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
//                .setValue("User").addOnCompleteListener(new OnCompleteListener<Void>() {
//            @Override
//            public void onComplete(@NonNull Task<Void> task) {
//
//            }
//        });

    }



    public void saveScore(){

        //get username from userhomescreenactivity
        //get userscore from argame play

        String name = "";
        String score = "";

        Map<String, Object> note = new HashMap<>();
        note.put(USER_NAME, name);
        note.put(USER_SCORE, score);


    }

    public void loadScore(){
    }
}
