package me.julionxn.cinematiccreeper.poses;

import com.google.gson.annotations.Expose;

import java.util.HashMap;

public class NpcPose {

    @Expose
    private final PoseType poseType;
    @Expose
    private final HashMap<Integer, PosePoint> poses = new HashMap<>();
    @Expose
    private int length = 0;

    public NpcPose(PoseType poseType){
        this.poseType = poseType;
    }

    public HashMap<Integer, PosePoint> getPoses(){
        return poses;
    }

    public void setPose(int tick, PosePoint posePoint){
        poses.put(tick, posePoint);
        if (tick > length - 1) length = tick + 1;
    }

    public void setPose(PosePoint posePoint){
        setPose(0, posePoint);
    }

    public PosePoint getPoseOfTick(int tick){
        return poses.get(tick);
    }

    public void removePose(int tick){
        if (!containsAPose(tick)) return;
        poses.remove(tick);
    }

    public boolean containsAPose(int tick){
        return poses.containsKey(tick);
    }

    public int getLength(){
        return length;
    }

}
