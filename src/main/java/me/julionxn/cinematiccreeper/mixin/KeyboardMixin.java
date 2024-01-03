package me.julionxn.cinematiccreeper.mixin;

import me.julionxn.cinematiccreeper.keybinds.InputHandlers;
import me.julionxn.cinematiccreeper.keybinds.Keybindings;
import me.julionxn.cinematiccreeper.managers.paths.PlayerPathState;
import me.julionxn.cinematiccreeper.util.mixins.PlayerData;
import net.minecraft.client.Keyboard;
import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.player.PlayerEntity;
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
        if (action != 1) return;
        if (key == 256) {
            PlayerEntity player = client.player;
            if (player == null) return;
            PlayerData playerData = (PlayerData) player;
            if (playerData.cinematiccreeper$getPathState().state() != PlayerPathState.State.NONE) {
                playerData.cinematiccreeper$setPathState(PlayerPathState.none());
            }
            return;
        }
        if (Keybindings.firstAction.matchesKey(key, scancode)) {
            InputHandlers.handleFirstAction(client);
            return;
        }
        if (Keybindings.secondAction.matchesKey(key, scancode)) {
            InputHandlers.handleSecondAction(client);
            return;
        }
        if (Keybindings.thirdAction.matchesKey(key, scancode)) {
            InputHandlers.handleThirdAction(client);
            return;
        }
        if (Keybindings.fourthAction.matchesKey(key, scancode)) {
            InputHandlers.handleFourthAction(client);
            return;
        }
    }

}
