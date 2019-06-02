package org.pursuit.ar_wrld.database;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.pursuit.ar_wrld.R;
import org.pursuit.ar_wrld.recyclerview.TopScoreViewHolder;
import org.pursuit.ar_wrld.usermodel.UserInformation;

public class DatabaseActivity extends AppCompatActivity {

    private static final String TAG = "DB";
    private SharedPreferences sharedPreferences;
    private String playerName;
    private int playerScore;
    private String playerTitle;
    private RecyclerView recyclerView;
    FirebaseRecyclerOptions<UserInformation> userOptions;
    FirebaseRecyclerAdapter<UserInformation, TopScoreViewHolder> adapter;

    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference myRef = database.getReference("mARtians");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        sharedPreferences = getSharedPreferences(GameInformation.SHARED_PREF_KEY, MODE_PRIVATE);
//        sharedPreferences = getSharedPreferences(SignUpActivity.MYSHAREDPREF, MODE_PRIVATE);
//        sharedPreferences = getSharedPreferences(UserTitleInformation.TITLE_SHAREDPREF_KEY, MODE_PRIVATE);

        savePlayerInfo();
        displayScore();


    }

    public void savePlayerInfo() {

//        if (sharedPreferences.contains(USERNAME_KEY)) {
//            playerName = sharedPreferences.getString(USERNAME_KEY, "");
//        }
//        if (sharedPreferences.contains(GameInformation.USER_SCORE_KEY)) {
//            playerScore = sharedPreferences.getInt(GameInformation.USER_SCORE_KEY, 0);
//        }
//        if (sharedPreferences.contains(UserTitleInformation.TITLE_SHAREDPREF_KEY)) {
//            playerTitle = sharedPreferences.getString(UserTitleInformation.DOPE, "");
//        }

        UserInformation userInformation = new UserInformation(playerName, playerScore, playerTitle);

        myRef.push()
                .setValue(userInformation);

        adapter.notifyDataSetChanged();


    }

    public void displayScore() {
        userOptions = new FirebaseRecyclerOptions.Builder<UserInformation>()
                .setQuery(myRef, UserInformation.class)
                .build();

        adapter =
                new FirebaseRecyclerAdapter<UserInformation, TopScoreViewHolder>(userOptions) {
                    @Override
                    protected void onBindViewHolder(@NonNull TopScoreViewHolder holder, int position, @NonNull UserInformation model) {
                        holder.selectedUsername.setText(model.getUsername());
                        holder.selectedUserTitle.setText(model.getUsertitle());
                        holder.selectedUserScore.setText(model.getUserscore());
                    }

                    @NonNull
                    @Override
                    public TopScoreViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
                        return new TopScoreViewHolder(LayoutInflater.from(getBaseContext()).inflate(R.layout.activity_itemviews, viewGroup, false));
                    }
                };
        adapter.startListening();
        recyclerView.setAdapter(adapter);

    }
}
