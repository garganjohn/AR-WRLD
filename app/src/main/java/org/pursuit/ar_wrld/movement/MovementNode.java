package org.pursuit.ar_wrld.movement;

import android.animation.ObjectAnimator;
import android.animation.TimeInterpolator;
import android.animation.TypeEvaluator;
import android.graphics.Path;
import android.view.animation.LinearInterpolator;

import com.google.ar.core.Anchor;
import com.google.ar.sceneform.AnchorNode;
import com.google.ar.sceneform.FrameTime;
import com.google.ar.sceneform.Node;
import com.google.ar.sceneform.math.Quaternion;
import com.google.ar.sceneform.math.Vector3;
import com.google.ar.sceneform.math.Vector3Evaluator;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

public class MovementNode extends Node {
    public MovementNode(ObjectAnimator objectAnimator, AnchorNode node) {
        this.objectAnimator = objectAnimator;
        this.node = node;
    }

    private ObjectAnimator objectAnimator;
    private AnchorNode node;
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

        upMovement();
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
//
//        Vector3[] vector3s = new Vector3[4];
//
//
//    }


    public void addOffset(float x, float y, float z) {
        float posX = this.getLocalPosition().x + x;
        float posY = this.getLocalPosition().y + y;
        float posZ = this.getLocalPosition().z + z;
        this.setLocalPosition(new Vector3(posX, posY, posZ));
    }

    public ObjectAnimator downMovement() {
        Vector3 originalNodePosition = this.getLocalPosition();
        down = new Vector3(-.07f, 0.08f, -734f);
        objectAnimator = new ObjectAnimator();


        ObjectAnimator objectAnimator = new ObjectAnimator();
        objectAnimator.setTarget(this);
        objectAnimator.setPropertyName("localPosition");
        objectAnimator.setAutoCancel(true);
        objectAnimator.setEvaluator(new Vector3Evaluator());
        objectAnimator.setObjectValues(originalNodePosition, down);
        objectAnimator.setEvaluator(new Vector3Evaluator());
//        objectAnimator.setRepeatCount(ObjectAnimator.INFINITE);
//        objectAnimator.setRepeatMode(ObjectAnimator.RESTART);
        // This makes the animation linear (smooth and uniform).
        objectAnimator.setInterpolator(new LinearInterpolator());
        // Duration in ms of the animation.
        objectAnimator.setDuration(5000);
        objectAnimator.start();


        return objectAnimator;
    }

    public ObjectAnimator upMovement() {
        //get nodes original coordinates
      //  randomVector3Array();
        //Random random = new Random();
        //int rnCoord = random.nextInt(10) + 1;
        down = new Vector3(-.07f, 0.08f, -734f);

        Vector3 up = new Vector3(0.885f, 0.0f, -0.800f);
       // down = randomVector3Array().get(rnCoord);
        Vector3 originalNodePosition = this.node.getLocalPosition();

        ObjectAnimator objectAnimator = new ObjectAnimator();
        objectAnimator.setAutoCancel(true);
        objectAnimator.setTarget(this.node);
        objectAnimator.setPropertyName("localPosition");

        objectAnimator.setObjectValues(originalNodePosition, up);
        objectAnimator.setEvaluator(new Vector3Evaluator());
//        objectAnimator.setRepeatCount(ObjectAnimator.INFINITE);
//        objectAnimator.setRepeatMode(ObjectAnimator.RESTART);
        objectAnimator.setInterpolator(new LinearInterpolator());
        objectAnimator.setDuration(5000);
        objectAnimator.start();

        return objectAnimator;


    }


    private final ObjectAnimator localPositionAnimator(Object... values) {
        ObjectAnimator var2 = new ObjectAnimator();
        var2.setTarget(this);
        var2.setPropertyName("localPosition");
        var2.setDuration(250L);
        var2.setInterpolator((TimeInterpolator) (new LinearInterpolator()));
        var2.setAutoCancel(true);
        var2.setObjectValues(Arrays.copyOf(values, values.length));
        var2.setEvaluator((TypeEvaluator) (new VectorEvaluator()));
        return var2;
    }

}