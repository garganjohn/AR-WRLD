package org.pursuit.ar_wrld;

import android.animation.ObjectAnimator;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.ankushgrover.hourglass.Hourglass;
import com.google.ar.core.Anchor;
import com.google.ar.core.Frame;
import com.google.ar.core.Plane;
import com.google.ar.core.TrackingState;
import com.google.ar.sceneform.AnchorNode;
import com.google.ar.sceneform.FrameTime;
import com.google.ar.sceneform.Node;
import com.google.ar.sceneform.Scene;
import com.google.ar.sceneform.animation.ModelAnimator;
import com.google.ar.sceneform.math.Quaternion;
import com.google.ar.sceneform.math.Vector3;
import com.google.ar.sceneform.rendering.AnimationData;
import com.google.ar.sceneform.rendering.ModelRenderable;
import com.google.ar.sceneform.ux.ArFragment;
import com.google.ar.sceneform.ux.TransformableNode;

import org.pursuit.ar_wrld.Effects.AudioLoader;
import org.pursuit.ar_wrld.gameEndsFragments.GameOverFragment;
import org.pursuit.ar_wrld.login.UserHomeScreenActivity;
import org.pursuit.ar_wrld.modelObjects.ModelLoader;
import org.pursuit.ar_wrld.movement.MovementNode;
import org.pursuit.ar_wrld.util.ModelLocationIndicator;
import org.pursuit.ar_wrld.weaponsInfo.WeaponsAvailable;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Random;

public class MainActivity extends AppCompatActivity {
    public static final String TAG = "FINDME";
    private ArFragment arFragment;
    private ModelLocationIndicator mli;
    private ImageView leftArrow;
    private ImageView rightArrow;
    private TextView scorekeepingTv;
    private TextView msgForUser;
    private TextView countDownText;
    private int startingMedAmmo = 25;
    private boolean timerRunning;
    private CountDownTimer countDownTimer;
    private long timeLeftInMilliseconds = 45000;
    int numOfModels = 0;
    private int scoreNumber;
    private int scoreTillClockModel = 5000;
    private String scoreString;
    private String aliensLeftString;
    private String medAmmoCounter;
    private String medDamageCounter;
    private String medDamageString;
    private String medAmmoString;
    private SharedPreferences sharedPreferences;
    private CountDownTimer alienAppearanceRate;
    private Vector3 vector;
    private TextView numOfAliensTv;
    private TextView medWeaponAmmoTv;
    private Hourglass easyAlienSpawn;
    private Hourglass medAlienSpawn;
    private Hourglass hardAlienSpawn;
    private Hourglass startGame;
    private ImageView weakWeapon;
    private ImageView medWeapon;
    private WeaponsAvailable weaponSelection;
    private TextView gameInfoTv;
    private boolean isUserTimeWarned = false;
    private int weaponDamage;
    private boolean isWeakWeaponChosen;
    private boolean isMedWeaponChosen;
    private Animation startFromBottom;
    private Animation exitToBottom;
    private CountDownTimer exitAnimationTimer;
    private String difficulty;
    private CountDownTimer hitChangeColor;
    private CountDownTimer backToOriginalColor;
    private int repitionForColors = 0;
    Button shootingButton;
    private AudioLoader audioLoader;
    private ObjectAnimator objectAnimation;
    private ArrayList<Vector3> vector3List;
    View view;


    // Controls animation playback.
    private ModelAnimator animator;
    // Index of the current animation playing.
    private MovementNode anchorNode;
    private int nextAnimation;
    private int firstPointThreshold;
    private int increaseScoreTillClockModelEasy = 5000;
    private int increaseScoreTillClockModelMed = 15000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        showActionBar();
        view = findViewById(R.id.background_for_ar_view);
        difficulty = getIntent().getStringExtra(GameInformation.GAME_DIFFICULTY);
        findViews();
        weaponSetup();
        getStringRes();
        audioSetup();
        setupGameInfo();
        setMedAmmoTv();
        sharedPreferences = getSharedPreferences(GameInformation.SHARED_PREF_KEY, MODE_PRIVATE);
//        sharedPreferences = getSharedPreferences(UserTitleInformation.TITLE_SHAREDPREF_KEY, MODE_PRIVATE);


        scorekeepingTv.setText(scoreString);
        numOfAliensTv.setText(aliensLeftString);
        medWeaponAmmoTv.setText(medAmmoCounter);

