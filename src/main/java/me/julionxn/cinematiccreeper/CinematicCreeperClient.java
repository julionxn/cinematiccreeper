package me.julionxn.cinematiccreeper;

import me.julionxn.cinematiccreeper.entity.AllEntities;
import me.julionxn.cinematiccreeper.entity.NpcEntityRenderer;
import me.julionxn.cinematiccreeper.managers.PresetsManager;
import me.julionxn.cinematiccreeper.managers.skins.NpcSkinManager;
import me.julionxn.cinematiccreeper.networking.AllPackets;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientLifecycleEvents;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayConnectionEvents;
import net.fabricmc.fabric.api.client.rendering.v1.EntityRendererRegistry;

public class CinematicCreeperClient implements ClientModInitializer {

    @Override
    public void onInitializeClient() {
        PresetsManager.getInstance().load();
        NpcSkinManager.getInstance().load();
        EntityRendererRegistry.register(AllEntities.NPC_ENTITY, NpcEntityRenderer::new);
        AllPackets.registerS2CPackets();
        ClientPlayConnectionEvents.DISCONNECT.register((handler, client) -> {
            PresetsManager.getInstance().save();
            NpcSkinManager.getInstance().save();
        });
        ClientLifecycleEvents.CLIENT_STOPPING.register(client -> PresetsManager.getInstance().save());
    }

}
