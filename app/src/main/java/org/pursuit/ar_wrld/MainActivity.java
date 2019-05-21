package org.pursuit.ar_wrld;

import android.content.SharedPreferences;
import android.graphics.Point;
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
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.ankushgrover.hourglass.Hourglass;
import com.google.ar.core.Anchor;
import com.google.ar.core.Frame;
import com.google.ar.core.HitResult;
import com.google.ar.core.Plane;
import com.google.ar.core.Trackable;
import com.google.ar.core.TrackingState;
import com.google.ar.sceneform.AnchorNode;
import com.google.ar.sceneform.FrameTime;
import com.google.ar.sceneform.Scene;
import com.google.ar.sceneform.animation.ModelAnimator;
import com.google.ar.sceneform.math.Vector3;
import com.google.ar.sceneform.rendering.AnimationData;
import com.google.ar.sceneform.rendering.ModelRenderable;
import com.google.ar.sceneform.ux.ArFragment;
import com.google.ar.sceneform.ux.BaseArFragment;
import com.google.ar.sceneform.ux.TransformableNode;

import org.pursuit.ar_wrld.modelObjects.ModelLoader;

import java.lang.ref.WeakReference;
import java.util.Collection;
import java.util.List;
import java.util.Random;

public class MainActivity extends AppCompatActivity {
    public static final String TAG = "FINDME";
    private ArFragment arFragment;
    private ModelLoader modelLoader1;
    private WeakReference weakReference;
    private TextView scorekeepingTv;
    private TextView msgForUser;
    private TextView countDownText;
    private boolean timerRunning;
    private CountDownTimer countDownTimer;
    private long timeLeftInMilliseconds = 15000;
    int numOfModels = 0;
    private int scoreNumber;
    private String stringPlaceHolder;
    private SharedPreferences sharedPreferences;
    private CountDownTimer alienAppearanceRate;
    private Vector3 vector;
    private TextView numOfAliensTv;
    private Hourglass alienSpawnRate;


    // Controls animation playback.
    private ModelAnimator animator;
    // Index of the current animation playing.
    private int nextAnimation;
    private ModelRenderable andy;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
       // shootingButton = findViewById(R.id.shooting_button);
        msgForUser = findViewById(R.id.msg_for_user);
        countDownText = findViewById(R.id.timer_textview);
        weakReference = new WeakReference<>(this);
        sharedPreferences = getSharedPreferences(GameInformation.SHARED_PREF_KEY, MODE_PRIVATE);
        scorekeepingTv = findViewById(R.id.scorekeeping_textview);
        scorekeepingTv.setText(getString(R.string.default_score_text));
        numOfAliensTv = findViewById(R.id.number_of_aliens_textview);

        vector = new Vector3();
        setUpAR();

//        modelLoader1 = new ModelLoader(weakReference);
        AnchorNode anchorNode = new AnchorNode();
        anchorNode.setWorldPosition(new Vector3(0, 0, 0));
//        modelLoader1.loadModel(anchorNode.getAnchor(), Uri.parse("andy.sfb"));

        arFragment.setOnTapArPlaneListener(new BaseArFragment.OnTapArPlaneListener() {
            @Override
            public void onTapPlane(HitResult hitResult, Plane plane, MotionEvent motionEvent) {
                Log.d(TAG, "onTapPlane: Event hit");
            }
        });

        alienSpawnRate = new Hourglass(5000, 1000) {
            @Override
            public void onTimerTick(long timeRemaining) {

            }

            @Override
            public void onTimerFinish() {
                loadModel(anchorNode.getAnchor(), Uri.parse("andy.sfb"));
                numOfAliensTv.setText(String.valueOf(numOfModels));
                Toast.makeText(MainActivity.this, "Model Loaded", Toast.LENGTH_SHORT).show();
                alienSpawnRate.startTimer();
            }
        };

