package me.julionxn.cinematiccreeper.mixin;

import me.julionxn.cinematiccreeper.keybinds.Keybindings;
import me.julionxn.cinematiccreeper.managers.paths.PathInputHandlers;
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
public class KeyboardMixin {

    @Shadow
    @Final
    private MinecraftClient client;

    @Inject(method = "onKey", at = @At("TAIL"))
    public void handleInput(long window, int key, int scancode, int action, int modifiers, CallbackInfo ci){
        if (action != 1) return;
        if (key == 256) {
            PlayerEntity player = client.player;
            if (player == null) return;
            PlayerData playerData = (PlayerData) player;
            if (playerData.cinematiccreeper$getPathState().state() != PlayerPathState.State.NONE){
                playerData.cinematiccreeper$setPathState(PlayerPathState.none());
            }
            return;
        }
        if (Keybindings.firstAction.matchesKey(key, scancode)){
            PathInputHandlers.handleFirstAction(client);
            return;
        }
        if (Keybindings.secondAction.matchesKey(key, scancode)){
            PathInputHandlers.handleSecondAction(client);
        }
        if (Keybindings.acceptAction.matchesKey(key, scancode)){
            PathInputHandlers.handleAcceptAction(client);
        }
    }

}
