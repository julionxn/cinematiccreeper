package me.julionxn.cinematiccreeper.screen.hud;

import me.julionxn.cinematiccreeper.CinematicCreeper;
import me.julionxn.cinematiccreeper.core.paths.PlayerPathHolder;
import me.julionxn.cinematiccreeper.keybinds.Keybindings;
import me.julionxn.cinematiccreeper.util.TextUtils;
import me.julionxn.cinematiccreeper.util.mixins.PlayerData;
import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.text.Text;
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
        PlayerData playerData = (PlayerData) player;
        PlayerPathHolder holder = playerData.cinematiccreeper$getPathHolder();
        PlayerPathHolder.State state = holder.state();
        if (state == PlayerPathHolder.State.NONE) return;

        String firstAction = TextUtils.parseKeybind(Keybindings.firstAction);
        String secondAction = TextUtils.parseKeybind(Keybindings.secondAction);
        String acceptAction = TextUtils.parseKeybind(Keybindings.thirdAction);

        context.drawTextWithShadow(client.textRenderer, "ESC: " + Text.translatable("hud.cinematiccreeper.exit").getString(),
                20, 20, 0xffffff);
        Identifier texture = state == PlayerPathHolder.State.ADDING ? ADDING_TEXTURE : RECORDING_TEXTURE;
        context.drawTexture(texture, context.getScaledWindowWidth() - 52,
                20, 0, 0, 0,
                32, 32, 32, 32);
        if (state == PlayerPathHolder.State.ADDING) {
            context.drawTextWithShadow(client.textRenderer, firstAction + ": " + Text.translatable("hud.cinematiccreeper.add_point").getString(),
                    20, 36, 0xffffff);
            context.drawTextWithShadow(client.textRenderer, secondAction + ": " + Text.translatable("hud.cinematiccreeper.remove_last_point").getString(),
                    20, 52, 0xffffff);
            context.drawTextWithShadow(client.textRenderer, acceptAction + ": " + Text.translatable("hud.cinematiccreeper.finish").getString(),
                    20, 68, 0xffffff);
        } else {
            //todo
        }

    }

}
