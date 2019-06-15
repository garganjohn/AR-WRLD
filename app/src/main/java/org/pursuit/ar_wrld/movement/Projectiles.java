package org.pursuit.ar_wrld.movement;


import android.animation.ObjectAnimator;
import android.content.Context;
import android.net.Uri;
import android.os.CountDownTimer;
import android.view.animation.LinearInterpolator;

import com.google.ar.core.Anchor;
import com.google.ar.sceneform.AnchorNode;
import com.google.ar.sceneform.Camera;
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
import com.google.ar.sceneform.ux.ArFragment;

public class Projectiles extends AnchorNode {

    public Projectiles(Context context, Uri uri, ArFragment arFragment) {
        this.context = context;
        this.uri = uri;
        this.anchorNode = anchorNode;
        this.targetNode = targetNode;
        this.arFragment = arFragment;

    }

    private ArFragment arFragment;
    private Context context;
    private String PROJECTILE_TAG = "Projectile State";
    private AnchorNode anchorNode;
    private Node targetNode;
    private Vector3 targetLocation;
    private Vector3 point1;
    private Uri uri;
    private Uri uri2;
    private Camera camera;
    private  Vector3 position;
    @Override
    public void onActivate() {
        super.onActivate();
        point1 = arFragment.getArSceneView().getScene().getCamera().getBack();
        this.setLocalPosition(arFragment.getArSceneView().getScene().getCamera().getBack());
        this.setLookDirection(arFragment.getArSceneView().getScene().getCamera().getForward());

    }

    @Override
    public void onUpdate(FrameTime frameTime) {
        super.onUpdate(frameTime);
        targetLocation = this.getLocalPosition();

        point1 = arFragment.getArSceneView().getScene().getCamera().getBack();

    }

    @Override
    public void onTransformChange(Node node) {
        super.onTransformChange(node);
        targetLocation = this.getLocalPosition();
    }

    public void launchProjectile(Node node) {
        //setRenderable();
        position = new Vector3(0,0,arFragment.getArSceneView().getScene().getCamera().getBack().z);

        //point1 = this.getLocalPosition();
        point1 = arFragment.getArSceneView().getScene().getCamera().getBack();
        Vector3 startPoint = getWorldPosition();
        Vector3 point2 = node.getWorldPosition();

        final Vector3 difference = Vector3.subtract(position, point2);
        final Vector3 directionFromTopToBottom = difference.normalized();
       final Quaternion rotationFromAToB =
               Quaternion.lookRotation(directionFromTopToBottom, Vector3.back());
        MaterialFactory.makeOpaqueWithColor(context, new Color(android.graphics.Color.RED))
                .thenAccept(
                        material -> {
/* Then, create a rectangular prism, using ShapeFactory.makeCube() and use the difference vector
       to extend to the necessary length.  */
                            ModelRenderable model = ShapeFactory.makeCube(
                                    new Vector3(0.01f, 0.01f, difference.length()),position
                                    , material);

/* Last, set the world rotation of the node to the rotation calculated earlier and set the world position to
       the midpoint between the given points . */

                            setRenderable(model);
//                            laserNode.setWorldPosition(startVctor);
                            position = new Vector3(0,0,arFragment.getArSceneView().getScene().getCamera().getBack().z);
                            //position = Vector3.add(point1, point2).scaled(0f);
                            setParent(arFragment.getArSceneView().getScene());

                            setWorldPosition(position);
                           setWorldRotation(rotationFromAToB);
                            });


//        ModelRenderable.builder()
//                .setSource(context, uri2)
//                .build()
//                .thenAccept(modelRenderable2 -> {
//                   //this.setLight(light);
//                    this.setRenderable(modelRenderable2);
//                   // modelBlink(light, 2, 0f, 500f, 500);
//                    this.setLocalScale(new Vector3(1f, 1f, 1f));
//                    this.setWorldRotation(rotationFromAToB);
//                });



        ObjectAnimator objectAnimator = new ObjectAnimator();

        objectAnimator.setAutoCancel(true);
        objectAnimator.setTarget(this);
        objectAnimator.setPropertyName("worldPosition");
        objectAnimator.setObjectValues(position, point2);

        objectAnimator.setEvaluator(new Vector3Evaluator());
        //animation happens forever
//        objectAnimator.setRepeatCount(Obj);
//        //animation is the called in reverse
//        objectAnimator.setRepeatMode(ObjectAnimator.REVERSE);
        // This makes the animation linear (smooth and uniform).
        objectAnimator.setInterpolator(new LinearInterpolator());
        // Duration in ms of the animation.
        objectAnimator.clone();
        objectAnimator.setDuration(250);

        objectAnimator.start();


                            new CountDownTimer(200, 1000) {
                                @Override
                                public void onTick(long millisUntilFinished) {

                                }

                                @Override
                                public void onFinish() {

                                    setRenderable(null);
                                }
                            }.start();
                        }

                            public void modelBlink (Light receiver,int times, float from, float to,
                            long inMs){
                                ObjectAnimator intensityAnimator = ObjectAnimator.ofFloat(receiver, "intensity", from, to);
                                intensityAnimator.setDuration(inMs);
                                intensityAnimator.setRepeatCount(times * 2 - 1);
                                intensityAnimator.setRepeatMode(ObjectAnimator.REVERSE);
                                intensityAnimator.start();
                            }

                            public void setRenderable () {
                                Light light = Light.builder(Light.Type.POINT)
                                        .setColor(new Color(android.graphics.Color.MAGENTA))
                                        .setFalloffRadius(0.5f)
                                        .setShadowCastingEnabled(true)
                                        .setIntensity(45f)
                                        .build();


                                ModelRenderable.builder()
                                        .setSource(context, uri)
                                        .build()
                                        .thenAccept(modelRenderable -> {
                                            this.setLight(light);
                                            this.setRenderable(modelRenderable);
                                            modelBlink(light, 2, 0f, 500f, 500);
                                            this.setLocalScale(new Vector3(1f, 1f, 1f));

                                            // this.setLocalScale(new Vector3(.1f, .1f, -.009f));
//                            this.setWorldRotation(rotationFromAToB);
                                        });

                            }


                        }