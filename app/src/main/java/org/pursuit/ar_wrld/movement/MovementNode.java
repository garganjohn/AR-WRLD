package org.pursuit.ar_wrld.movement;

import android.animation.ObjectAnimator;

import android.view.animation.LinearInterpolator;

import com.google.ar.sceneform.AnchorNode;
import com.google.ar.sceneform.FrameTime;
import com.google.ar.sceneform.Node;
import com.google.ar.sceneform.math.Quaternion;
import com.google.ar.sceneform.math.QuaternionEvaluator;
import com.google.ar.sceneform.math.Vector3;
import com.google.ar.sceneform.math.Vector3Evaluator;

import java.util.ArrayList;
import java.util.Random;

public class MovementNode extends AnchorNode {
    public MovementNode(ObjectAnimator objectAnimator) {
        this.objectAnimator = objectAnimator;
        this.node = getNode();
    }

    private ObjectAnimator objectAnimator;

    private Node node;
    private Vector3 up;
    private Vector3 down;
    private Vector3 forward;
    private Vector3 left;
    private Vector3 right;
    private Vector3 back;
    private ArrayList<Vector3> vector3List;

    public Node getNode() {
        return node;
    }

    @Override
    public void onUpdate(FrameTime frameTime) {
        super.onUpdate(frameTime);
        if (objectAnimator == null) {
            return;

        }
        randomMovement();
        //createAnimator(true);
    }

    public void addOffset(float x, float y, float z) {
        float posX = this.node.getLocalPosition().x + x;
        float posY = this.getLocalPosition().y + y;
        float posZ = this.getLocalPosition().z + z;
        this.setLocalPosition(new Vector3(posX, posY, posZ));
    }

    public ObjectAnimator randomMovement() {
        //get nodes original coordinates
        Vector3 originalNodePosition = this.getWorldPosition();
        //set new coordinates
        Vector3 up = new Vector3(0.885f, 0.0f, -0.800f);
        Vector3 left = new Vector3(0.700f, 0.5f, -0.300f);
        Vector3 down = new Vector3(-0.5f, -0.5f, -0.5f);
       objectAnimator = new ObjectAnimator();
        objectAnimator.setAutoCancel(true);
        objectAnimator.setTarget(this);
        //requires the setter name of what you are manipulating
        objectAnimator.setPropertyName("worldPosition");
        //evaluator of what values your are passing
        objectAnimator.setEvaluator(new Vector3Evaluator());
        //set multiple coordinates to be called one after the other
        objectAnimator.setObjectValues(originalNodePosition, up, left, down, originalNodePosition);
        //animation happens forever
        objectAnimator.setRepeatCount(ObjectAnimator.INFINITE);
        //animation is the called in reverse
        objectAnimator.setRepeatMode(ObjectAnimator.REVERSE);
        // This makes the animation linear (smooth and uniform).
        objectAnimator.setInterpolator(new LinearInterpolator());
        // Duration in ms of the animation.
        objectAnimator.setDuration(6000);

        objectAnimator.start();

        return objectAnimator;


    }


    public ObjectAnimator createAnimator(boolean clockwise) {
        // Node's setLocalRotation method accepts Quaternions as parameters.
        // First, set up orientations that will animate a circle.
        Quaternion[] orientations = new Quaternion[4];
        // Rotation to apply first, to tilt its axis.
        Quaternion baseOrientation = Quaternion.axisAngle(new Vector3(1.0f, 0f, 0.0f),0.03f );
        for (int i = 0; i < orientations.length; i++) {
            float angle = i * 360 / (orientations.length - 1);
            if (clockwise) {
                angle = 360 - angle;
            }
            Quaternion orientation = Quaternion.axisAngle(new Vector3(0.0f, 1.0f, 0.0f),25.19f );
            orientations[i] = Quaternion.multiply(baseOrientation, orientation);
        }

        objectAnimator = new ObjectAnimator();
        // Cast to Object[] to make sure the varargs overload is called.
        objectAnimator.setObjectValues((Object[]) orientations);

        // Next, give it the localRotation property.
        objectAnimator.setPropertyName("localRotation");

        // Use Sceneform's QuaternionEvaluator.
        objectAnimator.setEvaluator(new QuaternionEvaluator());

        //  Allow objectAnimator to repeat forever
        objectAnimator.setRepeatCount(ObjectAnimator.INFINITE);
        objectAnimator.setRepeatMode(ObjectAnimator.RESTART);
        objectAnimator.setInterpolator(new LinearInterpolator());
        objectAnimator.setAutoCancel(true);

        return objectAnimator;

    }

}