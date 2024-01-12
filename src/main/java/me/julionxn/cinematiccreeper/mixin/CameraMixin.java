package me.julionxn.cinematiccreeper.mixin;

import me.julionxn.cinematiccreeper.core.managers.CameraManager;
import net.minecraft.client.render.Camera;
import net.minecraft.entity.Entity;
import net.minecraft.world.BlockView;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Camera.class)
public abstract class CameraMixin {

    @Inject(method = "isThirdPerson", at = @At("TAIL"), cancellable = true)
    private void thirdI(CallbackInfoReturnable<Boolean> cir){
        if (CameraManager.getInstance().isActive()) {
            cir.setReturnValue(true);
        }
    }

    @Inject(method = "update", at = @At("HEAD"), cancellable = true)
    private void updateI(BlockView area, Entity focusedEntity, boolean thirdPerson, boolean inverseView, float tickDelta, CallbackInfo ci){
        if (CameraManager.getInstance().isActive()){
            CameraManager.getInstance().update((Camera) (Object) this);
            ci.cancel();
        }
    }

}
