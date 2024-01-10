package me.julionxn.cinematiccreeper.core.camera.paths;

import me.julionxn.cinematiccreeper.core.camera.CameraRecording;
import me.julionxn.cinematiccreeper.core.camera.Snap;
import me.julionxn.cinematiccreeper.util.RenderUtils;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.math.Vec3d;

public class CatmullRomPath extends CurveCameraPath{

    public CatmullRomPath(CameraRecording recording) {
        super(recording);
    }

    @Override
    protected void renderCurve(MinecraftClient client, MatrixStack stack) {
        for (int i = 0; i < ordererSnaps.size() - 3; i++) {
            Snap p0 = ordererSnaps.get(i);
            Snap p1 = ordererSnaps.get(i + 1);
            Snap p2 = ordererSnaps.get(i + 2);
            Snap p3 = ordererSnaps.get(i + 3);
            float t = 0;
            Vec3d start = interpolateCurve(t, p0, p1, p2, p3).getPos();
            for (int j = 1; j <= 20; j++) {
                t = (float) j / 20;
                Vec3d end = interpolateCurve(t, p0, p1, p2, p3).getPos();
                RenderUtils.renderLine(client, stack, start, end, 0xff0000);
                start = end;
            }
        }
    }

    @Override
    protected Snap interpolateCurve(float t, Snap p0, Snap p1, Snap p2, Snap p3) {
        float t2 = t * t;
        float t3 = t2 * t;
        Vec3d pos0 = p0.getPos();
        Vec3d pos1 = p1.getPos();
        Vec3d pos2 = p2.getPos();
        Vec3d pos3 = p3.getPos();
        double x = interpolate(t, t2, t3, pos0.x, pos1.x, pos2.x, pos3.x);
        double y = interpolate(t, t2, t3, pos0.y, pos1.y, pos2.y, pos3.y);
        double z = interpolate(t, t2, t3, pos0.z, pos1.z, pos2.z, pos3.z);
        float yaw = interpolate(t, t2, t3, p0.yaw, p1.yaw, p2.yaw, p3.yaw);
        float pitch = interpolate(t, t2, t3, p0.pitch, p1.pitch, p2.pitch, p3.pitch);
        double fov = interpolate(t, t2, t3, p0.fov, p1.fov, p2.fov, p3.fov);
        double zoom = interpolate(t, t2, t3, p0.zoom, p1.zoom, p2.zoom, p3.zoom);
        return new Snap(new Vec3d(x, y, z), yaw, pitch, zoom, fov);
    }

    private double interpolate(float t, float t2, float t3, double v1, double v2, double v3, double v4){
        return 0.5 * ((2 * v2) + (-v1 + v3) * t + (2 * v1 - 5 * v2 + 4 * v3 - v4) * t2 + (-v1 + 3 * v2 - 3 * v3 + v4) * t3);
    }

    private float interpolate(float t, float t2, float t3, float v1, float v2, float v3, float v4){
        return 0.5f * ((2 * v2) + (-v1 + v3) * t + (2 * v1 - 5 * v2 + 4 * v3 - v4) * t2 + (-v1 + 3 * v2 - 3 * v3 + v4) * t3);
    }

}
