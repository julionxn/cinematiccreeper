package me.julionxn.cinematiccreeper.mixin;

import me.julionxn.cinematiccreeper.core.managers.CameraManager;
import me.julionxn.cinematiccreeper.core.paths.PlayerPathHolder;
import me.julionxn.cinematiccreeper.util.mixins.PlayerData;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerEntity;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(MinecraftClient.class)
public abstract class MinecraftClientMixin {

    @Shadow
    @Nullable
    public ClientPlayerEntity player;

    @Inject(method = "openGameMenu", at = @At("HEAD"), cancellable = true)
    public void cancelESC(boolean pauseOnly, CallbackInfo ci) {
        if (player == null) return;
        boolean cancel = false;
        PlayerData accessor = (PlayerData) player;
        if (accessor.cinematiccreeper$getPathHolder().state() != PlayerPathHolder.State.NONE) {
            cancel = true;
        }
        if (CameraManager.getInstance().isRecording()){
            cancel = true;
        }
        if (cancel) ci.cancel();
    }

}
