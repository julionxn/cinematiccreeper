package me.julionxn.cinematiccreeper.core.poses;

import com.google.gson.annotations.Expose;
import org.jetbrains.annotations.Nullable;

import java.util.TreeMap;

public class NpcPose {

    @Expose
    private final TreeMap<Integer, PosePoint> poses = new TreeMap<>();
    @Expose
    private int length = 0;

    public TreeMap<Integer, PosePoint> getPoses(){
        return poses;
    }

    public void setPose(int tick, PosePoint posePoint){
        poses.put(tick, posePoint);
        if (tick > length - 1) length = tick;
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
        if (tick == length) {
            Integer newLength = currentPoseOfTick(tick);
            length = newLength == null ? 0 : newLength;
        }
    }

    @Nullable
    public Integer currentPoseOfTick(int tick){
        if (containsAPose(tick)) return tick;
        return poses.lowerKey(tick);
    }

    @Nullable
    public Integer nextPoseOfTick(int tick){
        return poses.higherKey(tick);
    }

    public boolean containsAPose(int tick){
        return poses.containsKey(tick);
    }

    public int getLength(){
        return length;
    }

    public boolean isEmpty(){
        return poses.isEmpty();
    }

}
