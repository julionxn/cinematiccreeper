package me.julionxn.cinematiccreeper.util;

public class MathHelper {

    public static final float PI = 3.14159265358f;

    public static float toDegrees(float rads){
        return rads * 180.0f / PI;
    }

    public static float clamp(float value, float min, float max){
        if (value > max) return max;
        if (value < min) return min;
        return value;
    }

}
