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

    public void findViews(){
        nameTextView = findViewById(R.id.player_name);
        titleForScore = findViewById(R.id.title_for_player_score);
        scoreTextView = findViewById(R.id.player_score);
        playAgainButton = findViewById(R.id.playagain_button);

        logoImage = findViewById(R.id.pic_for_result_logo);
    }

    public void moveBackHome(){
        playAgainButton.setOnClickListener(v -> {
            startActivity(new Intent(ResultPage.this, UserHomeScreenActivity.class));
            finish();
        });
    }

    public void animateViews(){

        bounceAnimation = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.bounce_anim);
        MyBounceInterpolator interpolator = new MyBounceInterpolator(0.2, 20);
        bounceAnimation.setInterpolator(interpolator);
        nameTextView.startAnimation(bounceAnimation);
        scoreTextView.startAnimation(bounceAnimation);

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

        Log.d("DB", "db" + databaseReference);

        currentRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String keys = "";
                for (DataSnapshot datas : dataSnapshot.getChildren()) {
                    keys = datas.getKey();
                    Log.d("FINDME", "onDataChange: "+keys);
                }
                if (dataSnapshot.child(keys).exists()) {

                    Log.d("FINDME", "chekcing user" + dataSnapshot.child(playerName).getChildren().toString());

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

//    private void retrieve3(){
//        String playerName = sharedPreferences.getString(GameInformation.USERNAME_KEY, "");
//        nameTextView.setText(playerName);
//
//        final long userScore = sharedPreferences.getLong(GameInformation.USER_SCORE_KEY, 0);
//        scoreTextView.setText(String.valueOf(userScore));
//
//        firebaseDatabase = FirebaseDatabase.getInstance();
//        databaseReference = firebaseDatabase.getReference("mARtians");
//
//        String key = databaseReference.push().getKey();
//
//        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                if(dataSnapshot.child(key).exists()){
//                    databaseReference.child(key).setValue(userInfo).addOnSuccessListener(new OnSuccessListener<Void>() {
//                        @Override
//                        public void onSuccess(Void aVoid) {
//                            userInfo.setUsername(playerName);
//                            userInfo.setUserscore(userScore);
//                            finish();
//                        }
//                    });
//                } else {
//                    UserInformation newUser = new UserInformation();
//                    newUser.setUsername(playerName);
//                    newUser.setUserscore(userScore);
//                    databaseReference.child(key).setValue(newUser).addOnSuccessListener(new OnSuccessListener<Void>() {
//                        @Override
//                        public void onSuccess(Void aVoid) {
//                            Log.d("Added", "New User added!" + newUser.getUsername() + newUser.getUserscore());
//                        }
//                    });
//                }
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError databaseError) {
//                Log.d("Error", "onUpdatingUser" + databaseError.toString());
//            }
//        });
//
//
//    }

//    private void retrieve2(){
//        String playerName = sharedPreferences.getString(GameInformation.USERNAME_KEY, "");
//        nameTextView.setText(playerName);
//
//        final long userScore = sharedPreferences.getLong(GameInformation.USER_SCORE_KEY, 0);
//        scoreTextView.setText(String.valueOf(userScore));
//
//        firebaseAuth = FirebaseAuth.getInstance();
//        FirebaseUser user = firebaseAuth.getCurrentUser();
//        userID = user.getUid();
//        Log.d("user", "USER" + userID);
//        firebaseDatabase = FirebaseDatabase.getInstance();
//        databaseReference = firebaseDatabase.getReference("mARtians");
//
////        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
////            @Override
////            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
////                UserInformation userInformation = dataSnapshot.getValue(UserInformation.class);
////                Log.d("RESULTPAGE", "findme" + userInformation);
////                for(DataSnapshot data: dataSnapshot.getChildren()){
////                    String key = data.getKey();
////                    if(dataSnapshot.child(key).exists()){
////                        userInformation.setUsername(playerName);
////                        userInformation.setUserscore(userScore);
////                    } else {
////                        UserInformation newUser = new UserInformation();
////                        newUser.setUsername(playerName);
////                        newUser.setUserscore(userScore);
////
////                        new FirebaseDatabaseHelper().addUser(newUser, new FirebaseDatabaseHelper.DataStatus() {
////                            @Override
////                            public void dataIsLoaded(List<UserInformation> userInformations, List<String> keys) {
////
////                            }
////
////                            @Override
////                            public void dataIsInserted() {
////                                System.out.println("Score is saved!");
////                            }
////                        });
////                    }
////                }
////            }
////
////            @Override
////            public void onCancelled(@NonNull DatabaseError databaseError) {
////                System.out.println("The read failed: " + databaseError.getCode());
////
////            }
////        });
//        // DatabaseReference currentRef = databaseReference.child(playerName);
//
//        Log.d("DB", "db" + databaseReference);
//
//        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                for (DataSnapshot datas : dataSnapshot.getChildren()) {
//                    String keys = datas.getKey();
//
//                    if (dataSnapshot.child(keys).exists()) {
//
//                        Log.d("FINDME", "chekcing user" + dataSnapshot.child(playerName));
//
////                    Log.d("FINDME", "onDataChange: " + dataSnapshot.getValue().toString());
////                    dataSnapshot.child(playerName).child("game 1").getValue();
////                    Log.d("FINDME", "getting game number" + dataSnapshot.child(playerName).child("game 1").getValue());
////                    currentRef.child("game 1").setValue(100);
//                        // databaseReference.child(keys).setValue(userScore);
//                        // databaseReference.child("score").setValue(userScore);
//
//                        UserInformation userInformation1 = dataSnapshot.getValue(UserInformation.class);
//                        userInformation1.setUsername(playerName);
//                        userInformation1.setUserscore(userScore);
//                        databaseReference.child(keys).setValue(userInformation1);
//                        finish();
//
//                    } else {
//                        UserInformation userInformation = new UserInformation();
//                        userInformation.setUsername(playerName);
//                        userInformation.setUserscore(userScore);
//
//                        new FirebaseDatabaseHelper().addUser(userInformation, new FirebaseDatabaseHelper.DataStatus() {
//                            @Override
//                            public void dataIsLoaded(List<UserInformation> userInformations, List<String> keys) {
//
//                            }
//
//                            @Override
//                            public void dataIsInserted() {
//                                Toast.makeText(ResultPage.this, "Score is saved successfully!", Toast.LENGTH_SHORT).show();
//
//                            }
//
//                        });
//
//                    }
//                }
//            }
//
//
////                long previousScore = dataSnapshot.getValue(Long.class);
////                long newScore = userScore + previousScore;
////                databaseReference.child(playerName).setValue(newScore);
////                Log.e("Key from db", "previous " + previousScore);
////                Log.e("Key from db", "current " + userScore);
////                Log.e("Key from db", "updated " + newScore);
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError databaseError) {
//                System.out.println("The read failed: " + databaseError.getCode());
//
//            }
//        });
//    }

}

