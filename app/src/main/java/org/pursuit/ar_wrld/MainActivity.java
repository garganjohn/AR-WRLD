package org.pursuit.ar_wrld;

import android.animation.ObjectAnimator;
import android.annotation.SuppressLint;
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
import android.view.animation.LinearInterpolator;
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
import com.google.ar.sceneform.Node;
import com.google.ar.sceneform.animation.ModelAnimator;
import com.google.ar.sceneform.math.Vector3;
import com.google.ar.sceneform.math.Vector3Evaluator;
import com.google.ar.sceneform.rendering.AnimationData;
import com.google.ar.sceneform.rendering.ModelRenderable;
import com.google.ar.sceneform.ux.ArFragment;
import com.google.ar.sceneform.ux.BaseArFragment;
import com.google.ar.sceneform.ux.TransformableNode;

import org.pursuit.ar_wrld.modelObjects.Model;
import org.pursuit.ar_wrld.modelObjects.ModelLoader;
import org.pursuit.ar_wrld.movement.TranslatableNode;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
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
    private AnchorNode anchorNode;
    private Node startNode;
    private AnchorNode trackedNode;
    private AnchorNode endNode;
    private ObjectAnimator objectAnimation;
    private TranslatableNode translatableNode;
    private ArrayList<Vector3> vector3List;

    private Anchor anchor;
    private TransformableNode node;
    private Random random;


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

        modelLoader1 = new ModelLoader(weakReference);
        anchorNode = new AnchorNode();
        anchorNode.setWorldPosition(new Vector3(0, 0, -.500f));
        modelLoader1.loadModel(anchorNode.getAnchor(), Uri.parse("Alien_01.sfb"));
//        moveAndy();


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
            public void onFinish() {
                modelLoader1.loadModel(anchorNode.getAnchor(), Uri.parse("Alien_01.sfb"));
            public void onTimerFinish() {
                loadModel(anchorNode.getAnchor(), Uri.parse("andy.sfb"));
                numOfAliensTv.setText(String.valueOf(numOfModels));


                Toast.makeText(MainActivity.this, "Model Loaded", Toast.LENGTH_SHORT).show();
                alienSpawnRate.startTimer();
            }
        };

        alienSpawnRate.startTimer();
    }


//    private void addObject(Uri model) {
//        Frame frame = arFragment.getArSceneView().getArFrame();
//        Point pt = getScreenCenter();
//        List<HitResult> hits;
//        if (frame != null) {
//            hits = frame.hitTest(pt.x, pt.y);
//            for (HitResult hit : hits) {
//                Trackable trackable = hit.getTrackable();
//                if (trackable instanceof Plane &&
//                        ((Plane) trackable).isPoseInPolygon(hit.getHitPose())) {
//                    modelLoader1.loadModel(hit.createAnchor(), model);
//                    break;
//
//                }
//            }
//        }
//    }

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

//    

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
        anchorNode = new AnchorNode();
        node = new TransformableNode(arFragment.getTransformationSystem());
        node.setRenderable(renderable);
        node.setParent(anchorNode);
        vector.set(randomCoordinates(true), randomCoordinates(false), -.7f);

        node.setLocalPosition(vector);
        ModelLoader modelLoader = new ModelLoader(2);

        arFragment.getArSceneView().getScene().addChild(anchorNode);
        node.getScaleController().setMinScale(0.5f);
        node.getScaleController().setMaxScale(3.0f);
        objectMovement();
        // objectAnimation.start();

        setNodeListener(node, anchorNode, modelLoader1);
        //node.setWorldPosition(new Vector3(node.getRight().x+0.04f,0.0f,-0.00f));
        TranslatableNode translatableNode = new TranslatableNode(node);
        translatableNode.pullUp();
        playAnimation(renderable);

        // setNodeListener(node2, anchorNode, modelLoader3);

    }

    public void onException(Throwable throwable) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(throwable.getMessage())
                .setTitle("Codelab error!");
        AlertDialog dialog = builder.create();
        dialog.show();
        return;
    }

    @SuppressLint("StringFormatInvalid")
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
                stringPlaceHolder = getString(R.string.score_text, scoreNumber);
                scorekeepingTv.setText(stringPlaceHolder);
                sharedPreferences.edit().putInt(GameInformation.USER_SCORE_KEY, scoreNumber).apply();
                Log.d(TAG, "setNodeListener: " + stringPlaceHolder);
                Log.d(TAG, "setNodeListener: " + scorekeepingTv.getText().toString());
                Toast.makeText(this, "Enemy Eliminated", Toast.LENGTH_SHORT).show();
                numOfAliensTv.setText(String.valueOf(numOfModels));
            }
        }));
        Log.d(TAG, "setNodeListener: After if statement" + modelLoader.getNumofLivesModel0());
        node.select();
    }

    /**
     * Use for easy plane detection
     */
