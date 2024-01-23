package me.julionxn.cinematiccreeper.mixin;


import me.julionxn.cinematiccreeper.util.mixins.NpcData;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.data.DataTracker;
import net.minecraft.entity.data.TrackedData;
import net.minecraft.entity.data.TrackedDataHandlerRegistry;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Entity.class)
public abstract class EntityMixin implements NpcData {

    @SuppressWarnings("all")
    @Unique
    private static final TrackedData<String> NPC_ID = DataTracker.registerData(Entity.class, TrackedDataHandlerRegistry.STRING);
    @SuppressWarnings("all")
    @Unique
    private static final TrackedData<Boolean> VISUAL_FIRE = DataTracker.registerData(Entity.class, TrackedDataHandlerRegistry.BOOLEAN);

    @Shadow
    @Final
    protected DataTracker dataTracker;

    @Inject(method = "<init>", at = @At("TAIL"))
    private void init(EntityType<?> type, World world, CallbackInfo ci) {
        dataTracker.startTracking(NPC_ID, "");
        dataTracker.startTracking(VISUAL_FIRE, false);
    }

    @Inject(method = "readNbt", at = @At("TAIL"))
    private void readNbtI(NbtCompound nbt, CallbackInfo ci) {
        dataTracker.set(NPC_ID, nbt.getString("NpcId"));
        dataTracker.set(VISUAL_FIRE, nbt.getBoolean("VisualFire"));
    }

    @Inject(method = "writeNbt", at = @At("TAIL"))
    private void writeNbtI(NbtCompound nbt, CallbackInfoReturnable<NbtCompound> cir) {
        nbt.putString("NpcId", dataTracker.get(NPC_ID));
        nbt.putBoolean("VisualFire", dataTracker.get(VISUAL_FIRE));
    }

    @Inject(method = "isOnFire", at = @At("TAIL"), cancellable = true)
    private void visualFire(CallbackInfoReturnable<Boolean> cir){
        if (dataTracker.get(VISUAL_FIRE)){
            cir.setReturnValue(true);
        }
    }

    @Override
    public void cinematiccreeper$setId(String id) {
        dataTracker.set(NPC_ID, id);
    }

    @Override
    public boolean cinematiccreeper$isNpc() {
        return !dataTracker.get(NPC_ID).isEmpty();
    }

    @Override
    public String cinematiccreeper$getId() {
        return dataTracker.get(NPC_ID);
    }

    @Override
    public void cinematiccreeper$setOnFire(boolean state) {
        dataTracker.set(VISUAL_FIRE, state);
    }

    @Override
    public boolean cinematiccreeper$isOnFire() {
        return dataTracker.get(VISUAL_FIRE);
    }
}
