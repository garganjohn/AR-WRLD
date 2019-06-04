package org.pursuit.ar_wrld.database;

import android.content.SharedPreferences;
import android.os.Bundle;
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
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.pursuit.ar_wrld.GameInformation;
import org.pursuit.ar_wrld.R;
import org.pursuit.ar_wrld.recyclerview.TopScoreViewHolder;
import org.pursuit.ar_wrld.usermodel.UserInformation;

import java.util.ArrayList;
import java.util.List;

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
    FirebaseRecyclerOptions<UserInformation> userOptions;
    FirebaseRecyclerAdapter<UserInformation, TopScoreViewHolder> adapter;

    public interface DataStatus{
        void dataIsLoaded(List<UserInformation> userInformations);
    }

    public FirebaseDatabaseHelper(){
        database = FirebaseDatabase.getInstance();
        myRef = database.getReference("mARtians");
    }



//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        firebaseAuth = FirebaseAuth.getInstance();
//        user = firebaseAuth.getCurrentUser();
//        database = FirebaseDatabase.getInstance();
//        myRef = database.getReference();
//
//        sharedPreferences = getApplicationContext().getSharedPreferences(GameInformation.SHARED_PREF_KEY, MODE_PRIVATE);
//
//        Log.e("Checking if name and score are here", "khaing" + name + playerScore);
//
//        savePlayerInfo();
//        displayScore();
//
//
//    }

    public void savePlayerInfo() {

        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                userInformationList.clear();
                List<String> keys = new ArrayList<>();
                for(DataSnapshot keyNode : dataSnapshot.getChildren()){
                    keys.add(keyNode.getKey());
                    UserInformation userInformation = keyNode.getValue(UserInformation.class);
                    userInformationList.add(userInformation);
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

//        name = sharedPreferences.getString(GameInformation.USERNAME_KEY, "");
//        playerScore = sharedPreferences.getInt(GameInformation.USER_SCORE_KEY, -1);
//
//        if(!TextUtils.isEmpty(name) && !TextUtils.isEmpty(String.valueOf(playerScore))) {
//
//            String aName = myRef.push().getKey();
//
//            UserInformation userInformation = new UserInformation(this.name, playerScore, playerTitle);
//
//            myRef.child(aName).setValue(userInformation);
//        }else {
//            //Toast.makeText(this, "Please enter a name!", Toast.LENGTH_SHORT).show();
//        }
//
//        adapter.notifyDataSetChanged();

    }


    public void displayScore() {
//        userOptions = new FirebaseRecyclerOptions.Builder<UserInformation>()
//                .setQuery(myRef, UserInformation.class)
//                .build();
//
//        adapter =
//                new FirebaseRecyclerAdapter<UserInformation, TopScoreViewHolder>(userOptions) {
//                    @Override
//                    protected void onBindViewHolder(@NonNull TopScoreViewHolder holder, long position, @NonNull UserInformation model) {
//                        holder.selectedUsername.setText(model.getUsername());
//                        holder.selectedUserTitle.setText(model.getUsertitle());
//                        holder.selectedUserScore.setText(model.getUserscore());
//                    }
//
//                    @NonNull
//                    @Override
//                    public TopScoreViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
//                        return new TopScoreViewHolder(LayoutInflater.from(getBaseContext()).inflate(R.layout.activity_itemviews, viewGroup, false));
//                    }
//                };
//        adapter.startListening();
//        recyclerView.setAdapter(adapter);

    }
}