//    public void changetexture() {
//        Texture.Sampler sampler =
//                Texture.Sampler.builder()
//                        .setMinFilter(Texture.Sampler.MinFilter.LINEAR)
//                        .setWrapMode(Texture.Sampler.WrapMode.REPEAT)
//                        .build();
//        Texture.builder()
//                .setSource(this, R.drawable.testing_carpet_texture)
//                .setSampler(sampler)
//                .build()
//                .thenAccept(texture -> {
//                    arFragment.getArSceneView().getPlaneRenderer()
//                            .getMaterial().thenAccept(material ->
//                            material.setTexture(PlaneRenderer.MATERIAL_TEXTURE, texture));
//                });
//    }
   
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

    private void moveAndy(AnchorNode anchorNode) {
        random = new Random();
        int cooridanateOption = random.nextInt(10) + 1;
        if (numOfModels < 1) {
            return;
        }
        // Create the Anchor.
        // Create the starting position.
        if (startNode == null) {
            // startNode = startNode.setParent(anchorNode);
            startNode = node.getParent();
            node.setParent(startNode);


            // Create the transformable andy and add it to the anchor.
            endNode = new AnchorNode(anchorNode.getAnchor());
            endNode.setParent(startNode);
            endNode.setWorldPosition(new Vector3(vector3List.get(cooridanateOption)));
        } else {
            // Create the end position and start the animation.
//            endNode = new AnchorNode(anchorNode.getAnchor());
//            endNode.setParent(arFragment.getArSceneView().getScene());
            //  endNode.setWorldPosition(new Vector3(.799f, 0.78f, -.700f));
            //endNode.setParent(arFragment.getArSceneView().getScene());
            objectMovement();
        }
    }

    private void objectMovement() {
        randomVector3Array();
        random = new Random();
        int coordinateOption = random.nextInt(10) + 1;

        objectAnimation = new ObjectAnimator();
        objectAnimation.setAutoCancel(true);
        objectAnimation.setTarget(node);
        endNode = new AnchorNode();
        endNode.setWorldPosition(new Vector3(randomVector3Array().get(coordinateOption)));
        // All the positions should be world positions
        // The first position is the start, and the second is the end.
        objectAnimation.setObjectValues(node.getWorldPosition(), endNode.getWorldPosition());

        // Use setWorldPosition to position andy.
        objectAnimation.setPropertyName("worldPosition");

        // The Vector3Evaluator is used to evaluator 2 vector3 and return the next
        // vector3.  The default is to use lerp.
        objectAnimation.setEvaluator(new Vector3Evaluator());
        // This makes the animation linear (smooth and uniform).
        objectAnimation.setInterpolator(new LinearInterpolator());
        // Duration in ms of the animation.
        objectAnimation.setDuration(5000);
        objectAnimation.start();


    }
    public void goToResultPage() {
//        Intent goToResultPageIntent = new Intent(MainActivity.this, ResultPage.class);
//        startActivity(goToResultPageIntent);
    }

    public float randomCoordinates(boolean isX) {
        random = new Random();
        if (isX) return random.nextFloat() - .700f;
        return random.nextFloat() - .500f;
    }

    private ArrayList<Vector3> randomVector3Array() {
        random = new Random();
        vector3List = new ArrayList<>();
        float xVector ;
        float yVector;
        float zVector ;
        for (int i = 0; i < 12; i++) {

            xVector = random.nextFloat();
            yVector = random.nextFloat();
            zVector = random.nextFloat();


            vector3List.add(new Vector3(xVector, yVector, zVector));
        }

        return vector3List;
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
