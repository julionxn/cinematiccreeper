package me.julionxn.cinematiccreeper.mixin;


import me.julionxn.cinematiccreeper.util.mixins.NpcData;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.data.DataTracker;
import net.minecraft.entity.data.TrackedData;
import net.minecraft.entity.data.TrackedDataHandlerRegistry;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Entity.class)
public class EntityMixin implements NpcData {

    @Shadow @Final protected DataTracker dataTracker;
    @SuppressWarnings("all") @Unique
    private static final TrackedData<String> NPC = DataTracker.registerData(Entity.class, TrackedDataHandlerRegistry.STRING);


    @Inject(method = "<init>" , at = @At("TAIL"))
    private void init(EntityType type, World world, CallbackInfo ci){
        dataTracker.startTracking(NPC, "");
    }

    @Override
    public void cinematiccreeper$setNpc(String id) {
        dataTracker.set(NPC, id);
    }

    @Override
    public boolean cinematiccreeper$isNpc() {
        return !cinematiccreeper$getId().isEmpty();
    }

    @Override
    @Unique
    public String cinematiccreeper$getId() {
        return dataTracker.get(NPC);
    }
}
