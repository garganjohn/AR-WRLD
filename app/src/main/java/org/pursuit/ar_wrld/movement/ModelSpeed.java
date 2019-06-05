package org.pursuit.ar_wrld.movement;

public class ModelSpeed {
    public long getModelSpeedMultiplier() {
        return modelSpeedMultiplier;
    }

    public void setModelSpeedMultiplier(long modelSpeedMultiplier) {
        this.modelSpeedMultiplier = modelSpeedMultiplier;
    }

    public long getRotationSpeedMultiplier() {
        return rotationSpeedMultiplier;
    }

    public void setRotationSpeedMultiplier(long rotationSpeedMultiplier) {
        this.rotationSpeedMultiplier = rotationSpeedMultiplier;
    }

    private long modelSpeedMultiplier = 1000;
    private long rotationSpeedMultiplier = 1000;


}

