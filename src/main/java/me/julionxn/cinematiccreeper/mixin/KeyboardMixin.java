package me.julionxn.cinematiccreeper.mixin;

import me.julionxn.cinematiccreeper.inputs.InputHandlersManager;
import net.minecraft.client.Keyboard;
import net.minecraft.client.MinecraftClient;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Keyboard.class)
public abstract class KeyboardMixin {

    @Shadow
    @Final
    private MinecraftClient client;

    @Inject(method = "onKey", at = @At("TAIL"))
    public void handleInput(long window, int key, int scancode, int action, int modifiers, CallbackInfo ci) {
        InputHandlersManager.handleKeyboard(client, key, action, modifiers);
    }

}
