package me.julionxn.cinematiccreeper.core.camera.paths;

import me.julionxn.cinematiccreeper.core.camera.CameraRecording;
import me.julionxn.cinematiccreeper.core.camera.Snap;
import me.julionxn.cinematiccreeper.util.RenderUtils;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.util.math.MatrixStack;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public abstract class CurveCameraPath extends CameraPath{

    protected List<Snap> ordererSnaps;

    public CurveCameraPath(CameraRecording recording) {
        super(recording);
    }

    @Override
    public void render(MinecraftClient client, MatrixStack stack) {
        int size = ordererSnaps.size();
        if (size == 0) return;
        if (size == 1){
            RenderUtils.renderBillboardTexture(client, ordererSnaps.get(0).getPos(), POINT_TEXTURE, 1f, 1f, 1f);
            return;
        }
        for (Snap ordererSnap : ordererSnaps) {
            RenderUtils.renderBillboardTexture(client, ordererSnap.getPos(), POINT_TEXTURE, 1f, 1f, 1f);
        }
        renderCurve(client, stack);
    }

    @Override
    public Snap getInterpolatedSnap(int tick) {
        int p1Tick = cameraRecording.getLowerTickFrom(tick);
        Integer p0Tick = cameraRecording.getLowerTickFrom(p1Tick);
        if (p0Tick == null) p0Tick = p1Tick;
        int p2Tick = cameraRecording.getHigherTickFrom(tick);
        Integer p3Tick = cameraRecording.getHigherTickFrom(p2Tick);
        if (p3Tick == null) p3Tick = p2Tick;
        Snap p0 = cameraRecording.getSnap(p0Tick);
        Snap p1 = cameraRecording.getSnap(p1Tick);
        Snap p2 = cameraRecording.getSnap(p2Tick);
        Snap p3 = cameraRecording.getSnap(p3Tick);
        float t =(tick - p1Tick) / (float) (p2Tick - p1Tick);
        return interpolateCurve(t, p0, p1, p2,p3);
    }

    protected abstract Snap interpolateCurve(float t, Snap p0, Snap p1, Snap p2, Snap p3);
    protected abstract void renderCurve(MinecraftClient client, MatrixStack stack);

    @Override
    public void onPointsModified() {
        List<Snap> snaps = cameraRecording.getOrdererTimeline();
        int size = snaps.size();
        if (size > 1){
            ordererSnaps = new ArrayList<>();
            ordererSnaps.add(snaps.get(0).copy());
            ordererSnaps.addAll(snaps);
            ordererSnaps.add(snaps.get(snaps.size() - 1).copy());
        } else if (size == 1) {
            ordererSnaps = snaps;
        } else {
            ordererSnaps = Collections.emptyList();
        }
    }
}
