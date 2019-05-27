package org.pursuit.ar_wrld.util;

import android.util.Log;
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

    public ModelLocationIndicator(TransformableNode transformableNode, Vector3 modelNodePosition, ArFragment arFragment) {
        this.modelNodePosition = modelNodePosition;
        this.arFragment = arFragment;
        this.modelNode = transformableNode;
    }

    public void addMarkerToModel() {
        float holder = modelNodePosition.y + 0.5f;
        Vector3 markerNodePosition = new Vector3(modelNodePosition.x, holder, modelNodePosition.z);
        markerNode = new TransformableNode(arFragment.getTransformationSystem());
        addImageToMarker(markerNode);
        markerNode.setParent(modelNode);
        markerNode.setLocalPosition(markerNodePosition);
        Log.d(TAG, "addMarkerToModel: ");
    }

    private void addImageToMarker(TransformableNode markerNode) {

        markerNode.setRenderable(pngToRenderable());
    }

    private Renderable pngToRenderable() {
        Renderable renderable;
        ViewRenderable.builder()
                .setView(arFragment.getContext(), R.layout.marker_layout)
                .build()
                .thenAccept(img -> {
                    ImageView imageView = (ImageView) img.getView();
                });
        return renderable;
    }
}

