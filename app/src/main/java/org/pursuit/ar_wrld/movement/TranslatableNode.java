package org.pursuit.ar_wrld.movement;


import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.animation.TimeInterpolator;
import android.animation.TypeEvaluator;
import android.view.animation.LinearInterpolator;

import com.google.ar.sceneform.Node;
import com.google.ar.sceneform.math.Vector3;

import java.util.Arrays;

import javax.annotation.Nullable;

public final class TranslatableNode extends Node {

    private DroidPosition position;

    public final DroidPosition getPosition() {
        return this.position;
    }

    public final void setPosition( DroidPosition var1) {
        this.position = var1;
    }

    public final void addOffset(float x, float y, float z) {
        float posX = this.getLocalPosition().x + x;
        float posY = this.getLocalPosition().y + y;
        float posZ = this.getLocalPosition().z + z;
        this.setLocalPosition(new Vector3(posX, posY, posZ));
    }

    // $FF: synthetic method
    public static void addOffset$default(TranslatableNode var0, float var1, float var2, float var3, int var4, Object var5) {
        if ((var4 & 1) != 0) {
            var1 = 0.0F;
        }

        if ((var4 & 2) != 0) {
            var2 = 0.0F;
        }

        if ((var4 & 4) != 0) {
            var3 = 0.0F;
        }

        var0.addOffset(var1, var2, var3);
    }

    public final void pullUp() {
        if (this.position != DroidPosition.MOVING_UP && this.position != DroidPosition.UP) {
            this.animatePullUp();
        }

    }

    public final void pullDown() {
        if (this.position != DroidPosition.MOVING_DOWN && this.position != DroidPosition.DOWN) {
            this.animatePullDown();
        }

    }

    private final ObjectAnimator localPositionAnimator(Object... values) {
        ObjectAnimator var2 = new ObjectAnimator();
        var2.setTarget(this);
        var2.setPropertyName("localPosition");
        var2.setDuration(250L);
        var2.setInterpolator((TimeInterpolator)(new LinearInterpolator()));
        var2.setAutoCancel(true);
        var2.setObjectValues(Arrays.copyOf(values, values.length));
        var2.setEvaluator((TypeEvaluator)(new VectorEvaluator()));
        return var2;
    }

    private final void animatePullUp() {
        Vector3 low = new Vector3(this.getLocalPosition());
        Vector3 var3 = new Vector3(this.getLocalPosition());
        var3.y = 0.4F;
        ObjectAnimator animation = this.localPositionAnimator(low, var3);
        animation.addListener((Animator.AnimatorListener)(new Animator.AnimatorListener() {
            public void onAnimationRepeat(@Nullable Animator animation) {
            }

            public void onAnimationCancel(@Nullable Animator animation) {
            }

            public void onAnimationEnd(@Nullable Animator animation) {
                TranslatableNode.this.setPosition(DroidPosition.UP);
            }

            public void onAnimationStart(@Nullable Animator animation) {
                TranslatableNode.this.setPosition(DroidPosition.MOVING_UP);
            }
        }));
        animation.start();
    }

    private final void animatePullDown() {
        Vector3 high = new Vector3(this.getLocalPosition());
        Vector3 var3 = new Vector3(this.getLocalPosition());
        var3.y = 0.0F;
        ObjectAnimator animation = this.localPositionAnimator(high, var3);
        animation.addListener((Animator.AnimatorListener)(new Animator.AnimatorListener() {
            public void onAnimationRepeat(@Nullable Animator animation) {
            }

            public void onAnimationCancel(@Nullable Animator animation) {
            }

            public void onAnimationEnd(@Nullable Animator animation) {
                TranslatableNode.this.setPosition(DroidPosition.DOWN);
            }

            public void onAnimationStart(@Nullable Animator animation) {
                TranslatableNode.this.setPosition(DroidPosition.MOVING_DOWN);
            }
        }));
        animation.start();
    }

    public TranslatableNode() {
        this.position = DroidPosition.DOWN;
    }
}