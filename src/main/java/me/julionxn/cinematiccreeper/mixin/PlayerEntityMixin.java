package me.julionxn.cinematiccreeper.mixin;

import me.julionxn.cinematiccreeper.core.managers.NpcSkinManager;
import me.julionxn.cinematiccreeper.core.paths.PlayerPathHolder;
import me.julionxn.cinematiccreeper.util.mixins.PlayerData;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.data.DataTracker;
import net.minecraft.entity.data.TrackedData;
import net.minecraft.entity.data.TrackedDataHandlerRegistry;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.util.Identifier;
import net.minecraft.world.World;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.List;

@Mixin(PlayerEntity.class)
public abstract class PlayerEntityMixin extends LivingEntity implements PlayerData {

    private @Unique PlayerPathHolder playerPathHolder = PlayerPathHolder.none();
    @SuppressWarnings("all")
    private static final @Unique TrackedData<String> SKIN_URL = DataTracker.registerData(PlayerEntity.class, TrackedDataHandlerRegistry.STRING);
    private @Unique @Nullable Identifier texture;

    protected PlayerEntityMixin(EntityType<? extends LivingEntity> entityType, World world) {
        super(entityType, world);
    }

    @Inject(method = "initDataTracker", at = @At("TAIL"))
    private void data(CallbackInfo ci){
        dataTracker.startTracking(SKIN_URL, "");
    }

    @Inject(method = "readCustomDataFromNbt", at = @At("TAIL"))
    private void read(NbtCompound nbt, CallbackInfo ci){
        dataTracker.set(SKIN_URL, nbt.getString("SkinUrl"));
    }

    @Inject(method = "writeCustomDataToNbt", at = @At("TAIL"))
    private void write(NbtCompound nbt, CallbackInfo ci){
        nbt.putString("SkinUrl", dataTracker.get(SKIN_URL));
    }

    @Override
    public void onDataTrackerUpdate(List<DataTracker.SerializedEntry<?>> dataEntries) {
        dataEntries.forEach(entry -> {
            if (entry.id() == SKIN_URL.getId()) {
                NpcSkinManager.getInstance().updateSkinOf((PlayerEntity)(Object)this);
            }
        });
        super.onDataTrackerUpdate(dataEntries);
    }

    @Override
    @Nullable
    public Identifier cinematiccreeper$getSkin() {
        return texture;
    }

    @Override
    public void cinematiccreeper$setSkin(@NotNull Identifier texture) {
        this.texture = texture;
    }

    @Override
    public void cinematiccreeper$setSkinUrl(String url) {
        dataTracker.set(SKIN_URL, url);
    }

    @Override
    public String cinematiccreeper$getSkinUrl() {
        return dataTracker.get(SKIN_URL);
    }

    @Override
    public void cinematiccreeper$setPathHolder(PlayerPathHolder state) {
        this.playerPathHolder = state;
    }

    @Override
    public PlayerPathHolder cinematiccreeper$getPathHolder() {
        return playerPathHolder;
    }

}
