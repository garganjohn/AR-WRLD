package org.pursuit.ar_wrld.Effects;

import android.animation.ObjectAnimator;

import com.google.ar.sceneform.Node;
import com.google.ar.sceneform.math.Vector3;
import com.google.ar.sceneform.rendering.Color;
import com.google.ar.sceneform.rendering.Light;
import com.google.ar.sceneform.ux.TransformableNode;

import org.pursuit.ar_wrld.movement.MovementNode;

public class ModelLights {

    public ModelLights(Light light, MovementNode anchorNode, TransformableNode node) {
        this.light = light;
        this.anchorNode = anchorNode;
        this.node = node;
    }

    private Light light;
    private MovementNode anchorNode;
    private TransformableNode node;

    public Light getLight() {
        return light;
    }

    public void setLight(Light light) {
        this.light = light;
    }

    public MovementNode getAnchorNode() {
        return anchorNode;
    }

    public void setAnchorNode(MovementNode anchorNode) {
        this.anchorNode = anchorNode;
    }

    public TransformableNode getNode() {
        return node;
    }

    public void setNode(TransformableNode node) {
        this.node = node;
    }

    public void setUpLights() {
        light =
                Light.builder(Light.Type.POINT)
                        .setFalloffRadius(0.5f)
                        .setColor(new Color(android.graphics.Color.YELLOW))
                        .setShadowCastingEnabled(false)
                        .setIntensity(5f)
                        .build();

        Vector3 position = anchorNode.getLocalPosition();


        Node lightNode = new Node();
        lightNode.setParent(this.node);
        lightNode.setLocalPosition(position);
        lightNode.setLight(light);
    }

    public void modelBlink(Light receiver, int times, float from, float to, long inMs) {
        ObjectAnimator intensityAnimator = ObjectAnimator.ofFloat(receiver, "intensity", from, to);
        intensityAnimator.setDuration(inMs);
        intensityAnimator.setRepeatCount(times * 2 - 1);
        intensityAnimator.setRepeatMode(ObjectAnimator.REVERSE);
        intensityAnimator.start();
    }

}
