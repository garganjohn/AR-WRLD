package org.pursuit.ar_wrld.database;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.pursuit.ar_wrld.GameInformation;
import org.pursuit.ar_wrld.R;
import org.pursuit.ar_wrld.ResultPage;
import org.pursuit.ar_wrld.login.UserHomeScreenActivity;
import org.pursuit.ar_wrld.recyclerview.TopScoreViewHolder;
import org.pursuit.ar_wrld.usermodel.UserInformation;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static android.content.Context.MODE_PRIVATE;

public class FirebaseDatabaseHelper {

    private static final String TAG = "DB";
    private SharedPreferences sharedPreferences;
    private String name;
    private long playerScore;
    private String playerTitle;
    private RecyclerView recyclerView;
    private FirebaseAuth firebaseAuth;
    private FirebaseUser user;
    private FirebaseDatabase database;
    private DatabaseReference myRef;
    private List<UserInformation> userInformationList;
    private UserHomeScreenActivity userHomeScreenActivity;
    FirebaseRecyclerOptions<UserInformation> userOptions;
    FirebaseRecyclerAdapter<UserInformation, TopScoreViewHolder> adapter;


    public interface DataStatus {
        void dataIsLoaded(List<UserInformation> userInformations, List<String> keys);

        void dataIsInserted();

        void dataIsUpdated();
    }

    public FirebaseDatabaseHelper() {
        database = FirebaseDatabase.getInstance();
        myRef = database.getReference("mARtians");
    }


    public void savePlayerInfo(final DataStatus dataStatus) {

        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                userInformationList.clear();
                List<String> keys = new ArrayList<>();
                for (DataSnapshot keyNode : dataSnapshot.getChildren()) {
                    keys.add(keyNode.getKey());
                    UserInformation userInformation = keyNode.getValue(UserInformation.class);
                    userInformationList.add(userInformation);
                }
                dataStatus.dataIsLoaded(userInformationList, keys);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    public void addUser(UserInformation userInformation, final DataStatus dataStatus) {
        String key = myRef.child(sharedPreferences.getString(GameInformation.USERNAME_KEY, "")).toString();
        myRef.child(key).setValue(userInformation)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        dataStatus.dataIsInserted();
                        Log.e("newUserCreated: ", key);
                    }
                });

    }

    public long displayScore() {
        long score = 0;

        myRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                long score = dataSnapshot.getValue(Long.class);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        return score;
    }

    public void updateScore(String name, long score) {

        myRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                long previousScore = dataSnapshot.getValue(Long.class);
                long newScore = score + previousScore;
                myRef.child(name).setValue(newScore);
            }


            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }



}
