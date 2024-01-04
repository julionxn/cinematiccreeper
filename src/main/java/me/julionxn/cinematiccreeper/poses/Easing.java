package me.julionxn.cinematiccreeper.poses;

import java.util.function.Function;

public enum Easing {

    NONE(t -> t),
    EASE_IN(t -> t * t),
    EASE_OUT(t -> {
        float toPow = 1 - t;
        return 1 - toPow * toPow;
    });

    private final Function<Float, Float> interpolation;

    Easing(Function<Float, Float> interpolation){
        this.interpolation = interpolation;
    }

    public float interpolate(float t, float start, float end){
        return start + interpolation.apply(t) * (end - start);
    }

}
