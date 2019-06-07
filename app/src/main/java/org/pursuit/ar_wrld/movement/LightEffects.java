package org.pursuit.ar_wrld.movement;

import android.animation.ObjectAnimator;

import com.google.ar.sceneform.Node;
import com.google.ar.sceneform.rendering.Color;
import com.google.ar.sceneform.rendering.Light;
import com.google.ar.sceneform.ux.TransformableNode;

public class LightEffects{


    public LightEffects(TransformableNode transformableNode, Light modelLight) {
        this.transformableNode = transformableNode;
        this.modelLight = modelLight;
    }

    public TransformableNode getTransformableNode() {
        return transformableNode;
    }

    private TransformableNode transformableNode;
    private Light modelLight ;

    public void setUpLightsRed() {
        modelLight =
                Light.builder(Light.Type.POINT)
                        .setColor(new Color(android.graphics.Color.RED))
                        // .setFalloffRadius(node.getScaleController().getMinScale())
                        //.setFalloffRadius(30f)
                        .setShadowCastingEnabled(false)
                        .setIntensity(0)
                        .build();
        Node lightNode = new Node();
        lightNode.setParent(getTransformableNode().getParent());
        // lightNode.setLocalPosition(node.getLocalScale());
        lightNode.setLocalPosition(transformableNode.getLocalScale());
        lightNode.setLight(modelLight);
        //modelBlink(modelLight, 3, 0f, 1000f, 1000);
    }

    public void modelBlink(Light receiver, int times, float from, float to, long inMs) {
        ObjectAnimator intensityAnimator = ObjectAnimator.ofFloat(receiver, "intensity", from, to);
        intensityAnimator.setDuration(inMs);
        intensityAnimator.setRepeatCount(times * 2 - 1);
        intensityAnimator.setRepeatMode(ObjectAnimator.REVERSE);
        intensityAnimator.start();

    }


    public void setUpLightsYellow() {
        modelLight =
                Light.builder(Light.Type.FOCUSED_SPOTLIGHT)
                        .setColor(new Color(android.graphics.Color.YELLOW))
                        .setFalloffRadius(transformableNode.getScaleController().getMinScale())
                        .setShadowCastingEnabled(false)
                        .setIntensity(0)
                        .build();
//        Node lightNode = new Node();
//        lightNode.setParent(transformableNode.getParent());
//        lightNode.setLocalPosition(transformableNode.getWorldPosition());
//        lightNode.setLight(modelLight);
        //modelBlink(modelLight, 3, 0f, 100000f, 100);

    }
}
