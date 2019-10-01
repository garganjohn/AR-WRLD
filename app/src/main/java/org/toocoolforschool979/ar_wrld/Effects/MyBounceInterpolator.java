package org.toocoolforschool979.ar_wrld.Effects;

public class MyBounceInterpolator implements android.view.animation.Interpolator {

    private double amplitude = 1;
    private double frequency = 10;

    public MyBounceInterpolator(double amplitude, double frequency) {
        this.amplitude = amplitude;
        this.frequency = frequency;
    }

    @Override
    public float getInterpolation(float input) {
        return (float) (-1 * Math.pow(Math.E, - input / amplitude) *
                Math.cos(frequency * input) + 1);
    }
}

