package me.julionxn.cinematiccreeper.mixin;

import me.julionxn.cinematiccreeper.managers.CameraManager;
import net.minecraft.client.render.Camera;
import net.minecraft.util.math.Vec3d;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Camera.class)
public class CameraMixin {

    @Inject(method = "setPos(Lnet/minecraft/util/math/Vec3d;)V", at = @At("HEAD"), cancellable = true)
    private void moveByI(Vec3d pos, CallbackInfo ci){
        if (CameraManager.getInstance().isActive()) {
            ci.cancel();
        }
    }

    @Inject(method = "setRotation", at = @At("HEAD"), cancellable = true)
    private void setRotationI(float yaw, float pitch, CallbackInfo ci){
        if (CameraManager.getInstance().isActive()) {
            ci.cancel();
        }
    }

    @Inject(method = "isThirdPerson", at = @At("TAIL"), cancellable = true)
    private void thirdI(CallbackInfoReturnable<Boolean> cir){
        if (CameraManager.getInstance().isActive()) {
            cir.setReturnValue(true);
        }
    }

}
