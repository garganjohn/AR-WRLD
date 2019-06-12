package org.pursuit.ar_wrld.movement;

import android.animation.ObjectAnimator;
import android.content.Context;
import android.net.Uri;
import android.os.CountDownTimer;
import android.view.animation.LinearInterpolator;

import com.google.ar.sceneform.AnchorNode;
import com.google.ar.sceneform.FrameTime;
import com.google.ar.sceneform.Node;
import com.google.ar.sceneform.math.Quaternion;
import com.google.ar.sceneform.math.Vector3;
import com.google.ar.sceneform.math.Vector3Evaluator;
import com.google.ar.sceneform.rendering.Color;
import com.google.ar.sceneform.rendering.Light;
import com.google.ar.sceneform.rendering.MaterialFactory;
import com.google.ar.sceneform.rendering.ModelRenderable;
import com.google.ar.sceneform.rendering.ShapeFactory;

public class ProjectileNode extends AnchorNode {

    public ProjectileNode(Context context, Uri uri) {
        this.context = context;
        this.uri = uri;
        this.anchorNode = anchorNode;
        this.targetNode = targetNode;
    }

    private Context context;
    private String PROJECTILE_TAG = "Projectile State";
    private AnchorNode anchorNode;
    private Node targetNode;
    private Vector3 targetLocation;
    private Vector3 point1;
    private Uri uri;


    @Override
    public void onUpdate(FrameTime frameTime) {
        super.onUpdate(frameTime);
        targetLocation = this.getLocalPosition();

        //point1 = this.getLocalPosition();

    }

    @Override
    public void onTransformChange(Node node) {
        super.onTransformChange(node);
        targetLocation = getScene().getCamera().getBack();
    }

    public void launchProjectile(Node node) {
        Light light = Light.builder(Light.Type.DIRECTIONAL)
                .setColor(new Color(android.graphics.Color.MAGENTA))
                .setFalloffRadius(0f)
                .setShadowCastingEnabled(true)
                .setIntensity(45f)
                .build();
        point1 = targetLocation;
        Vector3 point2 = node.getLocalPosition();

//        final Vector3 difference = Vector3.subtract(point1, point2);
//        final Vector3 directionFromTopToBottom = difference.normalized();
//        final Quaternion rotationFromAToB =
//
//
//                Quaternion.lookRotation(directionFromTopToBottom, Vector3.up());
//        MaterialFactory.makeOpaqueWithColor(context, new Color(android.graphics.Color.MAGENTA))
//                .thenAccept(
//                        material -> {
/* Then, create a rectangular prism, using ShapeFactory.makeCube() and use the difference vector
       to extend to the necessary length.  */
//                            ModelRenderable model = ShapeFactory.makeCube(
//                                    new Vector3(.01f, .01f, difference.length()),
//                                    Vector3.zero(), material);
        ModelRenderable.builder()
                .setSource(context, uri)
                .build()
                .thenAccept(modelRenderable -> {
                            this.setLight(light);
                            this.setRenderable(modelRenderable);
                            modelBlink(light, 2, 0f, 500f, 500);
                            this.setLocalScale(new Vector3(1f, 1f, 1f));
                            //this.setWorldRotation(rotationFromAToB);
                        });


        ObjectAnimator objectAnimator = new ObjectAnimator();

        objectAnimator.setAutoCancel(true);
        objectAnimator.setTarget(this);
        objectAnimator.setPropertyName("localPosition");
        objectAnimator.setObjectValues(point1, point2);

        objectAnimator.setEvaluator(new Vector3Evaluator());
        //animation happens forever
//        objectAnimator.setRepeatCount(Obj);
//        //animation is the called in reverse
//        objectAnimator.setRepeatMode(ObjectAnimator.REVERSE);
        // This makes the animation linear (smooth and uniform).
        objectAnimator.setInterpolator(new LinearInterpolator());
        // Duration in ms of the animation.
        objectAnimator.setDuration(500);

        objectAnimator.start();


        new CountDownTimer(450, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {

            }

            @Override
            public void onFinish() {
                setRenderable(null);
            }
        }.start();


    }

    public void modelBlink(Light receiver, int times, float from, float to, long inMs) {
        ObjectAnimator intensityAnimator = ObjectAnimator.ofFloat(receiver, "intensity", from, to);
        intensityAnimator.setDuration(inMs);
        intensityAnimator.setRepeatCount(times * 2 - 1);
        intensityAnimator.setRepeatMode(ObjectAnimator.REVERSE);
        intensityAnimator.start();
    }


}
