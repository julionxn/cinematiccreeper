package me.julionxn.cinematiccreeper.core.camera.targets;

import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;

public class BlockPosTarget implements CameraTarget{

    private final Vec3d pos;

    public BlockPosTarget(BlockPos blockPos){
        this.pos = blockPos.toCenterPos();
    }

    @Override
    public Vec3d getPos() {
        return pos;
    }
}
