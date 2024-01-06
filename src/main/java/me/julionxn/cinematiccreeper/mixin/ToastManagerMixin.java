package me.julionxn.cinematiccreeper.mixin;

import me.julionxn.cinematiccreeper.core.notifications.NotificationManager;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.toast.ToastManager;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ToastManager.class)
public class ToastManagerMixin {

    @Shadow @Final
    MinecraftClient client;

    @Inject(method = "draw", at = @At("TAIL"))
    private void renderNotifications(DrawContext context, CallbackInfo ci){
        NotificationManager.getInstance().draw(client.textRenderer, context);
    }

}
