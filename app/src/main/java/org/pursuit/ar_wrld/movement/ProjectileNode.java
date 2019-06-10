package org.pursuit.ar_wrld.movement;

import android.animation.ObjectAnimator;
import android.content.Context;
import android.net.Uri;
import android.util.Log;
import android.view.animation.LinearInterpolator;

import com.google.ar.sceneform.AnchorNode;
import com.google.ar.sceneform.FrameTime;
import com.google.ar.sceneform.Node;
import com.google.ar.sceneform.math.Vector3;
import com.google.ar.sceneform.math.Vector3Evaluator;
import com.google.ar.sceneform.rendering.ModelRenderable;

public class ProjectileNode extends Node {

    public ProjectileNode(Context context) {
        this.context = context;
        this.anchorNode = anchorNode;
        this.targetNode = targetNode;
    }

    private Context context;
    private String PROJECTILE_TAG = "Projectile State";
    private AnchorNode anchorNode;
    private Node targetNode;
    private Vector3 targetLocation;
    private Vector3 projectileLocation;


    @Override
    public void onUpdate(FrameTime frameTime) {
        super.onUpdate(frameTime);

//        targetLocation = targetNode.getLocalPosition();
        projectileLocation = this.getLocalPosition();

    }

    public void setUpRenderable(Uri uri) {
        ModelRenderable.builder()
                .setSource(context, uri)
                .build()
                .thenAccept(this::setRenderable)
                .exceptionally(null);


    }

    public void launchProjectile(Node node) {

        Vector3 nopdePosition = node.getLocalPosition();
        ObjectAnimator objectAnimator = new ObjectAnimator();

        objectAnimator.setAutoCancel(true);
        objectAnimator.setTarget(this);
        objectAnimator.setPropertyName("localPosition");
        objectAnimator.setObjectValues(projectileLocation, nopdePosition);

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


    }


}
