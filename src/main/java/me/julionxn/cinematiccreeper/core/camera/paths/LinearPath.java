package me.julionxn.cinematiccreeper.core.camera.paths;

import me.julionxn.cinematiccreeper.core.camera.CameraRecording;
import me.julionxn.cinematiccreeper.core.camera.Snap;
import me.julionxn.cinematiccreeper.util.RenderUtils;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.math.Vec3d;

import java.util.List;

public class LinearPath extends CameraPath{

    public LinearPath(CameraRecording recording) {
        super(recording);
    }

    @Override
    public void render(MinecraftClient client, MatrixStack stack) {
        List<Snap> snaps = cameraRecording.getOrdererTimeline();
        int size = snaps.size();
        if (size < 1) return;
        if (size == 1) {
            RenderUtils.renderBillboardTexture(client, snaps.get(0).getPos(), POINT_TEXTURE, 1f, 1f, 1f);
            return;
        }
        Vec3d start = snaps.get(0).getPos();
        for (Snap snap : snaps) {
            Vec3d end = snap.getPos();
            RenderUtils.renderBillboardTexture(client, end, POINT_TEXTURE, 1f, 1f, 1f);
            RenderUtils.renderLine(client, stack, start, end, 0x0000ff);
            start = end;
        }
    }

    @Override
    public Snap getInterpolatedSnap(int tick) {
        int startTick = cameraRecording.getLowerTickFrom(tick);
        Snap startSnap = cameraRecording.getSnap(startTick);
        int endTick = cameraRecording.getHigherTickFrom(tick);
        Snap endSnap = cameraRecording.getSnap(endTick);
        if (endSnap == null) return startSnap;
        float t = (tick - startTick) / (float) (endTick - startTick);
        return interpolate(t, startSnap, endSnap);
    }

    private Snap interpolate(float t, Snap startSnap, Snap endSnap) {
        double x = interpolate(startSnap.pos[0], endSnap.pos[0], t);
        double y = interpolate(startSnap.pos[1], endSnap.pos[1], t);
        double z = interpolate(startSnap.pos[2], endSnap.pos[2], t);
        double fov = interpolate(startSnap.fov, endSnap.fov, t);
        double zoom = interpolate(startSnap.zoom, endSnap.zoom, t);
        float yaw = interpolate(startSnap.yaw, endSnap.yaw, t);
        float pitch = interpolate(startSnap.pitch, endSnap.pitch, t);
        return new Snap(new Vec3d(x, y, z), yaw, pitch, zoom, fov);
    }

    private double interpolate(double from, double to, float t){
        return (1 - t) * from + t * to;
    }

    private float interpolate(float from, float to, float t){
        return (1 - t) * from + t * to;
    }

    @Override
    public void onPointsModified() {

    }

}
