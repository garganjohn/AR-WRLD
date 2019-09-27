package org.pursuit.ar_wrld.util;

import android.animation.ArgbEvaluator;
import android.animation.ObjectAnimator;
import android.graphics.Color;
import android.view.animation.Animation;
import android.widget.ImageView;

import com.google.ar.sceneform.math.Vector3;
import com.google.ar.sceneform.ux.TransformableNode;


public class ModelLocationIndicator {
    private static final String TAG = "model location";
    private ObjectAnimator animator;
    private TransformableNode markerNode;

    private ImageView leftArrow;
    private ImageView rightArrow;

    public ModelLocationIndicator(ImageView left, ImageView right) {

        this.leftArrow = left;
        this.rightArrow = right;
    }

    //TODO take in vector upon opbj inst. and then decide which arrow to indicate
    public void indicate(Vector3 vector) {
        if (vector.x < 0) {
            animator = new ObjectAnimator().ofInt(leftArrow, "backgroundColor", Color.RED, Color.TRANSPARENT);
            animator.setDuration(1500);
            animator.setStartDelay(1000);
            animator.setEvaluator(new ArgbEvaluator());
            animator.setRepeatMode(Animation.REVERSE);
            animator.setRepeatCount(Animation.ABSOLUTE);
            animator.start();
        }
        if (0 < vector.x) {
            animator = new ObjectAnimator().ofInt(rightArrow, "backgroundColor", Color.RED, Color.TRANSPARENT);
            animator.setDuration(1500);
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


