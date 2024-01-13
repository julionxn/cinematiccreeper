package me.julionxn.cinematiccreeper.mixin;

import me.julionxn.cinematiccreeper.core.camera.CameraRecordingPlayer;
import me.julionxn.cinematiccreeper.core.managers.CameraManager;
import me.julionxn.cinematiccreeper.keybinds.InputHandlersManager;
import me.julionxn.cinematiccreeper.screen.hud.RecordingHud;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.hud.InGameHud;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(InGameHud.class)
public abstract class InGameHudMixin {

    @Shadow @Final private MinecraftClient client;

    @Shadow protected abstract void renderCrosshair(DrawContext context);

    @Inject(method = "render", at = @At("HEAD"), cancellable = true)
    private void renderI(DrawContext context, float tickDelta, CallbackInfo ci){
        if (CameraManager.getInstance().isActive()) {
            ci.cancel();
            if (CameraManager.getInstance().isRecording()){
                RecordingHud.onHudRender(context, tickDelta);
            }
            if (CameraManager.getInstance().isSelectingTarget()){
                renderCrosshair(context);
            }
            CameraRecordingPlayer.hud.onHudRender(context);
        }
        InputHandlersManager.render(client, context);
    }

}