        alienSpawnRate.startTimer();
    }

    private void playAnimation(ModelRenderable modelRenderable){
        if (animator == null || !animator.isRunning()){
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

//        arFragment.getArSceneView().getScene().addOnUpdateListener(frameTime -> {
//            arFragment.onUpdate(frameTime);
//        });
        initializeGallery();
    }

    private void onUpdate(FrameTime frameTime) {
//        if (numOfModels > 0) return;
//        modelLoader1 = new ModelLoader(weakReference);
        Frame frame = arFragment.getArSceneView().getArFrame();
        Collection<Plane> planes = frame.getUpdatedTrackables(Plane.class);
        for (Plane plane : planes) {
            if (plane.getTrackingState() == TrackingState.TRACKING) {
//                addObject(Uri.parse("andy_dance.sfb"));
                break;
            }
        }
        startTimer();
    }

    private android.graphics.Point getScreenCenter() {
        View vw = findViewById(android.R.id.content);
        return new android.graphics.Point(vw.getWidth() / 2, vw.getHeight() / 2);
    }

    private void initializeGallery() {
        LinearLayout gallery = findViewById(R.id.gallery_layout);

        ImageView andy = new ImageView(this);
        andy.setImageResource(R.drawable.droid_thumb);
        andy.setContentDescription("andy");
        andy.setOnClickListener(view -> {
            addObject(Uri.parse("andy.sfb"));
        });
        gallery.addView(andy);

        ImageView cabin = new ImageView(this);
        cabin.setImageResource(R.drawable.cabin_thumb);
        cabin.setContentDescription("cabin");
        cabin.setOnClickListener(view -> {
            addObject(Uri.parse("Cabin.sfb"));
        });
        gallery.addView(cabin);

        ImageView house = new ImageView(this);
        house.setImageResource(R.drawable.house_thumb);
        house.setContentDescription("house");
        house.setOnClickListener(view -> {
            addObject(Uri.parse("House.sfb"));
        });
        gallery.addView(house);

        ImageView igloo = new ImageView(this);
        igloo.setImageResource(R.drawable.igloo_thumb);
        igloo.setContentDescription("igloo");
        igloo.setOnClickListener(view -> {
            addObject(Uri.parse("igloo.sfb"));
        });
        gallery.addView(igloo);
    }

    private void addObject(Uri model) {
        Frame frame = arFragment.getArSceneView().getArFrame();
        Point pt = getScreenCenter();
        List<HitResult> hits;
        if (frame != null) {
            hits = frame.hitTest(pt.x, pt.y);
            for (HitResult hit : hits) {
                Trackable trackable = hit.getTrackable();
                if (trackable instanceof Plane &&
                        ((Plane) trackable).isPoseInPolygon(hit.getHitPose())) {
//                    modelLoader1.loadModel(hit.createAnchor(), model);
                    break;

                }
            }
        }
    }

    public void addNodeToScene(Anchor anchor, ModelRenderable renderable) {
        numOfModels++;
        Log.d(TAG, "addNodeToScene: IN THIS METHOD");
        AnchorNode anchorNode = new AnchorNode(anchor);
        TransformableNode node = new TransformableNode(arFragment.getTransformationSystem());
        node.setRenderable(renderable);
        node.setParent(anchorNode);
//        node.setWorldPosition(new Vector3(4.0f, 2f, 0.450f));
        vector.set(randomCoordinates(true), randomCoordinates(false), randomZCoordinates());
        node.setLocalPosition(vector);
//        modelLoader1 = new ModelLoader(weakReference);
        ModelLoader modelLoader = new ModelLoader(2);
        arFragment.getArSceneView().getScene().addChild(anchorNode);

        setNodeListener(node, anchorNode, modelLoader);
        playAnimation(renderable);
        // setNodeListener(node2, anchorNode, modelLoader3);
    }

//    private void loadModel(Anchor anchor, Uri uri, ModelLoader modelLoader) {
//        if (modelLoader.getOwner() == null) {
//            Log.d(TAG, "Activity is null.  Cannot load model.");
//            return;
//        }
//        ModelRenderable.builder()
//                .setSource(owner.get(), uri)
//                .build()
//                .handle((renderable, throwable) -> {
//                    MainActivity activity = owner.get();
//                    if (activity == null) {
//                        return null;
//                    } else if (throwable != null) {
//                        activity.onException(throwable);
//                    } else {
//                        activity.addNodeToScene(anchor, renderable);
//                    }
//                    return null;
//                });
//
//        return;
//    }

    public void onException(Throwable throwable) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(throwable.getMessage())
                .setTitle("Codelab error!");
        AlertDialog dialog = builder.create();
        dialog.show();
        return;
    }

    private void setNodeListener(TransformableNode node, AnchorNode anchorNode, ModelLoader modelLoader){
        node.setOnTapListener(((hitTestResult, motionEvent) -> {
            Log.d(TAG, "setNodeListener: "+modelLoader.getNumofLivesModel0());
            if (0 < modelLoader.getNumofLivesModel0()){
                modelLoader.setNumofLivesModel0(modelLoader.getNumofLivesModel0() - 1);
                Toast.makeText(this, "Lives left: "+modelLoader.getNumofLivesModel0(), Toast.LENGTH_SHORT).show();
            }
            else {
                Log.d(TAG, "setNodeListener: In else state ");
                anchorNode.removeChild(node);
                numOfModels--;
                scoreNumber++;
                stringPlaceHolder = getString(R.string.score_text, scoreNumber);
                scorekeepingTv.setText(stringPlaceHolder);
                sharedPreferences.edit().putInt(GameInformation.USER_SCORE_KEY, scoreNumber).apply();
                Log.d(TAG, "setNodeListener: "+stringPlaceHolder);
                Log.d(TAG, "setNodeListener: "+scorekeepingTv.getText().toString());
                Toast.makeText(this, "Enemy Eliminated", Toast.LENGTH_SHORT).show();
                numOfAliensTv.setText(String.valueOf(numOfModels));
            }
        }));
        Log.d(TAG, "setNodeListener: After if statement"+modelLoader.getNumofLivesModel0());
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

    public void updateTimer(){

        int minutes = (int) timeLeftInMilliseconds / 60000;
        int seconds = (int) timeLeftInMilliseconds % 60000 / 1000;

        String timeLeftText;

        timeLeftText = "0" + minutes;
        timeLeftText += ":";
        if(seconds < 10) {timeLeftText += "0";}
        timeLeftText += seconds;

        countDownText.setText(timeLeftText);

    }

    public void goToResultPage(){
//        Intent goToResultPageIntent = new Intent(MainActivity.this, ResultPage.class);
//        startActivity(goToResultPageIntent);
    }

    public float randomCoordinates(boolean isX){
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
