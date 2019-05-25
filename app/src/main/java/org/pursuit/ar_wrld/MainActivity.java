package org.pursuit.ar_wrld;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
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
import com.google.ar.sceneform.HitTestResult;
import com.google.ar.sceneform.Scene;
import com.google.ar.sceneform.animation.ModelAnimator;
import com.google.ar.sceneform.math.Quaternion;
import com.google.ar.sceneform.math.Vector3;
import com.google.ar.sceneform.rendering.AnimationData;
import com.google.ar.sceneform.rendering.ModelRenderable;
import com.google.ar.sceneform.ux.ArFragment;
import com.google.ar.sceneform.ux.TransformableNode;

import org.pursuit.ar_wrld.Effects.AudioLoader;
import org.pursuit.ar_wrld.modelObjects.ModelLoader;
import org.pursuit.ar_wrld.weaponsInfo.WeaponsAvailable;

import java.util.Collection;
import java.util.Random;

public class MainActivity extends AppCompatActivity {
    public static final String TAG = "FINDME";
    private ArFragment arFragment;
    private TextView scorekeepingTv;
    private TextView msgForUser;
    private TextView countDownText;
    private boolean timerRunning;
    private CountDownTimer countDownTimer;
    private long timeLeftInMilliseconds = 30000;
    int numOfModels = 0;
    private int scoreNumber;
    private int scoreTillClockModel = 2000;
    private String scoreString;
    private String aliensLeftString;
    private String medAmmoCounter;
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
    private int weaponDamage;
    private boolean isWeakWeaponChosen;
    private boolean isMedWeaponChosen;
    Button shootingButton;


    // Controls animation playback.
    private ModelAnimator animator;
    // Index of the current animation playing.
    private int nextAnimation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
//        shootingButton = findViewById(R.id.shooting_button);
        findViews();
        weaponSetup();
        getStringRes();
        audioSetup();

        sharedPreferences = getSharedPreferences(GameInformation.SHARED_PREF_KEY, MODE_PRIVATE);
        scorekeepingTv.setText(scoreString);
        numOfAliensTv.setText(aliensLeftString);
        medWeaponAmmoTv.setText(medAmmoCounter);

        vector = new Vector3();
        setUpAR();
      
