package me.julionxn.cinematiccreeper.mixin;

import me.julionxn.cinematiccreeper.core.managers.CameraManager;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.input.Input;
import net.minecraft.client.network.ClientPlayerEntity;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ClientPlayerEntity.class)
public abstract class ClientPlayerEntityMixin {

    @Shadow @Final protected MinecraftClient client;

    @Shadow public Input input;

    @Inject(method = "tick", at = @At("HEAD"), cancellable = true)
    private void tickI(CallbackInfo ci){
        if (CameraManager.getInstance().getState() == CameraManager.State.MOVING){
            ci.cancel();
            input.tick(false, 0);
            CameraManager.getInstance().moveByKeyboard(input.movementSideways, input.movementForward, input.jumping, input.sneaking);
        }
    }

}
