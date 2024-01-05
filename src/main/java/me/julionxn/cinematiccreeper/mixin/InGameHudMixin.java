package me.julionxn.cinematiccreeper.mixin;

import me.julionxn.cinematiccreeper.core.managers.CameraManager;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.hud.InGameHud;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(InGameHud.class)
public abstract class InGameHudMixin {

    @Inject(method = "render", at = @At("HEAD"), cancellable = true)
    private void renderI(DrawContext context, float tickDelta, CallbackInfo ci){
        if (CameraManager.getInstance().isActive()) {
            ci.cancel();
        }
    }

}
