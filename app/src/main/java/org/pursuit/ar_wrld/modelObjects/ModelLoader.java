package org.pursuit.ar_wrld.modelObjects;

import android.net.Uri;
import android.util.Log;
import android.view.MotionEvent;

import com.google.ar.core.Anchor;
import com.google.ar.core.HitResult;
import com.google.ar.core.Plane;
import com.google.ar.sceneform.AnchorNode;
import com.google.ar.sceneform.rendering.ModelRenderable;

import org.pursuit.ar_wrld.MainActivity;

import java.lang.ref.WeakReference;

public class ModelLoader{
    private int numofLivesModel;

    public WeakReference<MainActivity> getOwner() {
        return owner;
    }

    private final WeakReference<MainActivity> owner;
    //allows class to respect mainactivies lifcycle during garbage collection
    private static final String TAG = "ModelLoader";

    public int getNumofLivesModel0() {
        return numofLivesModel;
    }

    public void setNumofLivesModel0(int numofLivesModel) {
        this.numofLivesModel = numofLivesModel;
    }

    public ModelLoader(WeakReference<MainActivity> owner) {
        this.owner = owner;
    }

    public void loadModel(Anchor anchor, Uri uri) {
        if (owner.get() == null) {
            Log.d(TAG, "Activity is null.  Cannot load model.");
            return;
        }
        ModelRenderable.builder()
                .setSource(owner.get(), uri)
                .build()
                .handle((renderable, throwable) -> {
                    MainActivity activity = owner.get();
                    if (activity == null) {
                        return null;
                    } else if (throwable != null) {
                        activity.onException(throwable);
                    } else {
                        activity.
                        addNodeToScene(anchor, renderable);
                    }
                    return null;
                });

        return;
    }


}