        arFragment.setOnTapArPlaneListener((hitResult, plane, motionEvent) -> Log.d(TAG, "onTapPlane: Event hit"));
        // If user misses their shot account here
        onTapForMissInteraction();
        spawningAliens();
    }

    private void audioSetup() {
        AudioLoader audioLoader = new AudioLoader(getApplicationContext());
        audioLoader.setShootingSound(R.raw.laser_sound);
    }

    private void onTapForMissInteraction() {
        arFragment.getArSceneView().getScene().setOnTouchListener(new Scene.OnTouchListener() {
            @Override
            public boolean onSceneTouch(HitTestResult hitTestResult, MotionEvent motionEvent) {
                if (!isOutOfAmmo() && isMedWeaponChosen){
                    shootMedWeapon();
                    setMedAmmoTv();
                }

                if (isMedWeaponChosen && isOutOfAmmo()) {
                    isWeakWeaponChosen = true;
                    isMedWeaponChosen = false;
                    weaponSwitch();
                }
                return false;
            }
        });
    }

    private void setMedAmmoTv() {
        medAmmoCounter = getString(R.string.med_weapon_info, weaponSelection.getMedWeaponAmmo());
        medWeaponAmmoTv.setText(medAmmoCounter);
    }

    private boolean isOutOfAmmo(){
        return weaponSelection.getMedWeaponAmmo() == 0;
    }

    private void shootMedWeapon() {
        weaponSelection.setMedWeaponAmmo(weaponSelection.getMedWeaponAmmo()-1);
    }

    private void weaponSetup() {
        medWeapon.setAlpha(0.125f);
        setWeaponListener();
        weaponSelection = new WeaponsAvailable(25);
        weaponDamage = weaponSelection.getWeakWeaponDamage();
    }

    private void weaponSwitch(){
        if (!isWeakWeaponChosen){
            weakWeapon.setAlpha(0.125f);
        }else {
            weaponDamage = weaponSelection.getWeakWeaponDamage();
            weakWeapon.setAlpha(1f);
        }
        if (!isMedWeaponChosen){
            medWeapon.setAlpha(0.125f);
        }else {
            weaponDamage = weaponSelection.getMedWeaponDamage();
            medWeapon.setAlpha(1f);
        }
    }

    private void setWeaponListener(){
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
    }

    private void spawningAliens() {
        final boolean[] isMedEnemyAdded = {false};
        final boolean[] isHardEnemyAdded = {false};
        AnchorNode anchorNode = new AnchorNode();
        anchorNode.setWorldPosition(new Vector3(0, 0, 0));
        easyAlienSpawn = new Hourglass(2000, 1000) {
            @Override
            public void onTimerTick(long timeRemaining) {

            }

            @Override
            public void onTimerFinish() {
                loadModel(anchorNode.getAnchor(), Uri.parse(GameInformation.EASY_ENEMY), GameInformation.EASY_ENEMY);

                Toast.makeText(MainActivity.this, "Model Loaded", Toast.LENGTH_SHORT).show();
                easyAlienSpawn.startTimer();

                if (scoreNumber > 10000 && !isMedEnemyAdded[0]){
                    isMedEnemyAdded[0] = true;
                    Toast.makeText(MainActivity.this, "Med Enemy coming in", Toast.LENGTH_SHORT).show();
                    medAlienSpawn.startTimer();
                }
            }
        };

        medAlienSpawn = new Hourglass(3000, 1000) {
            @Override
            public void onTimerTick(long timeRemaining) {

            }

            @Override
            public void onTimerFinish() {
                loadModel(anchorNode.getAnchor(), Uri.parse(GameInformation.MEDIUM_ENEMY), GameInformation.MEDIUM_ENEMY);
                medAlienSpawn.startTimer();

                if (scoreNumber > 25000 && !isHardEnemyAdded[0]){
                    isHardEnemyAdded[0] = true;
                    hardAlienSpawn.startTimer();
                }
            }
        };

        hardAlienSpawn = new Hourglass(6000, 1000) {
            @Override
            public void onTimerTick(long timeRemaining) {

            }

            @Override
            public void onTimerFinish() {
                loadModel(anchorNode.getAnchor(), Uri.parse(GameInformation.HARD_ENEMY), GameInformation.HARD_ENEMY);
                hardAlienSpawn.startTimer();
            }
        };

        easyAlienSpawn.startTimer();
        startGameTimer();
    }


    private void getStringRes() {
        scoreString = getString(R.string.score_text, scoreNumber);
        aliensLeftString = getString(R.string.aliens_remaining_string, numOfModels);
        medAmmoCounter = getString(R.string.med_weapon_info,weaponSelection.getMedWeaponAmmo());
    }

    private void playAnimation(ModelRenderable modelRenderable) {
        if (animator == null || !animator.isRunning()) {
            AnimationData data = modelRenderable.getAnimationData(nextAnimation);
            nextAnimation = (nextAnimation + 1);
            animator = new ModelAnimator(data, modelRenderable);
            animator.start();
        }
    }

    private void detectHit(Button button) {
        button.setOnClickListener(v -> {
        });
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
    }

    private android.graphics.Point getScreenCenter() {
        View vw = findViewById(android.R.id.content);
        return new android.graphics.Point(vw.getWidth() / 2, vw.getHeight() / 2);
    }

    public void addNodeToScene(Anchor anchor, ModelRenderable renderable, String whichEnemy) {
        numOfModels++;
        Log.d(TAG, "addNodeToScene: IN THIS METHOD");
        AnchorNode anchorNode = new AnchorNode();
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

        node.setWorldRotation(rotate);
        node.setLocalPosition(vector);

        ModelLoader modelLoader = new ModelLoader();
        boolean isTimerModel = false;

        if (whichEnemy == GameInformation.EASY_ENEMY){
            modelLoader.setNumofLivesModel0(3);
        }
        else if (whichEnemy == GameInformation.MEDIUM_ENEMY){
            modelLoader.setNumofLivesModel0(6);
        }
        else if (whichEnemy == GameInformation.HARD_ENEMY){
            modelLoader.setNumofLivesModel0(10);
        }
        else if (whichEnemy == GameInformation.TIME_INCREASE_MODEL){
            modelLoader.setNumofLivesModel0(1);
            isTimerModel = true;
            Log.d(TAG, "addNodeToScene: "+node.getLocalScale());
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
//        //TODO check for hit detection
//        shootingButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if (node.getWorldPosition().x == 0 && node.getWorldPosition().y == 0 && 0 < node.getWorldPosition().z ){
//                    Toast.makeText(MainActivity.this, "ENEMY HIT WITH SHOOTING BUTTON", Toast.LENGTH_SHORT).show();
//                }
//            }
//        });
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
                Toast.makeText(this, "Lives left: " + modelLoader.getNumofLivesModel0(), Toast.LENGTH_SHORT).show();
            } else {
                anchorNode.removeChild(node);

                if (whichEnemy == GameInformation.EASY_ENEMY){
                    scoreNumber += 1000;
                }
                else if (whichEnemy == GameInformation.MEDIUM_ENEMY){
                    scoreNumber += 2500;
                }
                else if (whichEnemy == GameInformation.HARD_ENEMY){
                    scoreNumber += 5000;
                }

                if (isTimerModel){
                    Log.d(TAG, "setNodeListener: TIME LEFT BEFORE CHANGE: "+timeLeftInMilliseconds);
                    timeLeftInMilliseconds += 5000;
                    scoreNumber += 500;
                    startGame.pauseTimer();
                    startGame = null;
                    startGameTimer();
                    Log.d(TAG, "setNodeListener: TIME LEFT AFTER CHANGE:"+timeLeftInMilliseconds);
                    Toast.makeText(this, "Time Extended by 5 sec", Toast.LENGTH_SHORT).show();
                }

                if (scoreNumber >= scoreTillClockModel){
                    if (scoreTillClockModel <= 20000){
                        scoreTillClockModel += 5000;
                    }
                    else {
                        scoreTillClockModel += 10000;
                    }
                    loadModel(anchorNode.getAnchor(), Uri.parse(GameInformation.TIME_INCREASE_MODEL), GameInformation.TIME_INCREASE_MODEL);
                }

                numOfModels--;
                getStringRes();
                sharedPreferences.edit().putInt(GameInformation.USER_SCORE_KEY, scoreNumber).apply();
                Log.d(TAG, "setNodeListener: " + scoreString);
                Log.d(TAG, "setNodeListener: " + scorekeepingTv.getText().toString());
                Toast.makeText(this, "Enemy Eliminated", Toast.LENGTH_SHORT).show();
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

    public void startGameTimer(){
        startGame = new Hourglass(timeLeftInMilliseconds, 1000) {
            @Override
            public void onTimerTick(long timeRemaining) {
                timeLeftInMilliseconds = timeRemaining;
                Log.d(TAG, "onTimerTick: "+timeLeftInMilliseconds);
                updateTimer();
            }

            @Override
            public void onTimerFinish() {
                countDownText.setText("Time's Up");
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

//        final AlertDialog.Builder dialog = new AlertDialog.Builder(getApplicationContext()).setTitle("Loading...").setMessage("Please wait for your results!");
        AlertDialog.Builder aBuilder = new AlertDialog.Builder(this);
        aBuilder.setMessage("Press Continue to see your results");
        aBuilder.setPositiveButton("Continue", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Toast.makeText(MainActivity.this, "Button has been clicked", Toast.LENGTH_SHORT).show();
            }
        });
        aBuilder.show();
//        dialog.setPositiveButton(" ", (dialog1, whichButton) -> Toast.makeText(MainActivity.this, "Exiting", Toast.LENGTH_SHORT).show());
//        final AlertDialog alert = dialog.create();
//        alert.show();

//        final Handler handler = new Handler();
//        final Runnable runnable = () -> {
//            if (alert.isShowing()) {
//                alert.dismiss();
//            }
//        };
//
//        alert.setOnDismissListener(dialog12 -> handler.removeCallbacks(runnable));
//
//        handler.postDelayed(runnable, 1000);

    }

    public void goToResultPage() {
        Intent goToResultPageIntent = new Intent(MainActivity.this, ResultPage.class);
        startActivity(goToResultPageIntent);
    }
    //Random X coordinates will be between -.3 to .8f
    //Radnom Y coordinates will be between -.5 to .5
    public float randomCoordinates(boolean isX) {
        Random random = new Random();

        if (isX){
            float min = -.5f;
            float max = .6f;
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
        if (new Random().nextInt(2) == 0){
            return minFloat + random.nextFloat() * (maxFloat - minFloat);
        }
        //Location infront of user
        return -(minFloat + random.nextFloat() * (maxFloat - minFloat));
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (easyAlienSpawn.isRunning()) easyAlienSpawn.pauseTimer();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (easyAlienSpawn.isPaused()) easyAlienSpawn.resumeTimer();
    }
}