        vector = new Vector3();
        setUpAR();

        // If user misses their shot account here
        onTapForMissInteraction();
        if (difficulty.equals(UserHomeScreenActivity.BOSS_LEVEL)){
            gameInfoPopup(R.string.boss_level,false);
            spawningAliens(true);
        }
        else {
            gameInfoPopup(R.string.game_intro, false);
            spawningAliens(false);
        }

        applyPerkToUser(sharedPreferences.getString(GameInformation.GAME_PERK_KEY, null));
    }

    private void applyPerkToUser(String whichPerk) {
        if (whichPerk == null){
            return;
        }
        switch (whichPerk){
            case GameInformation.MORE_AMMO_PERK:
                weaponSelection.setMedWeaponAmmo(startingMedAmmo+(startingMedAmmo / 2));
                setMedAmmoTv();
                break;
            case GameInformation.MORE_DAMAGE_PERK:
                weaponSelection.setMedWeaponDamage(4);
                setMedAmmoTv();
                break;
            case GameInformation.MORE_TIME_PERK:
                startGame.pauseTimer();
                startGame = null;
                timeLeftInMilliseconds += 20000;
                startGameTimer();
                break;
            case GameInformation.SLOW_TIME_PERK:
                increaseScoreTillClockModelEasy = 2500;
                increaseScoreTillClockModelMed = 7500;
                break;
        }
    }

    private void setupGameInfo() {
        startFromBottom = new TranslateAnimation(0, 0, 600f, 0);
        startFromBottom.setDuration(1000);

        exitToBottom = new TranslateAnimation(0, 0, 0, 600f);
        exitToBottom.setDuration(2000);

        startFromBottom.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                exitAnimationTimer.start();
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });

        exitToBottom.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                gameInfoTv.setVisibility(View.INVISIBLE);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });

        exitAnimationTimer = new CountDownTimer(6000, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {

            }

            @Override
            public void onFinish() {
                gameInfoTv.startAnimation(exitToBottom);
            }
        };

        hitChangeColor = new CountDownTimer(20,2000) {
            @Override
            public void onTick(long millisUntilFinished) {
                view.setBackgroundColor(ContextCompat.getColor(getApplicationContext(), R.color.warningColor));
            }

            @Override
            public void onFinish() {
                backToOriginalColor.start();
            }
        };

        backToOriginalColor = new CountDownTimer(20,2000) {
            @Override
            public void onTick(long millisUntilFinished) {
                view.setBackgroundColor(ContextCompat.getColor(getApplicationContext(), R.color.neutral_hit));
            }

            @Override
            public void onFinish() {
                repitionForColors++;
                if (repitionForColors < 5)
                hitChangeColor.start();
                else {
                    repitionForColors = 0;
                    view.setBackgroundColor(ContextCompat.getColor(getApplicationContext(), R.color.neutral_hit));
                }
            }
        };
    }

    private void gameInfoPopup(int stringToDisplay, boolean isWarning) {
        gameInfoTv.setText(stringToDisplay);
        if (gameInfoTv.getVisibility() == View.INVISIBLE) gameInfoTv.setVisibility(View.VISIBLE);
        if (isWarning)
            gameInfoTv.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.warningColor));
        gameInfoTv.startAnimation(startFromBottom);

    }

    private void audioSetup() {
        audioLoader = new AudioLoader(getApplicationContext());

    }

    private void onTapForMissInteraction() {
        arFragment.getArSceneView().getScene().setOnTouchListener((hitTestResult, motionEvent) -> {
            if (!isOutOfAmmo() && isMedWeaponChosen) {
                shootMedWeapon();
                setMedAmmoTv();
            }

            if (isMedWeaponChosen && isOutOfAmmo()) {
                isWeakWeaponChosen = true;
                isMedWeaponChosen = false;
                weaponSwitch();
            }

            hitChangeColor.start();

            return false;
        });
    }

    private void setMedAmmoTv() {
        medDamageString = Integer.toString(weaponSelection.getMedWeaponDamage());
        medAmmoString = Integer.toString(weaponSelection.getMedWeaponAmmo());
        medAmmoCounter = getString(R.string.med_weapon_info, medDamageString, medAmmoString);
        medWeaponAmmoTv.setText(medAmmoCounter);
    }

    private boolean isOutOfAmmo() {
        return weaponSelection.getMedWeaponAmmo() == 0;
    }

    private void shootMedWeapon() {
        weaponSelection.setMedWeaponAmmo(weaponSelection.getMedWeaponAmmo() - 1);
    }

    private void weaponSetup() {
        medWeapon.setAlpha(0.125f);
        setWeaponListener();
        weaponSelection = new WeaponsAvailable(startingMedAmmo);
        weaponDamage = weaponSelection.getWeakWeaponDamage();
    }

    private void weaponSwitch() {
        if (!isWeakWeaponChosen) {
            weakWeapon.setAlpha(0.125f);
        } else {
            weaponDamage = weaponSelection.getWeakWeaponDamage();
            weakWeapon.setAlpha(1f);
        }
        if (!isMedWeaponChosen) {
            medWeapon.setAlpha(0.125f);
        } else {
            weaponDamage = weaponSelection.getMedWeaponDamage();
            medWeapon.setAlpha(1f);
        }
    }

    private void setWeaponListener() {
        weakWeapon.setOnClickListener(v -> {
            isWeakWeaponChosen = true;
            isMedWeaponChosen = false;
            weaponSwitch();
        });

        medWeapon.setOnClickListener(v -> {
            if (weaponSelection.getMedWeaponAmmo() > 0) {
                isMedWeaponChosen = true;
                isWeakWeaponChosen = false;
                weaponSwitch();
            }
        });
    }

    private void findViews() {
        msgForUser = findViewById(R.id.msg_for_user);
        countDownText = findViewById(R.id.timer_textview);
        scorekeepingTv = findViewById(R.id.scorekeeping_textview);
        numOfAliensTv = findViewById(R.id.number_of_aliens_textview);
        medWeaponAmmoTv = findViewById(R.id.damage_for_med_weapon);
        weakWeapon = findViewById(R.id.weak_weapon);
        medWeapon = findViewById(R.id.med_weapon);

        rightArrow = findViewById(R.id.left_marker);
        leftArrow = findViewById(R.id.right_marker);
        mli = new ModelLocationIndicator(rightArrow, leftArrow);

        gameInfoTv = findViewById(R.id.game_info_textview);

    }

    private void spawningAliens(boolean isBoss) {

        AnchorNode anchorNode = new AnchorNode();
        anchorNode.setWorldPosition(new Vector3(0, 0, 0));

        if (isBoss) {
            Log.d(TAG, "spawningAliens: ");
            loadModel(anchorNode.getAnchor(), Uri.parse(GameInformation.BOSS_ENEMY), GameInformation.BOSS_ENEMY);
        } else {
            final boolean[] isMedEnemyAdded = {false};
            final boolean[] isHardEnemyAdded = {false};

            easyAlienSpawn = new Hourglass(4000, 1000) {
                @Override
                public void onTimerTick(long timeRemaining) {

                }


                @Override
                public void onTimerFinish() {
                    loadModel(anchorNode.getAnchor(), Uri.parse(GameInformation.EASY_ENEMY), GameInformation.EASY_ENEMY);

                    easyAlienSpawn.startTimer();

                    if (scoreNumber > 5000 && !isMedEnemyAdded[0]) {
                        isMedEnemyAdded[0] = true;
                        medAlienSpawn.startTimer();
                    }
                }
            };

            medAlienSpawn = new Hourglass(5000, 1000) {
                @Override
                public void onTimerTick(long timeRemaining) {

                }

                @Override
                public void onTimerFinish() {
                    loadModel(anchorNode.getAnchor(), Uri.parse(GameInformation.MEDIUM_ENEMY), GameInformation.MEDIUM_ENEMY);
                    medAlienSpawn.startTimer();

                    if (scoreNumber > 10000 && !isHardEnemyAdded[0]) {
                        isHardEnemyAdded[0] = true;
                        hardAlienSpawn.startTimer();
                    }
                }
            };

            hardAlienSpawn = new Hourglass(8000, 1000) {
                @Override
                public void onTimerTick(long timeRemaining) {

                }

                @Override
                public void onTimerFinish() {
                    loadModel(anchorNode.getAnchor(), Uri.parse(GameInformation.HARD_ENEMY), GameInformation.HARD_ENEMY);
                    hardAlienSpawn.startTimer();
                }
            };

            switch (difficulty) {
                case UserHomeScreenActivity.EASY_STRING:
                    easyAlienSpawn.startTimer();
                    break;
                case UserHomeScreenActivity.MEDIUM_STRING:
                    medAlienSpawn.startTimer();
                    break;
                case UserHomeScreenActivity.HARD_STRING:
                    hardAlienSpawn.setTime(2000);
                    hardAlienSpawn.startTimer();
                    break;
            }
        }

        startGameTimer();
    }


    @SuppressLint("StringFormatInvalid")
    private void getStringRes() {
        scoreString = getString(R.string.score_text, scoreNumber);
        aliensLeftString = getString(R.string.aliens_remaining_string, numOfModels);
    }

    private void playAnimation(ModelRenderable modelRenderable) {
        if (animator == null || !animator.isRunning()) {
            AnimationData data = modelRenderable.getAnimationData(nextAnimation);
            nextAnimation = (nextAnimation + 1);
            animator = new ModelAnimator(data, modelRenderable);
            animator.start();
        }
    }

    private void setUpAR() {

        arFragment = (ArFragment) getSupportFragmentManager().findFragmentById(R.id.sceneform_fragment);

        arFragment.getPlaneDiscoveryController().hide();
        arFragment.getPlaneDiscoveryController().setInstructionView(null);

        arFragment.getArSceneView().getScene().addOnUpdateListener(this::onUpdate);

    }

    private void onUpdate(FrameTime frameTime) {
        Frame frame = arFragment.getArSceneView().getArFrame();
        Collection<Plane> planes = frame.getUpdatedTrackables(Plane.class);
        for (Plane plane : planes) {
            if (plane.getTrackingState() == TrackingState.TRACKING) {
                break;
            }
        }
        if (anchorNode != null) {
            ArrayList<Node> overlappedNodes = arFragment.getArSceneView().getScene().overlapTestAll(anchorNode);
            for (Node node : overlappedNodes) {
                if (node instanceof MovementNode ) {
                    Toast.makeText(this,"Collision!",Toast.LENGTH_SHORT).show();
                    // May want to use a flag to check that the node wasn't overlapping the previous frame.
                    // Play sound if overlapping started.
                }
            }
        }
    }

    private android.graphics.Point getScreenCenter() {
        View vw = findViewById(android.R.id.content);
        return new android.graphics.Point(vw.getWidth() / 2, vw.getHeight() / 2);
    }

    public void addNodeToScene(Anchor anchor, ModelRenderable renderable, String whichEnemy) {
        numOfModels++;
        // AnchorNode anchorNode = new AnchorNode();

        switch (difficulty){
            case UserHomeScreenActivity.EASY_STRING:
                if (numOfModels > 9){
                    goToResultPage();
                }
            case UserHomeScreenActivity.MEDIUM_STRING:
                if (numOfModels > 6){
                    goToResultPage();
                }
            case UserHomeScreenActivity.HARD_STRING:
                Log.d(TAG, "setNodeListener: "+numOfModels);
                if (numOfModels > 4){
                    goToResultPage();
                }
        }

        anchorNode = new MovementNode();
        TransformableNode node = new TransformableNode(arFragment.getTransformationSystem());
        node.getScaleController().setMinScale(0.25f);
        node.getScaleController().setMaxScale(1.0f);
        getStringRes();
        numOfAliensTv.setText(aliensLeftString);
        node.setRenderable(renderable);

        node.setLocalScale(new Vector3(0.25f, 0.5f, 1.0f));
        node.setParent(anchorNode);
        vector.set(randomCoordinates(true), randomCoordinates(false), randomZCoordinates());

        Quaternion rotate = Quaternion.axisAngle(new Vector3(0, 1f, 0), 90f);

        anchorNode.randomMovement();
        node.setWorldRotation(rotate);
        node.setLocalPosition(vector);
        mli.indicate(vector);
        //TODO put location logic here

        ModelLoader modelLoader = new ModelLoader();
        boolean isTimerModel = false;

        switch (whichEnemy) {
            case GameInformation.EASY_ENEMY:
                modelLoader.setNumofLivesModel0(2);
                break;
            case GameInformation.MEDIUM_ENEMY:
                modelLoader.setNumofLivesModel0(3);
                break;
            case GameInformation.HARD_ENEMY:
                modelLoader.setNumofLivesModel0(4);
                break;
            case GameInformation.TIME_INCREASE_MODEL:
                modelLoader.setNumofLivesModel0(1);
                isTimerModel = true;
                Log.d(TAG, "addNodeToScene: " + node.getLocalScale());
                break;
            case GameInformation.BOSS_ENEMY:
                modelLoader.setNumofLivesModel0(30);
                break;
        }

        arFragment.getArSceneView().getScene().addChild(anchorNode);
        //Rotates the model every frame
        //Second parameter in Quaternion.axisAngle() measures speed of rotation
        arFragment.getArSceneView().getScene().addOnUpdateListener(new Scene.OnUpdateListener() {
            @Override
            public void onUpdate(FrameTime frameTime) {
                Quaternion startQ = node.getLocalRotation();
                Quaternion rotateQ = Quaternion.axisAngle(new Vector3(0, 1f, 0), 5f);
                node.setLocalRotation(Quaternion.multiply(startQ, rotateQ));
            }
        });

        setNodeListener(node, anchorNode, modelLoader, isTimerModel, whichEnemy);
        playAnimation(renderable);
    }


    public void onException(Throwable throwable) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(throwable.getMessage())
                .setTitle("Codelab error!");
        AlertDialog dialog = builder.create();
        dialog.show();
        return;
    }

    private void setNodeListener(TransformableNode node, AnchorNode anchorNode, ModelLoader modelLoader, boolean isTimerModel, String whichEnemy) {
        node.setOnTapListener(((hitTestResult, motionEvent) -> {

            if (!isOutOfAmmo() && isMedWeaponChosen) {
                shootMedWeapon();
                setMedAmmoTv();
            }

            if (isMedWeaponChosen && isOutOfAmmo()) {
                isWeakWeaponChosen = true;
                isMedWeaponChosen = false;
                weaponSwitch();
            }

            modelLoader.setNumofLivesModel0(modelLoader.getNumofLivesModel0() - weaponDamage);
            if (0 < modelLoader.getNumofLivesModel0()) {
                laserSound();
                Toast.makeText(this, "Lives left: " + modelLoader.getNumofLivesModel0(), Toast.LENGTH_SHORT).show();
            } else {
                anchorNode.removeChild(node);
                mli.cancelAnimator();


                switch (whichEnemy) {
                    case GameInformation.EASY_ENEMY:
                        scoreNumber += 1000;
                        break;
                    case GameInformation.MEDIUM_ENEMY:
                        scoreNumber += 2500;
                        break;
                    case GameInformation.HARD_ENEMY:
                        scoreNumber += 5000;
                        break;
                    case GameInformation.BOSS_ENEMY:
                        scoreNumber += 25000;
                        break;
                }

                if (isTimerModel) {
                    Log.d(TAG, "setNodeListener: TIME LEFT BEFORE CHANGE: " + timeLeftInMilliseconds);
                    timeLeftInMilliseconds += 2000;
                    scoreNumber += 500;
                    startGame.pauseTimer();
                    startGame = null;
                    startGameTimer();
                    Log.d(TAG, "setNodeListener: TIME LEFT AFTER CHANGE:" + timeLeftInMilliseconds);
                    Toast.makeText(this, "Time Extended by 5 sec", Toast.LENGTH_SHORT).show();
                }

                if (scoreNumber >= scoreTillClockModel) {
                    firstPointThreshold = 20000;
                    if (scoreTillClockModel <= firstPointThreshold) {
                        scoreTillClockModel += increaseScoreTillClockModelEasy;
                    } else {
                        scoreTillClockModel += increaseScoreTillClockModelMed;
                    }
                    loadModel(anchorNode.getAnchor(), Uri.parse(GameInformation.TIME_INCREASE_MODEL), GameInformation.TIME_INCREASE_MODEL);
                }

                numOfModels--;
                shootSound();
                getStringRes();
                sharedPreferences.edit().putInt(GameInformation.USER_SCORE_KEY, scoreNumber).commit();
                Log.d(TAG, "setNodeListener: " + scoreString);
                Log.d(TAG, "setNodeListener: " + scorekeepingTv.getText().toString());
                scorekeepingTv.setText(scoreString);
                numOfAliensTv.setText(aliensLeftString);

            }
        }));
        node.select();
    }

    public void loadModel(Anchor anchor, Uri uri, String whichEnemy) {
        ModelRenderable.builder()
                .setSource(this, uri)
                .build()
                .thenAccept(modelRenderable -> {
                    addNodeToScene(anchor, modelRenderable, whichEnemy);
                });
        return;
    }

    public void startGameTimer() {
        backgroundMusic();

        startGame = new Hourglass(timeLeftInMilliseconds, 1000) {
            @Override
            public void onTimerTick(long timeRemaining) {
                timeLeftInMilliseconds = timeRemaining;
                updateTimer();
                if (timeLeftInMilliseconds < 10000 && !isUserTimeWarned) {
                    isUserTimeWarned = true;
                    gameInfoPopup(R.string.timer_warning, true);
                }
            }

            @Override
            public void onTimerFinish() {
                countDownText.setText("Time's Up");
                stopAudio();
                showDialog();
                new Hourglass(3000, 1000) {
                    @Override
                    public void onTimerTick(long timeRemaining) {

                    }

                    @Override
                    public void onTimerFinish() {
                        goToResultPage();

                    }
                }.startTimer();
            }
        };
        startGame.startTimer();
    }

    public void updateTimer() {
        int minutes = (int) timeLeftInMilliseconds / 60000;
        int seconds = (int) timeLeftInMilliseconds % 60000 / 1000;

        String timeLeftText;

        timeLeftText = "0" + minutes;
        timeLeftText += ":";
        if (seconds < 10) {
            timeLeftText += "0";
        }
        timeLeftText += seconds;

        countDownText.setText(timeLeftText);
    }

    public void showDialog() {

        AlertDialog.Builder aBuilder = new AlertDialog.Builder(this);
        aBuilder.setMessage("Press Continue to see your results");
        aBuilder.setPositiveButton("Continue", (dialog, which) -> Toast.makeText(MainActivity.this, "Button has been clicked", Toast.LENGTH_SHORT).show());
        aBuilder.show();
    }

    public void goToResultPage() {
        Log.d(TAG, "goToResultPage: ");
        GameOverFragment gameOverFragment = GameOverFragment.newInstance("apple","banana");
        getSupportFragmentManager().beginTransaction().add(R.id.result_container, gameOverFragment).commit();

        
//        Intent goToResultPageIntent = new Intent(MainActivity.this, ResultPage.class);
//        startActivity(goToResultPageIntent);
    }

    //Random X coordinates will be between -.2 to .4f
    //Radnom Y coordinates will be between -.5 to .5
    public float randomCoordinates(boolean isX) {
        Random random = new Random();

        if (isX) {
            float min = -.3f;
            float max = .3f;
            return (min + random.nextFloat() * (max - min));
        }

        return random.nextFloat() - .500f;
    }

    // Number is displayed between -.7 and -1
    public static float randomZCoordinates() {
        Random random = new Random();
        Float minFloat = .7f;
        Float maxFloat = 1f;
        //Location behind user
//        if (new Random().nextInt(2) == 0) {
//            return minFloat + random.nextFloat() * (maxFloat - minFloat);
//        }
        //Location infront of user
        return -(minFloat + random.nextFloat() * (maxFloat - minFloat));
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (easyAlienSpawn != null && easyAlienSpawn.isRunning()) easyAlienSpawn.pauseTimer();
        if (medAlienSpawn != null && medAlienSpawn.isRunning()) medAlienSpawn.pauseTimer();
        if (hardAlienSpawn != null && hardAlienSpawn.isRunning()) hardAlienSpawn.pauseTimer();

    }

    @Override
    protected void onResume() {
        super.onResume();
        if (easyAlienSpawn != null && easyAlienSpawn.isPaused()) easyAlienSpawn.resumeTimer();
        if (medAlienSpawn != null && medAlienSpawn.isPaused()) easyAlienSpawn.resumeTimer();
        if (hardAlienSpawn != null && hardAlienSpawn.isPaused()) easyAlienSpawn.resumeTimer();

    }

    public void shootSound() {
        audioSetup();
        audioLoader.explodeSound();
    }

    public void laserSound() {

        audioSetup();
        audioLoader.laserSound();
    }

    public void backgroundMusic() {
        audioSetup();
        audioLoader.backGroundMusic();
    }

    public void stopAudio() {
        audioLoader.stopAudio();
    }

    private void showActionBar() {
        ActionBar actionBar = getSupportActionBar();
        actionBar.show();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
