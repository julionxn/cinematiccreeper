package me.julionxn.cinematiccreeper.core.camera;

import com.google.gson.annotations.Expose;

import java.util.TreeMap;

public class CameraRecording {

    @Expose public final String id;
    @Expose private int tick = 0;
    @Expose private int length = 0;
    @Expose private int displayLength = 60;
    @Expose private int previousMaxTick = 0;
    @Expose private final TreeMap<Integer, Snap> timeline = new TreeMap<>();

    public CameraRecording(String id){
        this.id = id;
    }

    public void addSnap(Snap snap){
        if (tick > length){
            previousMaxTick = length;
            length = tick;
        }
        if (timeline.size() > 2){
            int snapToAdjustTick = timeline.lowerKey(tick);
            int previousSnapTick = timeline.lowerKey(snapToAdjustTick);
            Snap snapToAdjust = timeline.get(snapToAdjustTick);
            Snap previousSnap = timeline.get(previousSnapTick);
            snapToAdjust.adjustControlPoint(previousSnap, snap);
        }
        timeline.put(tick, snap);
    }

    public void removeSnap(){
        if (!timeline.containsKey(tick)) return;
        timeline.remove(tick);
        Integer prevTick = timeline.lowerKey(tick);
        if (prevTick != null){
            timeline.get(prevTick).clearSecondControl();
        }
        if (tick == length){
            length = previousMaxTick;
            if (length > 60){
                setCurrentTick(length);
            }
        }
    }

    public void adjustTick(int in){
        if (tick + in < 0) {
            setCurrentTick(0);
            return;
        }
        setCurrentTick(tick + in);
    }

    public void adjustToNextSnap(){
        Integer nextTick = timeline.higherKey(tick);
        if (nextTick == null) return;
        setCurrentTick(nextTick);
    }

    public void adjustToPrevSnap(){
        Integer nextTick = timeline.lowerKey(tick);
        if (nextTick == null) return;
        setCurrentTick(nextTick);
    }

    private void setCurrentTick(int tick) {
        if (tick < 0) return;
        displayLength = Math.max(tick, Math.max(60, length));
        this.tick = tick;
    }

    public int getDisplayLength(){
        return displayLength;
    }

    public int getSize(){
        return timeline.size();
    }

}
