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

    public static float interpolateCyclic(Interpolation interpolation, float t, float start, float end){
        float angleDif = end - start;
        if (Math.abs(angleDif) > 180){
            angleDif += angleDif > 0 ? -360 : 360;
        }
        float interpolated = start + interpolation.interpolation.apply(t) * angleDif;
        return clamp(interpolated);
    }

    private static float clamp(float angle){
        angle = angle % 360;
        if (angle > 180){
            angle -= 360;
        } else if (angle < -180){
            angle += 360;
        }
        return angle;
    }

}
