package me.julionxn.cinematiccreeper.core.camera;

import me.julionxn.cinematiccreeper.core.camera.paths.CameraPath;
import me.julionxn.cinematiccreeper.core.managers.CameraManager;

public class CameraRecordingPlayer {

    private final CameraManager manager;
    private final CameraRecording recording;
    private int tick;
    private boolean playing;
    private Snap previousSnap;

    public CameraRecordingPlayer(CameraManager manager, CameraRecording recording) {
        this.manager = manager;
        this.recording = recording;
    }

    public void play(){
        previousSnap = manager.takeSnap();
        tick = 0;
        playing = true;
        Snap snap = recording.getOrdererTimeline().get(0);
        manager.setZoom(snap.zoom);
        manager.setActualFov(snap.fov);
        manager.setActualAngles(snap.yaw, snap.pitch);
        manager.setActualPos(snap.getPos());
    }

    public void stop(){
        playing = false;
        manager.setZoom(previousSnap.zoom);
        manager.setActualFov(previousSnap.fov);
        manager.setActualAngles(previousSnap.yaw, previousSnap.pitch);
        manager.setActualPos(previousSnap.getPos());
        manager.setBlockInputs(false);
    }

    public void tick(){
        if (!playing) return;
        CameraPath path = recording.getPath();
        Snap snap = path.getInterpolatedSnap(tick);
        manager.setZoom(snap.zoom);
        manager.updateFov(snap.fov);
        manager.updateRotation(snap.yaw, snap.pitch);
        manager.moveTo(snap.getPos());
        tick++;
        if (tick > recording.getLength()) stop();
    }

}
