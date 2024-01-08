package me.julionxn.cinematiccreeper.core.camera;

import com.google.gson.annotations.Expose;
import net.minecraft.util.math.Vec3d;

public class Snap {

    @Expose public final double[] pos;
    @Expose private double[] firstControlPos;
    @Expose private double[] secondControlPos;
    @Expose public final float yaw;
    @Expose public final float pitch;
    @Expose public final double zoom;
    @Expose public final double fov;

    public Snap(Vec3d pos, float yaw, float pitch, double zoom, double fov){
        this.pos = new double[]{pos.x, pos.y, pos.z};
        this.firstControlPos = this.pos;
        this.secondControlPos = this.pos;
        this.yaw = yaw;
        this.pitch = pitch;
        this.zoom = zoom;
        this.fov = fov;
    }

    public void adjustControlPoint(Snap prevPoint, Snap nextPoint) {
        double alpha = 1.0 / 3.0;
        double beta = 2.0 / 3.0;

        double sX = prevPoint.pos[0] + alpha * (pos[0] - prevPoint.pos[0]);
        double sY = prevPoint.pos[1] + alpha * (pos[1] - prevPoint.pos[1]);
        double sZ = prevPoint.pos[2] + alpha * (pos[2] - prevPoint.pos[2]);
        prevPoint.secondControlPos = new double[]{sX, sY, sZ};

        double fX = pos[0] + beta * (nextPoint.pos[0] - prevPoint.pos[0]);
        double fY = pos[1] + beta * (nextPoint.pos[1] - prevPoint.pos[1]);
        double fZ = pos[2] + beta * (nextPoint.pos[2] - prevPoint.pos[2]);
        firstControlPos = new double[]{fX, fY, fZ};
    }

    public void clearSecondControl(){
        secondControlPos = pos;
    }


}
