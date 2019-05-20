package org.pursuit.ar_wrld.modelObjects;

import android.net.Uri;
import android.util.Log;

import com.google.ar.core.Anchor;
import com.google.ar.sceneform.rendering.ModelRenderable;

import org.pursuit.ar_wrld.MainActivity;

import java.lang.ref.WeakReference;

public class ModelLoader {
    private int numofLivesModel;
    private Model model;

    public WeakReference<MainActivity> getOwner() {
        return owner;
    }

    public Model getModel() {
        return model;
    }

    private final WeakReference<MainActivity> owner;
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

    public void loadModel(Model model) {
        this.model = model;
        if (owner.get() == null) {
            Log.d(TAG, "Activity is null.  Cannot load model.");
            return;
        }
        ModelRenderable.builder()
                .setSource(owner.get(), model.getModel())
                .build()
                .handle((renderable, throwable) -> {
                    MainActivity activity = owner.get();
                    if (activity == null) {
                        return null;
                    } else if (throwable != null) {
                        activity.onException(throwable);
                    } else {
                        activity.addNodeToScene(model.getAnchor(), renderable);
                    }
                    return null;
                });

        return;
    }
}

