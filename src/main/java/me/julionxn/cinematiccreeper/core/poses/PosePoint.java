package me.julionxn.cinematiccreeper.core.poses;

import com.google.gson.annotations.Expose;
import me.julionxn.cinematiccreeper.core.Interpolation;

public class PosePoint {

    @Expose public PoseData torso = new PoseData();
    @Expose public PoseData head = new PoseData();
    @Expose public PoseData leftArm = new PoseData().pivot(5, 2, 0);
    @Expose public PoseData rightArm = new PoseData().pivot(-5, 2, 0);
    @Expose public PoseData leftLeg = new PoseData().pivot(1.9f, 12, 0);
    @Expose public PoseData rightLeg = new PoseData().pivot(-1.9f, 12, 0);
    @Expose public Interpolation interpolation = Interpolation.LINEAR;

    public PosePoint() {
    }

}
