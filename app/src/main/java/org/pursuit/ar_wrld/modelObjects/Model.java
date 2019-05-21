package org.pursuit.ar_wrld.modelObjects;

import android.net.Uri;

import com.google.ar.core.Anchor;

public class Model {
    private int lives = 2;
    private Anchor anchor;
    private Uri model;

    public Model(Anchor anchor, Uri model) {
        this.anchor = anchor;
        this.model = model;
    }

    public Anchor getAnchor() {
        return anchor;
    }

    public Uri getModel() {
        return model;
    }

    public int getLives() {
        return lives;
    }

    public void setLives(int lives) {
        this.lives = lives;
    }
}
