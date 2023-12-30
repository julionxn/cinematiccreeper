package me.julionxn.cinematiccreeper.mixin;

import me.julionxn.cinematiccreeper.util.mixins.MobNpcData;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.data.DataTracker;
import net.minecraft.entity.data.TrackedData;
import net.minecraft.entity.data.TrackedDataHandlerRegistry;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.world.World;
import org.joml.Vector3f;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(MobEntity.class)
public abstract class MobEntityMixin extends LivingEntity implements MobNpcData {

    @SuppressWarnings("all")
    @Unique
    private static final TrackedData<Vector3f> SPAWN_POSITION = DataTracker.registerData(MobEntity.class, TrackedDataHandlerRegistry.VECTOR3F);

    protected MobEntityMixin(EntityType<? extends LivingEntity> entityType, World world) {
        super(entityType, world);
    }

    @Inject(method = "initDataTracker", at = @At("TAIL"))
    private void dataTrackerI(CallbackInfo ci) {
        dataTracker.startTracking(SPAWN_POSITION, new Vector3f());
    }

    @Inject(method = "readCustomDataFromNbt", at = @At("TAIL"))
    private void readDataI(NbtCompound nbt, CallbackInfo ci) {
        NbtCompound compound = nbt.getCompound("SpawnPosition");
        Vector3f position = new Vector3f(compound.getFloat("x"),
                compound.getFloat("y"),
                compound.getFloat("z"));
        dataTracker.set(SPAWN_POSITION, position);
    }

    @Inject(method = "writeCustomDataToNbt", at = @At("TAIL"))
    private void writeDataI(NbtCompound nbt, CallbackInfo ci) {
        Vector3f spawnPosition = cinematiccreeper$getSpawnPosition();
        NbtCompound nbtCompound = new NbtCompound();
        nbtCompound.putFloat("x", spawnPosition.x);
        nbtCompound.putFloat("y", spawnPosition.y);
        nbtCompound.putFloat("z", spawnPosition.z);
        nbt.put("SpawnPosition", nbtCompound);
    }

    @Override
    public void cinematiccreeper$setSpawnPosition(Vector3f blockPos) {
        dataTracker.set(SPAWN_POSITION, blockPos);
    }

    @Override
    public Vector3f cinematiccreeper$getSpawnPosition() {
        return dataTracker.get(SPAWN_POSITION);
    }
}