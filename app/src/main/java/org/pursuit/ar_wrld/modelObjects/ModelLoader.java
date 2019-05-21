package org.pursuit.ar_wrld.modelObjects;

import android.net.Uri;
import android.util.Log;

import com.google.ar.core.Anchor;
import com.google.ar.sceneform.rendering.ModelRenderable;

import org.pursuit.ar_wrld.MainActivity;

import java.lang.ref.WeakReference;

public class ModelLoader {
    private int numofLivesModel;

//    public WeakReference<MainActivity> getOwner() {
//        return owner;
//    }

    private static final String TAG = "ModelLoader";

    public int getNumofLivesModel0() {
        return numofLivesModel;
    }

    public void setNumofLivesModel0(int numofLivesModel) {
        this.numofLivesModel = numofLivesModel;
    }

    public ModelLoader(int numofLivesModel) {
        this.numofLivesModel = numofLivesModel;
    }

    }


