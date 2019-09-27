package org.pursuit.ar_wrld.movement;


import android.animation.ObjectAnimator;
import android.content.Context;
import android.net.Uri;
import android.os.CountDownTimer;
import android.view.animation.LinearInterpolator;
import com.google.ar.sceneform.AnchorNode;
import com.google.ar.sceneform.Camera;
import com.google.ar.sceneform.FrameTime;
import com.google.ar.sceneform.Node;
import com.google.ar.sceneform.math.Vector3;
import com.google.ar.sceneform.math.Vector3Evaluator;
import com.google.ar.sceneform.rendering.Color;
import com.google.ar.sceneform.rendering.Light;
import com.google.ar.sceneform.rendering.ModelRenderable;
import com.google.ar.sceneform.ux.ArFragment;

public class Projectiles extends AnchorNode {

    public Projectiles(Context context, Uri uri, ArFragment arFragment) {
        this.context = context;
        this.uri = uri;
        this.anchorNode = anchorNode;
        this.targetNode = targetNode;
        this.arFragment = arFragment;

    }

    private ArFragment arFragment;
    private Context context;
    private String PROJECTILE_TAG = "Projectile State";
    private AnchorNode anchorNode;
    private Node targetNode;
    private Vector3 targetLocation;
    private Vector3 point1;
    private Uri uri;
    private Uri uri2;
    private Camera camera;
    private  Vector3 position;
    @Override
    public void onActivate() {
        super.onActivate();
        point1 = arFragment.getArSceneView().getScene().getCamera().getBack();
        setLocalPosition(arFragment.getArSceneView().getScene().getCamera().getLocalPosition());

    }

    @Override
    public void onUpdate(FrameTime frameTime) {
        super.onUpdate(frameTime);
        targetLocation = this.getLocalPosition();
        point1 = arFragment.getArSceneView().getScene().getCamera().getBack();

    }

    @Override
    public void onTransformChange(Node node) {
        super.onTransformChange(node);
        targetLocation = this.getLocalPosition();
    }

    public void launchProjectile(Node node) {
        setRenderable();
        point1 = arFragment.getArSceneView().getScene().getCamera().getLocalPosition();
        Vector3 startPoint = getWorldPosition();
        Vector3 point2 = node.getWorldPosition();
        setLookDirection(point2);
        ObjectAnimator objectAnimator = new ObjectAnimator();

        objectAnimator.setAutoCancel(true);
        objectAnimator.setTarget(this);
        objectAnimator.setPropertyName("worldPosition");
        objectAnimator.setObjectValues(point1, point2);

        objectAnimator.setEvaluator(new Vector3Evaluator());
        objectAnimator.setInterpolator(new LinearInterpolator());
        objectAnimator.clone();
        objectAnimator.setDuration(350);

        objectAnimator.start();

        new CountDownTimer(400, 450) {
            @Override
            public void onTick(long millisUntilFinished) {

            }

            @Override
            public void onFinish() {

                setRenderable(null);
            }
        }.start();
    }

    public void modelBlink (Light receiver,int times, float from, float to,
                            long inMs){
        ObjectAnimator intensityAnimator = ObjectAnimator.ofFloat(receiver, "intensity", from, to);
        intensityAnimator.setDuration(inMs);
        intensityAnimator.setRepeatCount(times * 2 - 1);
        intensityAnimator.setRepeatMode(ObjectAnimator.REVERSE);
        intensityAnimator.start();
    }

    public void setRenderable () {
        Light light = Light.builder(Light.Type.POINT)
                .setColor(new Color(android.graphics.Color.GREEN))
                .setFalloffRadius(0.5f)
                .setShadowCastingEnabled(true)
                .setIntensity(45f)
                .build();


        ModelRenderable.builder()
                .setSource(context, uri)
                .build()
                .thenAccept(modelRenderable -> {

                    setLight(light);
                    setRenderable(modelRenderable);
                    modelBlink(light, 2, 0f, 500f, 500);
                    setLocalScale(new Vector3(.225f, .225f, .225f));
                });
    }

}

