package me.julionxn.cinematiccreeper.inputs.handlers;

import me.julionxn.cinematiccreeper.core.notifications.Notification;
import me.julionxn.cinematiccreeper.core.notifications.NotificationManager;
import me.julionxn.cinematiccreeper.inputs.InputAction;
import me.julionxn.cinematiccreeper.inputs.InputHandler;
import me.julionxn.cinematiccreeper.inputs.Keybindings;
import me.julionxn.cinematiccreeper.networking.AllPackets;
import me.julionxn.cinematiccreeper.screen.gui.screens.skins.SkinClosetMenu;
import me.julionxn.cinematiccreeper.util.mixins.PlayerData;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.minecraft.client.network.AbstractClientPlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

public class ChangeSkinHandler extends InputHandler {

    @Override
    public void init() {
        addPressAction(new InputAction(Keybindings.changeSkin, Text.of(""), (client, modifier) -> {
            PlayerData data = (PlayerData) client.player;
            if (data == null) return;
            String skinUrl = data.cinematiccreeper$getSkinUrl();
            Identifier defaultTexture = ((AbstractClientPlayerEntity) data).getSkinTextures().texture();
            Identifier startTexture;
            if (skinUrl.isEmpty()){
                startTexture = defaultTexture;
            } else {
                startTexture = data.cinematiccreeper$getSkin();
            }
            client.setScreen(new SkinClosetMenu(null, (name, skin) -> {
                String currentSkinUrl = data.cinematiccreeper$getSkinUrl();
                client.setScreen(null);
                String url = skin == null ? "" : skin.getUrl().toString();
                if (currentSkinUrl.equals(url)) return;
                ClientPlayNetworking.send(AllPackets.C2S_CHANGE_PLAYER_SKIN, PacketByteBufs.create().writeString(url));
                NotificationManager.getInstance().add(Notification.SAVED);
            }, startTexture, defaultTexture));
        }, () -> true));
    }

    @Override
    public boolean shouldRender() {
        return false;
    }

    @Override
    public boolean shouldCancelNext() {
        return false;
    }
}
