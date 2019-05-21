package org.pursuit.ar_wrld;

import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.ankushgrover.hourglass.Hourglass;
import com.google.ar.core.Anchor;
import com.google.ar.core.Frame;
import com.google.ar.core.HitResult;
import com.google.ar.core.Plane;
import com.google.ar.core.TrackingState;
import com.google.ar.sceneform.AnchorNode;
import com.google.ar.sceneform.FrameTime;
import com.google.ar.sceneform.Scene;
import com.google.ar.sceneform.animation.ModelAnimator;
import com.google.ar.sceneform.math.Quaternion;
import com.google.ar.sceneform.math.Vector3;
import com.google.ar.sceneform.rendering.AnimationData;
import com.google.ar.sceneform.rendering.ModelRenderable;
import com.google.ar.sceneform.ux.ArFragment;
import com.google.ar.sceneform.ux.BaseArFragment;
import com.google.ar.sceneform.ux.TransformableNode;

import org.pursuit.ar_wrld.modelObjects.ModelLoader;

import java.lang.ref.WeakReference;
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
    private long timeLeftInMilliseconds = 15000;
    int numOfModels = 0;
    private int scoreNumber;
    private String scoreString;
    private String aliensLeftString;
    private SharedPreferences sharedPreferences;
    private CountDownTimer alienAppearanceRate;
    private Vector3 vector;
    private TextView numOfAliensTv;
    private Hourglass alienSpawnRate;


    // Controls animation playback.
    private ModelAnimator animator;
    // Index of the current animation playing.
    private int nextAnimation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        // shootingButton = findViewById(R.id.shooting_button);
        msgForUser = findViewById(R.id.msg_for_user);
        countDownText = findViewById(R.id.timer_textview);
        sharedPreferences = getSharedPreferences(GameInformation.SHARED_PREF_KEY, MODE_PRIVATE);
        scorekeepingTv = findViewById(R.id.scorekeeping_textview);
        scorekeepingTv.setText(getString(R.string.default_score_text));
        numOfAliensTv = findViewById(R.id.number_of_aliens_textview);
        getStringRes();

        vector = new Vector3();
        setUpAR();

        AnchorNode anchorNode = new AnchorNode();
        anchorNode.setWorldPosition(new Vector3(0, 0, 0));

        arFragment.setOnTapArPlaneListener((hitResult, plane, motionEvent) -> Log.d(TAG, "onTapPlane: Event hit"));


        alienSpawnRate = new Hourglass(5000, 1000) {
            @Override
            public void onTimerTick(long timeRemaining) {

            }

            @Override
            public void onTimerFinish() {
                loadModel(anchorNode.getAnchor(), Uri.parse("andy.sfb"));

                Toast.makeText(MainActivity.this, "Model Loaded", Toast.LENGTH_SHORT).show();
                alienSpawnRate.startTimer();
            }
        };

        alienSpawnRate.startTimer();
    }

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
        startTimer();
    }

    private android.graphics.Point getScreenCenter() {
        View vw = findViewById(android.R.id.content);
        return new android.graphics.Point(vw.getWidth() / 2, vw.getHeight() / 2);
    }

    public void addNodeToScene(Anchor anchor, ModelRenderable renderable) {
        numOfModels++;
        Log.d(TAG, "addNodeToScene: IN THIS METHOD");
        getStringRes();
        numOfAliensTv.setText(aliensLeftString);
        AnchorNode anchorNode = new AnchorNode(anchor);
        TransformableNode node = new TransformableNode(arFragment.getTransformationSystem());
        node.setRenderable(renderable);
        node.setParent(anchorNode);
        vector.set(randomCoordinates(true), randomCoordinates(false), -.7f);

        Quaternion rotate = Quaternion.axisAngle(new Vector3(0,1f,0), 90f);
        node.setWorldRotation(rotate);
        node.setLocalPosition(vector);
        ModelLoader modelLoader = new ModelLoader(2);

        arFragment.getArSceneView().getScene().addChild(anchorNode);
        //Rotates the model every frame
        arFragment.getArSceneView().getScene().addOnUpdateListener(new Scene.OnUpdateListener() {
            @Override
            public void onUpdate(FrameTime frameTime) {
                Quaternion startQ = node.getLocalRotation();
                Quaternion rotateQ = Quaternion.axisAngle(new Vector3(0,1f,0), 5f);
                node.setLocalRotation(Quaternion.multiply(startQ,rotateQ));
            }
        });

        setNodeListener(node, anchorNode, modelLoader);
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

    private void setNodeListener(TransformableNode node, AnchorNode anchorNode, ModelLoader modelLoader) {

        node.setOnTapListener(((hitTestResult, motionEvent) -> {
            Log.d(TAG, "setNodeListener: " + modelLoader.getNumofLivesModel0());
            if (0 < modelLoader.getNumofLivesModel0()) {
                modelLoader.setNumofLivesModel0(modelLoader.getNumofLivesModel0() - 1);
                Toast.makeText(this, "Lives left: " + modelLoader.getNumofLivesModel0(), Toast.LENGTH_SHORT).show();
            } else {
                Log.d(TAG, "setNodeListener: In else state ");
                anchorNode.removeChild(node);
                numOfModels--;
                scoreNumber++;
                getStringRes();
                sharedPreferences.edit().putInt(GameInformation.USER_SCORE_KEY, scoreNumber).apply();
                Log.d(TAG, "setNodeListener: " + scoreString);
                Log.d(TAG, "setNodeListener: " + scorekeepingTv.getText().toString());
                Toast.makeText(this, "Enemy Eliminated", Toast.LENGTH_SHORT).show();
                scorekeepingTv.setText(scoreString);
                numOfAliensTv.setText(aliensLeftString);
            }
        }));
        Log.d(TAG, "setNodeListener: After if statement" + modelLoader.getNumofLivesModel0());
        node.select();
    }

    public void loadModel(Anchor anchor, Uri uri) {
        ModelRenderable.builder()
                .setSource(this, uri)
                .build()
                .thenAccept(modelRenderable -> {addNodeToScene(anchor,modelRenderable);});

        return;
    }

    public void startTimer(){
        countDownTimer = new CountDownTimer(timeLeftInMilliseconds, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                timeLeftInMilliseconds = millisUntilFinished;
                updateTimer();

            }

            @Override
            public void onFinish() {
                countDownTimer.cancel();
                countDownText.setText(R.string.time_up_msg);
                goToResultPage();

            }
        }.start();

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

    public void goToResultPage() {
//        Intent goToResultPageIntent = new Intent(MainActivity.this, ResultPage.class);
//        startActivity(goToResultPageIntent);
    }

    public float randomCoordinates(boolean isX) {
        Random random = new Random();
        if (isX) return random.nextFloat() - .700f;
        return random.nextFloat() - .500f;
    }


    // Number is displayed between -.7 and -1
    public static float randomZCoordinates(){
        Random random = new Random();
        Float maxFloat = .7f;
        Float minFloat = 1f;
        return random.nextFloat() * (maxFloat - minFloat) - maxFloat;
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (alienSpawnRate.isRunning()) alienSpawnRate.pauseTimer();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (alienSpawnRate.isPaused()) alienSpawnRate.resumeTimer();

    }
}
