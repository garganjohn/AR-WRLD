package org.pursuit.ar_wrld;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.pursuit.ar_wrld.Effects.MyBounceInterpolator;
import org.pursuit.ar_wrld.database.FirebaseDatabaseHelper;
import org.pursuit.ar_wrld.login.UserHomeScreenActivity;
import org.pursuit.ar_wrld.usermodel.UserInformation;

import java.util.List;

public class ResultPage extends AppCompatActivity {

    private TextView nameTextView;
    private TextView scoreTextView;
    private TextView titleForScore;
    private TextView msgTextView;
    private Button playAgainButton;
    private FirebaseDatabaseHelper firebaseDatabaseHelper;
    private SharedPreferences sharedPreferences;
    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference databaseReference;
    private ImageView logoImage;
    private String dbKey;
    private UserInformation userInfo;
    private Animation bounceAnimation;
    private long durationX = 2000;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_resultpage);
        sharedPreferences = getApplicationContext().getSharedPreferences(GameInformation.SHARED_PREF_KEY, MODE_PRIVATE);

        findViews();
        retrieveUserNameAndScore();
        animateViews();

        moveBackHome();


    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(this, UserHomeScreenActivity.class));
        super.onBackPressed();
    }

    public void findViews() {
        nameTextView = findViewById(R.id.player_name);
        titleForScore = findViewById(R.id.title_for_player_score);
        scoreTextView = findViewById(R.id.player_score);
        playAgainButton = findViewById(R.id.playagain_button);

        logoImage = findViewById(R.id.pic_for_result_logo);
        msgTextView = findViewById(R.id.congrats_msg);
    }

    public void moveBackHome() {
        playAgainButton.setOnClickListener(v -> {
            startActivity(new Intent(ResultPage.this, UserHomeScreenActivity.class));
            finish();
        });
    }

    public void animateViews() {

        bounceAnimation = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.bounce_anim);
        MyBounceInterpolator interpolator = new MyBounceInterpolator(0.2, 20);
        bounceAnimation.setInterpolator(interpolator);
        nameTextView.startAnimation(bounceAnimation);
        scoreTextView.startAnimation(bounceAnimation);
        msgTextView.startAnimation(bounceAnimation);

        ObjectAnimator animatorX = ObjectAnimator.ofFloat(logoImage, "x", 180f);
        animatorX.setDuration(durationX);

        ObjectAnimator rotateAnimator = ObjectAnimator.ofFloat(logoImage, "rotation", 360f);
        rotateAnimator.setDuration(durationX);

        AnimatorSet animatorSet = new AnimatorSet();
        animatorSet.playTogether(animatorX, rotateAnimator);
        animatorSet.start();

    }


    public void retrieveUserNameAndScore() {
        String playerName = sharedPreferences.getString(GameInformation.USERNAME_KEY, "");
        nameTextView.setText(playerName);

        final long userScore = sharedPreferences.getLong(GameInformation.USER_SCORE_KEY, 0);
        scoreTextView.setText(String.valueOf(userScore));

        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference("mARtians");
        DatabaseReference currentRef = databaseReference.child(playerName);

        currentRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String keys = "";
                for (DataSnapshot datas : dataSnapshot.getChildren()) {
                    keys = datas.getKey();
                }
                if (dataSnapshot.child(keys).exists()) {

                    currentRef.child(keys).setValue(userScore);
                } else {
                    UserInformation userInformation = new UserInformation();
                    userInformation.setUserscore(userScore);

                    currentRef.child(keys).setValue(userInformation);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}


