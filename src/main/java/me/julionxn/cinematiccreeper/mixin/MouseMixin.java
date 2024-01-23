package me.julionxn.cinematiccreeper.mixin;

import me.julionxn.cinematiccreeper.core.managers.CameraManager;
import me.julionxn.cinematiccreeper.inputs.InputHandlersManager;
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
public abstract class MouseMixin {

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
            manager.changeDirectionByMouse(dx, dy);
        }
    }

    @Inject(method = "onMouseButton", at = @At("TAIL"))
    private void rightClickI(long window, int button, int action, int mods, CallbackInfo ci){
        if (CameraManager.getInstance().isSelectingTarget()){
            if (action != 1) return;
            if (button == 1) CameraManager.getInstance().handleTargetSelection(client);
            if (button == 2) CameraManager.getInstance().setPlayerAsTarget(client);
        }
    }

    @Inject(method = "onMouseScroll", at = @At("TAIL"))
    private void scrollI(long window, double horizontal, double vertical, CallbackInfo ci){
        InputHandlersManager.handleScroll(client, vertical);
    }

}
