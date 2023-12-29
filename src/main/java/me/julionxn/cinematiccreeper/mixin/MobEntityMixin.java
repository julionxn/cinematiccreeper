package me.julionxn.cinematiccreeper.mixin;

import me.julionxn.cinematiccreeper.util.mixins.NpcData;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.data.DataTracker;
import net.minecraft.entity.data.TrackedData;
import net.minecraft.entity.data.TrackedDataHandlerRegistry;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(MobEntity.class)
public abstract class MobEntityMixin extends LivingEntity implements NpcData {

    @SuppressWarnings("all")
    @Unique
    private static final TrackedData<Boolean> NPC = DataTracker.registerData(MobEntity.class, TrackedDataHandlerRegistry.BOOLEAN);

    protected MobEntityMixin(EntityType<? extends LivingEntity> entityType, World world) {
        super(entityType, world);
    }

    @Inject(method = "initDataTracker", at = @At("TAIL"))
    private void dataTrackerI(CallbackInfo ci){
        dataTracker.startTracking(NPC, false);
    }

    @Inject(method = "readCustomDataFromNbt", at = @At("TAIL"))
    private void readDataI(NbtCompound nbt, CallbackInfo ci){
        dataTracker.set(NPC, nbt.getBoolean("Npc"));
    }

    @Inject(method = "writeCustomDataToNbt", at = @At("TAIL"))
    private void writeDataI(NbtCompound nbt, CallbackInfo ci){
        nbt.putBoolean("Npc", dataTracker.get(NPC));
    }

    @Override
    public boolean cinematiccreeper$isNpc() {
        return dataTracker.get(NPC);
    }

    @Override
    public void cinematiccreeper$setNpc(boolean npc) {
        dataTracker.set(NPC, npc);
    }
}