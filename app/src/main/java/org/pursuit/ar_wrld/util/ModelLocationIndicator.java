package org.pursuit.ar_wrld.util;

import android.animation.ArgbEvaluator;
import android.animation.ObjectAnimator;
import android.graphics.Color;
import android.util.Log;
import android.view.animation.Animation;
import android.widget.ImageView;

import com.google.ar.sceneform.math.Vector3;
import com.google.ar.sceneform.rendering.Renderable;
import com.google.ar.sceneform.rendering.ViewRenderable;
import com.google.ar.sceneform.ux.ArFragment;
import com.google.ar.sceneform.ux.TransformableNode;

import org.pursuit.ar_wrld.R;

public class ModelLocationIndicator {
    private static final String TAG = "model location";
    private Vector3 modelNodePosition;
    private ObjectAnimator animator;
    private TransformableNode modelNode;
    private TransformableNode markerNode;
    private ArFragment arFragment;
    private ImageView upArrow;
    private ImageView downArrow;
    private ImageView leftArrow;
    private ImageView rightArrow;

    public ModelLocationIndicator(ImageView up, ImageView down, ImageView left, ImageView right) {
        this.upArrow = up;
        this.downArrow = down;
        this.leftArrow = left;
        this.rightArrow = right;
    }

    public void pickMarkers() {
        float holder = modelNodePosition.y + 0.5f;
        Vector3 markerNodePosition = new Vector3(modelNodePosition.x, holder, modelNodePosition.z);
        markerNode = new TransformableNode(arFragment.getTransformationSystem());
        markerNode.setParent(modelNode);
        markerNode.setLocalPosition(markerNodePosition);
        Log.d(TAG, "addMarkerToModel: ");
    }


    //TODO take in vector upon opbj inst. and then decide which arrow to indicate
    public void indicate(Vector3 vector) {
        if (vector.z < 0) {
            animator = new ObjectAnimator().ofInt(downArrow, "backgroundColor", Color.RED, Color.BLUE);
            animator.setDuration(500);
            animator.setStartDelay(1000);
            animator.setEvaluator(new ArgbEvaluator());
            animator.setRepeatMode(Animation.REVERSE);
            animator.setRepeatCount(Animation.ABSOLUTE);
            animator.start();
        }
        if (vector.x < 0) {
            animator = new ObjectAnimator().ofInt(leftArrow, "backgroundColor", Color.RED, Color.BLUE);
            animator.setDuration(500);
            animator.setStartDelay(1000);
            animator.setEvaluator(new ArgbEvaluator());
            animator.setRepeatMode(Animation.REVERSE);
            animator.setRepeatCount(Animation.ABSOLUTE);
            animator.start();
        }
        if (0 < vector.x) {
            animator = new ObjectAnimator().ofInt(rightArrow, "backgroundColor", Color.RED, Color.BLUE);
            animator.setDuration(500);
            animator.setStartDelay(1000);
            animator.setEvaluator(new ArgbEvaluator());
            animator.setRepeatMode(Animation.REVERSE);
            animator.setRepeatCount(Animation.ABSOLUTE);
            animator.start();
        }
    }
//TODO cancel and reset arrow that's blinking
    public void cancelAnimator() {
        animator.cancel();
    }


}

