package org.pursuit.ar_wrld;

import android.content.SharedPreferences;
import android.graphics.Point;
import android.net.Uri;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.ar.core.Anchor;
import com.google.ar.core.Config;
import com.google.ar.core.Frame;
import com.google.ar.core.HitResult;
import com.google.ar.core.Plane;
import com.google.ar.core.Session;
import com.google.ar.core.Trackable;
import com.google.ar.core.TrackingState;
import com.google.ar.core.exceptions.UnavailableApkTooOldException;
import com.google.ar.core.exceptions.UnavailableArcoreNotInstalledException;
import com.google.ar.core.exceptions.UnavailableDeviceNotCompatibleException;
import com.google.ar.core.exceptions.UnavailableSdkTooOldException;
import com.google.ar.sceneform.AnchorNode;
import com.google.ar.sceneform.FrameTime;
import com.google.ar.sceneform.animation.ModelAnimator;
import com.google.ar.sceneform.math.Vector3;
import com.google.ar.sceneform.rendering.AnimationData;
import com.google.ar.sceneform.rendering.ModelRenderable;
import com.google.ar.sceneform.rendering.PlaneRenderer;
import com.google.ar.sceneform.rendering.Texture;
import com.google.ar.sceneform.ux.ArFragment;
import com.google.ar.sceneform.ux.TransformableNode;

import org.pursuit.ar_wrld.modelObjects.ModelLoader;

import java.lang.ref.WeakReference;
import java.util.Collection;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    public static final String TAG = "FINDME";
    private ArFragment arFragment;
    private ModelLoader modelLoader1;
    private ModelLoader modelLoader2;
    private ModelLoader modelLoader3;
    private WeakReference weakReference;
    private Button shootingButton;
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
        setUpAR();
        startStopTimer();


        // Possible for models to show up without touch
        modelLoader1 = new ModelLoader(weakReference);
        AnchorNode anchorNode = new AnchorNode();
        anchorNode.setWorldPosition(new Vector3(3.04f, 2.04f, 1.98f));
        modelLoader1.loadModel(anchorNode.getAnchor(), Uri.parse("andy.sfb"));

//        shootingButton.setOnClickListener(view -> {
//            Log.d(TAG, "onCreate: Shooting button pressed");
//            hitReaction();
//        });
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
//        modelLoader1 = new ModelLoader(weakReference);
//        modelLoader2 = new ModelLoader(weakReference);
//        modelLoader3 = new ModelLoader(weakReference);

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
        if (numOfModels > 0) return;
        modelLoader1 = new ModelLoader(weakReference);
        Frame frame = arFragment.getArSceneView().getArFrame();
        Collection<Plane> planes = frame.getUpdatedTrackables(Plane.class);
        for (Plane plane : planes) {
            if (plane.getTrackingState() == TrackingState.TRACKING) {
                addObject(Uri.parse("andy_dance.sfb"));
                break;
            }
        }
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
        numOfModels++;
        Frame frame = arFragment.getArSceneView().getArFrame();
        Point pt = getScreenCenter();
        List<HitResult> hits;
        if (frame != null) {
            hits = frame.hitTest(pt.x, pt.y);
            for (HitResult hit : hits) {
                Trackable trackable = hit.getTrackable();
                if (trackable instanceof Plane &&
                        ((Plane) trackable).isPoseInPolygon(hit.getHitPose())) {
                    modelLoader1.loadModel(hit.createAnchor(), model);
                    break;

                }
            }
        }
    }

    public void addNodeToScene(Anchor anchor, ModelRenderable renderable) {
        numOfModels++;
        AnchorNode anchorNode = new AnchorNode(anchor);
        TransformableNode node = new TransformableNode(arFragment.getTransformationSystem());
        node.setRenderable(renderable);
        node.setParent(anchorNode);
        node.setLocalPosition(new Vector3(0f, 0f, 0f));
//        modelLoader1 = new ModelLoader(weakReference);
        modelLoader1.setNumofLivesModel0(2);
        arFragment.getArSceneView().getScene().addChild(anchorNode);

        setNodeListener(node, anchorNode, modelLoader1);
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
            }
        }));
        Log.d(TAG, "setNodeListener: After if statement"+modelLoader.getNumofLivesModel0());
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

    public void startStopTimer(){
        if(timerRunning){
            stopTimer();
        } else {
            startTimer();
        }

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

            }
        }.start();
        timerRunning = true;
    }

    public void stopTimer(){
        countDownTimer.cancel();
        timerRunning = false;

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

}
