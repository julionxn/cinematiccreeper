package me.julionxn.cinematiccreeper.core.camera;

import com.google.gson.annotations.Expose;
import me.julionxn.cinematiccreeper.core.camera.paths.*;

import java.util.ArrayList;
import java.util.List;
import java.util.TreeMap;

public class CameraRecording {

    @Expose public final String id;
    private int tick = 0;
    @Expose private int length = 0;
    private int displayLength = 60;
    private int previousMaxTick = 0;
    @Expose private final TreeMap<Integer, Snap> timeline = new TreeMap<>();
    @Expose private final PathType pathType;
    private CameraPath path;

    public CameraRecording(String id, PathType type){
        this.id = id;
        this.pathType = type;
        generatePath();
    }

    private void generatePath(){
        switch (pathType){
            case CATMULL -> path = new CatmullRomPath(this);
            case BSPLINE -> path = new BSplinePath(this);
            default -> path = new LinearPath(this);
        }
    }

    public CameraPath getPath(){
        if (path == null) generatePath();
        return path;
    }

    public void addSnap(Snap snap){
        if (tick > length){
            previousMaxTick = length;
            length = tick;
        }
        snap.tick = tick;
        timeline.put(tick, snap);
        path.onPointsModified();
    }

    public void removeSnap(){
        if (!timeline.containsKey(tick)) return;
        timeline.remove(tick);
        if (tick == length){
            length = previousMaxTick;
            if (length > 60){
                setCurrentTick(length);
            }
        }
        path.onPointsModified();
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

    public TreeMap<Integer, Snap> getTimeline(){
        return timeline;
    }

    public Integer getLowerTickFrom(int tick){
        if (timeline.containsKey(tick)) return tick;
        return timeline.lowerKey(tick);
    }

    public Integer getHigherTickFrom(int tick){
        Integer high = timeline.higherKey(tick);
        if (high == null) return length;
        return high;
    }

    public Snap getSnap(int tick){
        return timeline.get(tick);
    }

    public List<Snap> getOrdererTimeline(){
        return new ArrayList<>(timeline.values());
    }

    public int getTick(){
        return tick;
    }

    public boolean containsSnap(int tick){
        return timeline.containsKey(tick);
    }

    public int getLength(){
        return length;
    }

    public int getDisplayLength(){
        return displayLength;
    }

    public int getSize(){
        return timeline.size();
    }

}
