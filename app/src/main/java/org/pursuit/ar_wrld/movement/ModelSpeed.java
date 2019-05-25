package org.pursuit.ar_wrld.movement;

public class ModelSpeed {


    private float modelSpeedMultiplier = 1.0f;
    private float rotationSpeedMultiplier = 1.0f;

    public void setModelSpeedMultiplier(float modelSpeedMultiplier) {
        this.modelSpeedMultiplier = modelSpeedMultiplier;
    }

    public float getModelSpeedMultiplier() {
        return modelSpeedMultiplier;
    }

    public void setRotationSpeedMultiplier(float rotationSpeedMultiplier) {
        this.rotationSpeedMultiplier = rotationSpeedMultiplier;
    }

    public float getRotationSpeedMultiplier() {
        return rotationSpeedMultiplier;
    }
}

