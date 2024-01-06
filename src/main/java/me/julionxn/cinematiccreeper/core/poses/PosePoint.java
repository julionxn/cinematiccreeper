package me.julionxn.cinematiccreeper.core.poses;

import com.google.gson.annotations.Expose;
import me.julionxn.cinematiccreeper.core.Easing;

public class PosePoint {

    @Expose public PoseData head = new PoseData();
    @Expose public PoseData leftArm = new PoseData();
    @Expose public PoseData rightArm = new PoseData();
    @Expose public PoseData leftLeg = new PoseData();
    @Expose public PoseData rightLeg = new PoseData();
    @Expose public Easing easing = Easing.NONE;

    public PosePoint() {
    }

}
