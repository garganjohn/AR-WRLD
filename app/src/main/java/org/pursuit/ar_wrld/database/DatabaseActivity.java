package org.pursuit.ar_wrld.database;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.FirebaseFirestore;

import org.pursuit.ar_wrld.login.UserHomeScreenActivity;

import java.util.HashMap;
import java.util.Map;

public class DatabaseActivity extends AppCompatActivity {

    private static final String TAG = "DB";
    private static final String USER_NAME = "username";
    private static final String USER_SCORE = "userscore";

    private FirebaseFirestore db = FirebaseFirestore.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        saveScore();


    }

    public void saveScore(){

        //get username from userhomescreenactivity
        //get userscore from argame play

        String name = "";
        int score = 0;

        Map<String, Object> note = new HashMap<>();
        note.put(USER_NAME, name);
        note.put(USER_SCORE, score);

        db.collection("mARtians").document("Practice Score").set(note)
                .addOnSuccessListener(aVoid -> Toast.makeText(DatabaseActivity.this, "Note saved", Toast.LENGTH_SHORT).show())
                .addOnFailureListener(e -> {
                    Toast.makeText(DatabaseActivity.this, "Error!", Toast.LENGTH_SHORT).show();
                    Log.d(TAG, e.toString());
                });

    }
}
