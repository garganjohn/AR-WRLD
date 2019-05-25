package org.pursuit.ar_wrld.movement;

import android.animation.ObjectAnimator;
import android.graphics.Path;
import android.view.animation.LinearInterpolator;

import com.google.ar.sceneform.AnchorNode;
import com.google.ar.sceneform.FrameTime;
import com.google.ar.sceneform.Node;
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

    public ObjectAnimator getObjectAnimator() {
        return objectAnimator;
    }

    @Override
    public void onUpdate(FrameTime frameTime) {
        super.onUpdate(frameTime);
        if (objectAnimator == null) {
            return;

        }
        upMovment();
    }

    private ArrayList<Vector3> randomVector3Array() {
        Random random = new Random();
        vector3List = new ArrayList<>();
        up = node.getUp();
        down = node.getDown();
        forward = node.getForward();

        float xVector;
        float yVector;
        float zVector;
        for (int i = 0; i < 12; i++) {

            xVector = random.nextFloat();
            yVector = random.nextFloat();
            zVector = random.nextFloat();


            vector3List.add(new Vector3(xVector, yVector, zVector));
        }

        return vector3List;
    }

//    private ObjectAnimator startRandomMovement() {
//        //get nodes original coordinates
//        Vector3 originalNodePosition = node.getWorldPosition();
//
//        //RandomArray of Coordinates are instantiated
//        randomVector3Array();
//        Random random = new Random();
//        int coordinateOption = random.nextInt(10) + 1;
//        Vector3 previousPosition = node.getWorldPosition();
//        //Implementing a path
//        Path path = new Path();
//        path.addCircle(0.4f, 0.03f, 0.0f, Path.Direction.CCW);
//
//        ObjectAnimator objectAnimator = new ObjectAnimator();
//        objectAnimator.setAutoCancel(true);
//        objectAnimator.setTarget(node);
//        objectAnimator.ofObject(node, "worldPosition", new Vector3Evaluator(), path);
//        AnchorNode endNode = new AnchorNode();
//        endNode.setWorldPosition(new Vector3(randomVector3Array().get(coordinateOption)));
//        // All the positions should be world positions
//        // The first position is the start, and the second is the end.
//        objectAnimator.setObjectValues(node.getWorldPosition(), endNode.getWorldPosition());
//        /*long duration = objectAnimation.getTotalDuration();
//         * create parameters that account for when the animation is done and then start a new one */
//
//        // Use setWorldPosition to position andy.
//        objectAnimator.setPropertyName("worldPosition");
//
//        // The Vector3Evaluator is used to evaluator 2 vector3 and return the next
//        // vector3.  The default is to use lerp.
//        objectAnimator.setEvaluator(new Vector3Evaluator());
//        objectAnimator.setRepeatCount(ObjectAnimator.INFINITE);
//        objectAnimator.setRepeatMode(ObjectAnimator.RESTART);
//        // This makes the animation linear (smooth and uniform).
//        objectAnimator.setInterpolator(new LinearInterpolator());
//        // Duration in ms of the animation.
//        objectAnimator.setDuration(5000);
//        objectAnimator.start();
//
//        return objectAnimator;
//
//
//    }

//    private void startAnimatio() {
//        if (objectAnimator != null) {
//            return;
//        }
//        objectAnimator = startRandomMovement(node);
//    }

    public void addOffset(float x, float y, float z) {
        float posX = this.getLocalPosition().x + x;
        float posY = this.getLocalPosition().y + y;
        float posZ = this.getLocalPosition().z + z;
        this.setLocalPosition(new Vector3(posX, posY, posZ));
    }

   public ObjectAnimator upMovment() {
        //get nodes original coordinates
        Vector3 originalNodePosition = this.getLocalPosition();
        Vector3 up = new Vector3(0.885f, 0.0f, -0.800f);

        //RandomArray of Coordinates are instantiated
//        randomVector3Array();
//        Random random = new Random();
//        int coordinateOption = random.nextInt(10) + 1;
//        Vector3 previousPosition = node.getWorldPosition();
//        //Implementing a path
//        Path path = new Path();
//        path.addCircle(0.4f, 0.03f, 0.0f, Path.Direction.CCW);

        ObjectAnimator objectAnimator = new ObjectAnimator();
        objectAnimator.setAutoCancel(true);
        objectAnimator.setTarget(this);
        objectAnimator.setPropertyName("localPosition");
        // objectAnimator.ofObject(this, "localPosition", new Vector3Evaluator(), up);
        // AnchorNode endNode = new AnchorNode();
        // All the positions should be world positions
        // The first position is the start, and the second is the end.e
        objectAnimator.setEvaluator(new Vector3Evaluator());
        objectAnimator.setObjectValues(originalNodePosition, up);
        /*long duration = objectAnimation.getTotalDuration();
         * create parameters that account for when the animation is done and then start a new one */

        // Use setWorldPosition to position andy.
//        objectAnimator.setPropertyName("worldPosition");

        // The Vector3Evaluator is used to evaluator 2 vector3 and return the next
        // vector3.  The default is to use lerp.
        objectAnimator.setEvaluator(new Vector3Evaluator());
        objectAnimator.setRepeatCount(ObjectAnimator.INFINITE);
        objectAnimator.setRepeatMode(ObjectAnimator.RESTART);
        // This makes the animation linear (smooth and uniform).
        objectAnimator.setInterpolator(new LinearInterpolator());
        // Duration in ms of the animation.
        objectAnimator.setDuration(5000);
        objectAnimator.start();

        return objectAnimator;


    }
}