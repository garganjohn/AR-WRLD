package org.pursuit.ar_wrld.Effects;

import android.annotation.SuppressLint;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

public class HitMarker extends Drawable {
    public HitMarker(float xpos, float ypos) {
        this.xpos = xpos;
        this.ypos = ypos;
    }

    private Paint paint = new Paint();
    float xpos;
    float ypos;

    public float getXpos() {
        return xpos;
    }


    public float getYpos() {
        return ypos;
    }


    private boolean enabled;

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    @Override
    public void draw(Canvas canvas) {
            paint.setColor(Color.RED);
            canvas.drawText("HIT!", xpos, ypos, paint);

//            paint.setColor(Color.GRAY);
//            canvas.drawText("Miss!", xpos, ypos, paint);

    }


    @Override
    public void setAlpha(int alpha) {

    }

    @Override
    public void setColorFilter( ColorFilter colorFilter) {

    }

    @SuppressLint("WrongConstant")
    @Override
    public int getOpacity() {
        return 0;
    }


}
