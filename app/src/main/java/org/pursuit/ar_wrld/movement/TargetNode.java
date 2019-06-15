package org.pursuit.ar_wrld.movement;

import com.google.ar.sceneform.Node;
import com.google.ar.sceneform.math.Vector3;

public class TargetNode extends Node {
    public TargetNode() {
    }

    private Node parentNode;
    private Vector3 parentNodePosition;

    private Vector3 createTarget() {
        parentNodePosition = getParent().getLocalPosition();
        return parentNodePosition;


    }
}
