package me.julionxn.cinematiccreeper.core.camera.paths;

import me.julionxn.cinematiccreeper.core.camera.CameraRecording;
import me.julionxn.cinematiccreeper.core.camera.Snap;
import me.julionxn.cinematiccreeper.util.RenderUtils;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.math.Vec3d;

public class BSplinePath extends CurveCameraPath{

    public BSplinePath(CameraRecording recording) {
        super(recording);
    }

    @Override
    protected void renderCurve(MinecraftClient client, MatrixStack stack) {
        for (int i = 1; i < ordererSnaps.size() - 2; i++) {
            Snap p0 = ordererSnaps.get(i - 1);
            Snap p1 = ordererSnaps.get(i);
            Snap p2 = ordererSnaps.get(i + 1);
            Snap p3 = ordererSnaps.get(i + 2);
            float t = 0;
            Vec3d start = interpolateCurve(t, p0, p1, p2, p3).getPos();
            for (int j = 0; j <= 20; j++) {
                t = (float) j / 20;
                Vec3d result = interpolateCurve(t, p0, p1, p2, p3).getPos();
                RenderUtils.renderLine(client, stack, start, result, 0xff0000);
                start = result;
            }
        }
    }

    @Override
    protected Snap interpolateCurve(float t, Snap p0, Snap p1, Snap p2, Snap p3) {
        float t2 = t * t;
        float t3 = t2 * t;
        float u = 1.0f / 6.0f;
        float b0 = u * (-t3 + 3 * t2 - 3 * t + 1);
        float b1 = u * (3 * t3 - 6 * t2 + 4);
        float b2 = u * (-3 * t3 + 3 * t2 + 3 * t + 1);
        float b3 = u * t3;
        Vec3d pos0 = p0.getPos();
        Vec3d pos1 = p1.getPos();
        Vec3d pos2 = p2.getPos();
        Vec3d pos3 = p3.getPos();
        double x = interpolate(b0, b1, b2, b3, pos0.x, pos1.x, pos2.x, pos3.x);
        double y = interpolate(b0, b1, b2, b3, pos0.y, pos1.y, pos2.y, pos3.y);
        double z = interpolate(b0, b1, b2, b3, pos0.z, pos1.z, pos2.z, pos3.z);
        float yaw = interpolate(b0, b1, b2, b3, p0.yaw, p1.yaw, p2.yaw, p3.yaw);
        float pitch = interpolate(b0, b1, b2, b3, p0.pitch, p1.pitch, p2.pitch, p3.pitch);
        double fov = interpolate(b0, b1, b2, b3, p0.fov, p1.fov, p2.fov, p3.fov);
        double zoom = interpolate(b0, b1, b2, b3, p0.zoom, p1.zoom, p2.zoom, p3.zoom);
        return new Snap(new Vec3d(x, y, z), yaw, pitch, zoom, fov);
    }

    private double interpolate(float b0, float b1, float b2, float b3, double p0, double p1, double p2, double p3) {
        return b0 * p0 + b1 * p1 + b2 * p2 + b3 * p3;
    }

    private float interpolate(float b0, float b1, float b2, float b3, float p0, float p1, float p2, float p3) {
        return b0 * p0 + b1 * p1 + b2 * p2 + b3 * p3;
    }

}
