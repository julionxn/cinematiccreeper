package me.julionxn.cinematiccreeper.core.camera.targets;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityPose;
import net.minecraft.util.math.Vec3d;

public class EntityTarget implements CameraTarget{

    private final Entity entity;
    private final float eyeHeight;

    public EntityTarget(Entity entity){
        this.entity = entity;
        this.eyeHeight = entity.getEyeHeight(EntityPose.STANDING);
    }

    @Override
    public Vec3d getPos() {
        return entity.getPos().add(0, eyeHeight, 0);
    }
}
