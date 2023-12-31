package me.julionxn.cinematiccreeper.core.paths;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.util.math.Vec3d;
import org.jetbrains.annotations.Nullable;

public class PathAction {

    public static final int SNEAK_FLAG = 0;
    public static final int HIT_FLAG = 1;

    private final double[] pos = new double[3];
    private final Float[] angles = new Float[2];
    private byte flags = 0;

    public PathAction(double x, double y, double z) {
        this(x, y, z, null, null);
    }

    public PathAction(double x, double y, double z, Float yaw, Float pitch) {
        pos[0] = x;
        pos[1] = y;
        pos[2] = z;
        angles[0] = yaw;
        angles[1] = pitch;
    }

    public static PathAction getCurrentPathAction(PlayerEntity player, boolean angles) {
        Vec3d pos = player.getPos();
        boolean sneaking = player.isSneaking();
        boolean hit = player.handSwinging;
        PathAction pathAction = new PathAction(pos.x, pos.y, pos.z);
        pathAction.setFlag(PathAction.SNEAK_FLAG, sneaking);
        pathAction.setFlag(PathAction.HIT_FLAG, hit);
        if (angles) {
            pathAction.angles[0] = player.getYaw();
            pathAction.angles[1] = player.getPitch();
        }
        return pathAction;
    }

    public static void addToBuf(PacketByteBuf buf, PathAction pathAction) {
        buf.writeDouble(pathAction.pos[0]);
        buf.writeDouble(pathAction.pos[1]);
        buf.writeDouble(pathAction.pos[2]);
        buf.writeByte(pathAction.flags);
        if (pathAction.getYaw() != null && pathAction.getPitch() != null) {
            buf.writeBoolean(true);
            buf.writeFloat(pathAction.getYaw());
            buf.writeFloat(pathAction.getPitch());
        } else {
            buf.writeBoolean(false);
        }
    }

    public static PathAction fromBuf(PacketByteBuf buf) {
        PathAction pathAction = new PathAction(buf.readDouble(), buf.readDouble(), buf.readDouble());
        pathAction.flags = buf.readByte();
        boolean addAngles = buf.readBoolean();
        if (addAngles){
            pathAction.angles[0] = buf.readFloat();
            pathAction.angles[1] = buf.readFloat();
        }
        return pathAction;
    }

    @Nullable
    public Float getYaw() {
        return angles[0];
    }

    @Nullable
    public Float getPitch() {
        return angles[1];
    }

    public boolean getFlag(int flag) {
        return ((flags >> flag) & 1) == 1;
    }

    public Vec3d getPos() {
        return new Vec3d(pos[0], pos[1], pos[2]);
    }

    public boolean getSneak() {
        return getFlag(SNEAK_FLAG);
    }

    public boolean getHit() {
        return getFlag(HIT_FLAG);
    }

    public void setFlag(int flag, boolean value) {
        if (value) {
            flags |= (byte) (1 << flag);
        } else {
            flags &= (byte) ~(1 << flag);
        }
    }

}
