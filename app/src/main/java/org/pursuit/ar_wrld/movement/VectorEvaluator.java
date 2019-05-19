package org.pursuit.ar_wrld.movement;

import android.animation.TypeEvaluator;

import com.google.ar.sceneform.math.Vector3;

import javax.annotation.Nullable;

public final class VectorEvaluator implements TypeEvaluator {
    @Nullable
    public Vector3 evaluate(float fraction, Vector3 startValue, Vector3 endValue) {
        float startX = startValue.x;
        float x = startX + fraction * (endValue.x - startX);
        float startY = startValue.y;
        float y = startY + fraction * (endValue.y - startY);
        float startZ = startValue.z;
        float z = startZ + fraction * (endValue.z - startZ);
        return new Vector3(x, y, z);
    }

    // $FF: synthetic method
    // $FF: bridge method
    public Object evaluate(float var1, Object var2, Object var3) {
        return this.evaluate(var1, (Vector3) var2, (Vector3) var3);
    }
}
