package me.julionxn.cinematiccreeper.mixin;

import me.julionxn.cinematiccreeper.core.managers.CameraManager;
import net.minecraft.client.render.Camera;
import net.minecraft.client.render.GameRenderer;
import net.minecraft.client.util.math.MatrixStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(GameRenderer.class)
public abstract class GameRendererMixin {

    @Inject(method = "renderHand", at = @At("HEAD"), cancellable = true)
    private void renderHandI(MatrixStack matrices, Camera camera, float tickDelta, CallbackInfo ci){
        if (CameraManager.getInstance().isActive()) {
            ci.cancel();
        }
    }

    @Inject(method = "bobView", at = @At("HEAD"), cancellable = true)
    private void bobViewI(MatrixStack matrices, float tickDelta, CallbackInfo ci){
        if (CameraManager.getInstance().isActive()) {
            ci.cancel();
        }
    }

    @Inject(method = "getFov", at = @At("HEAD"), cancellable = true)
    private void fovI(Camera camera, float tickDelta, boolean changingFov, CallbackInfoReturnable<Double> cir){
        if (CameraManager.getInstance().isActive()) {
            cir.setReturnValue(CameraManager.getInstance().getFov());
            cir.cancel();
        }
    }

}
