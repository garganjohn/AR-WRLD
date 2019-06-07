package org.pursuit.ar_wrld.movement;

import android.animation.ObjectAnimator;

import android.view.MotionEvent;
import android.view.animation.LinearInterpolator;
import android.widget.Toast;

import com.google.ar.core.Anchor;
import com.google.ar.sceneform.AnchorNode;
import com.google.ar.sceneform.ArSceneView;
import com.google.ar.sceneform.FrameTime;
import com.google.ar.sceneform.HitTestResult;
import com.google.ar.sceneform.Node;
import com.google.ar.sceneform.math.Quaternion;
import com.google.ar.sceneform.math.QuaternionEvaluator;
import com.google.ar.sceneform.math.Vector3;
import com.google.ar.sceneform.math.Vector3Evaluator;
import com.google.ar.sceneform.rendering.Color;
import com.google.ar.sceneform.rendering.Light;

import org.pursuit.ar_wrld.Effects.AudioLoader;

import java.util.ArrayList;
import java.util.Random;

public class MovementNode extends AnchorNode {

    public MovementNode(Anchor anchor) {
        super(anchor);
    }

    private ObjectAnimator objectAnimator;
    private Node node;
    private ModelSpeed modelSpeed;
    private Light light;

    public ModelSpeed getModelSpeed() {
        return modelSpeed;
    }

    public void setModelSpeed(ModelSpeed modelSpeed) {
        this.modelSpeed = modelSpeed;
    }

    private ArrayList<Vector3> vector3List;
    private Long speedMultiplier = 10000L;


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

    }

    public void addOffset(float x, float y, float z) {
        float posX = this.node.getLocalPosition().x + x;
        float posY = this.getLocalPosition().y + y;
        float posZ = this.getLocalPosition().z + z;
        this.setLocalPosition(new Vector3(posX, posY, posZ));
    }

    public ObjectAnimator randomMovement() {
        randomVectors();
        Vector3 originalNodePosition = this.getLocalPosition();

        Vector3 postion = getRandomElement(vector3List);

        Quaternion[] orientations = new Quaternion[4];
        // Rotation to apply first, to tilt its axis.
        Quaternion baseOrientation = Quaternion.axisAngle(new Vector3(1.0f, 0f, 0.0f), 5f);
        for (int i = 0; i < orientations.length; i++) {
            float angle = i * 360 / (orientations.length - 1);
        }
//            if (clockwise) {
//                angle = 360 - angle;
//            }
//            Quaternion orientation = Quaternion.axisAngle(new Vector3(0.0f, 1.0f, 0.0f), angle);
//            orientations[i] = Quaternion.multiply(baseOrientation, orientation);
//        }

        //get nodes original coordinates

        //set new coordinates
        Vector3 up = Vector3.up();
       // Vector3 up = new Vector3(0.885f, 0.0f, -0.800f);
        Vector3 left = new Vector3(0.700f, 0.5f, -0.300f);
        Vector3 down = Vector3.down();
       // Vector3 vector31 = Vecto
        Vector3 cross = Vector3.cross(originalNodePosition,down);


        ObjectAnimator objectAnimator = new ObjectAnimator();

        objectAnimator.setAutoCancel(true);
        objectAnimator.setTarget(this);
        objectAnimator.setPropertyName("localPosition");

        // Use Sceneform's QuaternionEvaluator.



        //requires the setter name of what you are manipulating

        //evaluator of what values your are passing

        //set multiple coordinates to be called one after the other
        objectAnimator.setObjectValues(originalNodePosition, up, left, down, cross);
        objectAnimator.setEvaluator(new Vector3Evaluator());

        //animation happens forever
        objectAnimator.setRepeatCount(ObjectAnimator.INFINITE);
        //animation is the called in reverse
        objectAnimator.setRepeatMode(ObjectAnimator.REVERSE);
        // This makes the animation linear (smooth and uniform).
        objectAnimator.setInterpolator(new LinearInterpolator());
        // Duration in ms of the animation.
        objectAnimator.setDuration(speedMultiplier);

        objectAnimator.start();

        return objectAnimator;


    }


    private ArrayList<Vector3> randomVectors() {
        Random rand = new Random();


        vector3List = new ArrayList<>();
        float xPos;
        float yPos;
        float zPos;

        for (int i = 0; i < 20; i++) {
            xPos = rand.nextFloat();
            yPos = rand.nextFloat() * (0.885f - 0.5f) + 0.885f;
            zPos = rand.nextFloat() * (0.300f - 0.800f) - 0.800f;
            Vector3 vectors = new Vector3(xPos, yPos, zPos);
            vector3List.add(vectors);

//            for (int j = 0; j < i; j++) {
//                yPos = rand.nextFloat();
//                for (int k = 0; k < i; k++) {
//                    zPos = rand.nextFloat();
//
//                }
//
//            }


        }
        return vector3List;

    }

    private void collisionSpace() {

        node.getCollisionShape();

    }

    public void speedSetting(long setSpeed) {
        speedMultiplier = setSpeed;

    }

    public Vector3 getRandomElement(ArrayList<Vector3> list) {
        Random rand = new Random();
        return list.get(rand.nextInt(list.size()));
    }
}
