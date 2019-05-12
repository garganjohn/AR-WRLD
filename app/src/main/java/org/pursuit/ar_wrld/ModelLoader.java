package org.pursuit.ar_wrld;

import android.net.Uri;
import android.util.Log;

import com.google.ar.core.Anchor;
import com.google.ar.sceneform.rendering.ModelRenderable;

import java.lang.ref.WeakReference;

public class ModelLoader {
    private int numofLivesModel0;
    private int numofLivesModel1;
    private int numofLivesModel2;

    private final WeakReference<MainActivity> owner;
    private static final String TAG = "ModelLoader";

    public int getNumofLivesModel0() {
        return numofLivesModel0;
    }

    public void setNumofLivesModel0(int numofLivesModel0) {
        this.numofLivesModel0 = numofLivesModel0;
    }

    public int getNumofLivesModel1() {
        return numofLivesModel1;
    }

    public void setNumofLivesModel1(int numofLivesModel1) {
        this.numofLivesModel1 = numofLivesModel1;
    }

    public int getNumofLivesModel2() {
        return numofLivesModel2;
    }

    public void setNumofLivesModel2(int numofLivesModel2) {
        this.numofLivesModel2 = numofLivesModel2;
    }

    ModelLoader(WeakReference<MainActivity> owner) {
        this.owner = owner;
    }

    void loadModel(Anchor anchor, Uri uri) {
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
                        activity.addNodeToScene(anchor, renderable);
                    }
                    return null;
                });

        return;
    }
}

