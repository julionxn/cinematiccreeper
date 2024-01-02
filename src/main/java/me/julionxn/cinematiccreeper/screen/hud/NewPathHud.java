package me.julionxn.cinematiccreeper.screen.hud;

import me.julionxn.cinematiccreeper.CinematicCreeper;
import me.julionxn.cinematiccreeper.keybinds.Keybindings;
import me.julionxn.cinematiccreeper.util.TextUtils;
import me.julionxn.cinematiccreeper.util.mixins.PlayerData;
import me.julionxn.cinematiccreeper.managers.paths.PlayerPathState;
import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.Identifier;

public class NewPathHud implements HudRenderCallback {

    private static final Identifier ADDING_TEXTURE = new Identifier(CinematicCreeper.MOD_ID, "textures/hud/adding.png");
    private static final Identifier RECORDING_TEXTURE = new Identifier(CinematicCreeper.MOD_ID, "textures/hud/recording.png");

    @Override
    public void onHudRender(DrawContext context, float tickDelta) {
        MinecraftClient client = MinecraftClient.getInstance();
        if (client == null) return;
        PlayerEntity player = client.player;
        if (player == null) return;
        PlayerData playerData  = (PlayerData) player;
        PlayerPathState pathState = playerData.cinematiccreeper$getPathState();
        PlayerPathState.State state = pathState.state();
        if (state == PlayerPathState.State.NONE) return;

        String firstAction = TextUtils.parseKeybind(Keybindings.firstAction).toUpperCase();
        String secondAction = TextUtils.parseKeybind(Keybindings.secondAction).toUpperCase();
        String acceptAction = TextUtils.parseKeybind(Keybindings.acceptAction).toUpperCase();

        context.drawTextWithShadow(client.textRenderer, "ESC: Salir",
                20, 20, 0xffffff);
        Identifier texture = state == PlayerPathState.State.ADDING ? ADDING_TEXTURE : RECORDING_TEXTURE;
        context.drawTexture(texture, context.getScaledWindowWidth() - 52,
                20, 0, 0, 0,
                32, 32, 32, 32);
        if (state == PlayerPathState.State.ADDING){
            context.drawTextWithShadow(client.textRenderer, firstAction + ": Añadir punto",
                    20, 36, 0xffffff);
            context.drawTextWithShadow(client.textRenderer, secondAction + ": Eliminar último punto",
                    20, 52, 0xffffff);
            context.drawTextWithShadow(client.textRenderer, acceptAction + ": Terminar",
                    20, 68, 0xffffff);
        } else {

        }



    }

}