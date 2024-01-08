package me.julionxn.cinematiccreeper.core;

import java.util.function.Function;

public enum Interpolation {

    LINEAR(t -> t),
    EASE_IN(t -> t * t),
    EASE_OUT(t -> {
        float toPow = 1 - t;
        return 1 - toPow * toPow;
    });

    private final Function<Float, Float> interpolation;

    Interpolation(Function<Float, Float> interpolation){
        this.interpolation = interpolation;
    }

    public float interpolate(float t, float start, float end){
        return start + interpolation.apply(t) * (end - start);
    }

    public double interpolate(double t, double start, double end){
        return start + interpolation.apply((float) t) * (end - start);
    }

}
