package me.julionxn.cinematiccreeper.mixin;

import me.julionxn.cinematiccreeper.core.managers.CameraManager;
import me.julionxn.cinematiccreeper.keybinds.InputHandlersManager;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.Mouse;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.entity.player.PlayerEntity;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Mouse.class)
public class MouseMixin {

    @Shadow @Final private MinecraftClient client;

    @Redirect(method = "updateMouse", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/network/ClientPlayerEntity;changeLookDirection(DD)V"))
    private void cancelUpdate(ClientPlayerEntity instance, double dx, double dy){
        PlayerEntity player = client.player;
        if (player != null){
            CameraManager manager = CameraManager.getInstance();
            CameraManager.State state = manager.getState();
            if (state != CameraManager.State.MOVING){
                player.changeLookDirection(dx, dy);
                return;
            }
            manager.changeDirection(dx, dy);
        }
    }

    @Inject(method = "onMouseScroll", at = @At("TAIL"))
    private void scrollI(long window, double horizontal, double vertical, CallbackInfo ci){
        InputHandlersManager.handleScroll(client, vertical);
    }

}
