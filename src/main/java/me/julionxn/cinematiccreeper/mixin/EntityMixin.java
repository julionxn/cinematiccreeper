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
public class EntityMixin implements NpcData {

    @SuppressWarnings("all")
    @Unique
    private static final TrackedData<String> NPC_ID = DataTracker.registerData(Entity.class, TrackedDataHandlerRegistry.STRING);
    @Shadow
    @Final
    protected DataTracker dataTracker;

    @Inject(method = "<init>", at = @At("TAIL"))
    private void init(EntityType type, World world, CallbackInfo ci) {
        dataTracker.startTracking(NPC_ID, "");
    }

    @Inject(method = "readNbt", at = @At("TAIL"))
    private void readNbtI(NbtCompound nbt, CallbackInfo ci) {
        dataTracker.set(NPC_ID, nbt.getString("NpcId"));
    }

    @Inject(method = "writeNbt", at = @At("TAIL"))
    private void writeNbtI(NbtCompound nbt, CallbackInfoReturnable<NbtCompound> cir) {
        nbt.putString("NpcId", dataTracker.get(NPC_ID));
    }

    @Override
    public void cinematiccreeper$setId(String id) {
        dataTracker.set(NPC_ID, id);
    }

    @Override
    public boolean cinematiccreeper$isNpc() {
        return !cinematiccreeper$getId().isEmpty();
    }

    @Override
    @Unique
    public String cinematiccreeper$getId() {
        return dataTracker.get(NPC_ID);
    }
}
