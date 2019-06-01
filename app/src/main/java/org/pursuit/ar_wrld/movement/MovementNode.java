package org.pursuit.ar_wrld.movement;

import android.animation.ObjectAnimator;

import android.view.MotionEvent;
import android.view.animation.LinearInterpolator;

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

public class MovementNode extends AnchorNode implements Node.OnTapListener {

    public MovementNode() {
        this.node = node;
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

    //    private Vector3 up;
//    private Vector3 down;
//    private Vector3 forward;
//    private Vector3 left;
//    private Vector3 right;
//    private Vector3 back;
//    private AudioLoader audioLoader;
//    private ArrayList<Vector3> vector3List;
    private Long speedMultiplier = 6000L;


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
        setUpLights();
    }

    public void addOffset(float x, float y, float z) {
        float posX = this.node.getLocalPosition().x + x;
        float posY = this.getLocalPosition().y + y;
        float posZ = this.getLocalPosition().z + z;
        this.setLocalPosition(new Vector3(posX, posY, posZ));
    }

    public ObjectAnimator randomMovement() {

        //get nodes original coordinates
        Vector3 originalNodePosition = this.getLocalPosition();
        //set new coordinates
        Vector3 up = new Vector3(0.885f, 0.0f, -0.800f);
        Vector3 left = new Vector3(0.700f, 0.5f, -0.300f);
        Vector3 down = new Vector3(-0.5f, -0.5f, -0.5f);
        ObjectAnimator objectAnimator = new ObjectAnimator();
        objectAnimator.setAutoCancel(true);
        objectAnimator.setTarget(this);
        objectAnimator.setPropertyName("localPosition");

        //requires the setter name of what you are manipulating

        //evaluator of what values your are passing

        //set multiple coordinates to be called one after the other
        objectAnimator.setObjectValues(originalNodePosition, up, left, down, originalNodePosition);
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


    public ObjectAnimator createAnimator(boolean clockwise) {
        // Node's setLocalRotation method accepts Quaternions as parameters.
        // First, set up orientations that will animate a circle.
        Quaternion[] orientations = new Quaternion[4];
        // Rotation to apply first, to tilt its axis.
        Quaternion baseOrientation = Quaternion.axisAngle(new Vector3(1.0f, 0f, 0.0f), 0.03f);
        for (int i = 0; i < orientations.length; i++) {
            float angle = i * 360 / (orientations.length - 1);
            if (clockwise) {
                angle = 360 - angle;
            }
            Quaternion orientation = Quaternion.axisAngle(new Vector3(0.0f, 1.0f, 0.0f), 25.19f);
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


    private void getNodeCoordinates(Node node) {
//
//        float x = node.getWorldPosition().x;
//        float y = node.getWorldPosition().y;
//        float z = node.getWorldPosition().z;
//        Path path = new Path();
//        path.moveTo(x + 0, y + 0);
//        path.lineTo(x + 0.20f, y + 0.40f);
//        path.lineTo(x + 0.40f, y + 0.90f);
//        ObjectAnimator objectAnimator =
//                ObjectAnimator.ofObject(node, "transformationSystem", new Vector3Evaluator(), path);
//        objectAnimator.setDuration(3000);
//        objectAnimator.start();
//
//
    }

    private void collisionSpace() {

        node.getCollisionShape();

    }

    public void speedSetting(long setSpeed) {
        speedMultiplier = setSpeed;

    }

    public void setUpLights() {
        light =
                Light.builder(Light.Type.POINT)
                        .setFalloffRadius(20f)
                        .setColor(new Color(178, 34, 34))
                        .setShadowCastingEnabled(false)
                        .setIntensity(3f)
                        .build();

        // for (int i = 0; i < 4; i++) {
        // Sets the color of and creates the light.


        // Create node and set its light.


//            RotatingNode orbit = new RotatingNode();
//            orbit.setParent(anchorNode);

        Node lightNode = new Node();
        lightNode.setParent(this);
        lightNode.setLocalPosition(this.getLocalPosition());
        lightNode.setLight(light);

    }

    public void modelBlink(/*Light receiver,*/int times, float from, float to, long inMs) {

        ObjectAnimator intensityAnimator = ObjectAnimator.ofFloat(light, "intensity", from, to);
        intensityAnimator.setTarget(this.getRenderable());
        intensityAnimator.setDuration(inMs);
        intensityAnimator.setRepeatCount(times * 2 - 1);
        intensityAnimator.setRepeatMode(ObjectAnimator.REVERSE);
        intensityAnimator.start();
    }

    @Override
    public void onTap(HitTestResult hitTestResult, MotionEvent motionEvent) {
        modelBlink(3, 0f, 100000f, 100);
    }
}