package me.julionxn.cinematiccreeper.mixin;

import me.julionxn.cinematiccreeper.managers.paths.PlayerPathState;
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
public class MinecraftClientMixin {

    @Shadow
    @Nullable
    public ClientPlayerEntity player;

    @Inject(method = "openGameMenu", at = @At("HEAD"), cancellable = true)
    public void cancelESC(boolean pauseOnly, CallbackInfo ci) {
        if (player == null) return;
        PlayerData accessor = (PlayerData) player;
        if (accessor.cinematiccreeper$getPathState().state() == PlayerPathState.State.NONE) return;
        ci.cancel();
    }

}
