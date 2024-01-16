package me.julionxn.cinematiccreeper.core.poses;

import com.google.gson.annotations.Expose;

public class PoseData {

    @Expose public float yaw;
    @Expose public float pitch;
    @Expose public float roll;
    @Expose public float scaleX = 1;
    @Expose public float scaleY = 1;
    @Expose public float scaleZ = 1;
    @Expose public float translationX;
    @Expose public float translationY;
    @Expose public float translationZ;

    public PoseData pivot(float x, float y, float z){
        this.translationX = x;
        this.translationY = y;
        this.translationZ = z;
        return this;
    }

}
