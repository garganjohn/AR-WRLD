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
    private void indicate(Vector3 vector) {

        ObjectAnimator animator = new ObjectAnimator().ofInt(imageView, "backgroundColor", Color.RED, Color.BLUE);
        animator.setDuration(1500);
        animator.setEvaluator(new ArgbEvaluator());
        animator.setRepeatMode(Animation.REVERSE);
        animator.setRepeatCount(Animation.INFINITE);
        animator.start();
    }


}

