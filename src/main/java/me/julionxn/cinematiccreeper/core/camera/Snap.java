package me.julionxn.cinematiccreeper.core.camera;

import com.google.gson.annotations.Expose;
import net.minecraft.util.math.Vec3d;

import java.util.Arrays;

public class Snap {

    @Expose public int tick;
    @Expose public final double[] pos;
    @Expose public final float yaw;
    @Expose public final float pitch;
    @Expose public final double zoom;
    @Expose public final double fov;

    public Snap(Vec3d pos, float yaw, float pitch, double zoom, double fov){
        this.pos = new double[]{pos.x, pos.y, pos.z};
        this.yaw = yaw;
        this.pitch = pitch;
        this.zoom = zoom;
        this.fov = fov;
    }

    public Vec3d getPos(){
        return getPos(pos);
    }

    private Vec3d getPos(double[] pos){
        return new Vec3d(pos[0], pos[1], pos[2]);
    }

    public Snap copy()  {
        return new Snap(new Vec3d(pos[0], pos[1], pos[2]), yaw, pitch, zoom, fov);
    }

    @Override
    public String toString() {
        return "Snap{" +
                "tick=" + tick +
                ", pos=" + Arrays.toString(pos) +
                ", yaw=" + yaw +
                ", pitch=" + pitch +
                ", zoom=" + zoom +
                ", fov=" + fov +
                '}';
    }
}
