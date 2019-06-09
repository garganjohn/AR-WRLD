package org.pursuit.ar_wrld.database;

import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.util.Log;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseException;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.pursuit.ar_wrld.GameInformation;
import org.pursuit.ar_wrld.login.UserHomeScreenActivity;
import org.pursuit.ar_wrld.recyclerview.TopScoreViewHolder;
import org.pursuit.ar_wrld.usermodel.UserInformation;

import java.util.ArrayList;
import java.util.List;


public class FirebaseDatabaseHelper {

    private static final String TAG = "DB";
    private SharedPreferences sharedPreferences;
    private long playerScore;
    private FirebaseDatabase database;
    private DatabaseReference myRef;
    private List<UserInformation> userInformationList;
    private UserHomeScreenActivity userHomeScreenActivity;
    FirebaseRecyclerOptions<UserInformation> userOptions;
    FirebaseRecyclerAdapter<UserInformation, TopScoreViewHolder> adapter;


    public interface DataStatus {
        void dataIsLoaded(List<UserInformation> userInformations, List<String> keys);

        void dataIsInserted();

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
        String key = myRef.push().getKey();
//        String gameNumber = "Game Number";
//        myRef.child(key).child(gameNumber).setValue(userInformation)
        myRef.child(key).setValue(userInformation)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        dataStatus.dataIsInserted();
                        Log.e("newUserCreated: ", key);
                    }
                });

    }

    public void getKey(){

    }


    public void updateScore(String name, long score) {

        myRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                long newScore = 0;
                try {
                    long previousScore = dataSnapshot.getValue(Long.class);
                    myRef.child(name).setValue(previousScore);
                } catch (NullPointerException npe) {
                    long previousScore = 0;
                    newScore = score + previousScore;
                    myRef.child(name).setValue(newScore);
                } catch (DatabaseException dbe){
                    myRef.child(name).setValue(newScore);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }


}
