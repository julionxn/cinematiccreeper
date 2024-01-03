package me.julionxn.cinematiccreeper;

import me.julionxn.cinematiccreeper.entity.AllEntities;
import me.julionxn.cinematiccreeper.entity.NpcEntityRenderer;
import me.julionxn.cinematiccreeper.keybinds.Keybindings;
import me.julionxn.cinematiccreeper.managers.NpcSkinManager;
import me.julionxn.cinematiccreeper.managers.PresetsManager;
import me.julionxn.cinematiccreeper.managers.paths.PathRenderer;
import me.julionxn.cinematiccreeper.managers.paths.PlayerPathState;
import me.julionxn.cinematiccreeper.networking.AllPackets;
import me.julionxn.cinematiccreeper.screen.hud.NewPathHud;
import me.julionxn.cinematiccreeper.util.mixins.PlayerData;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientLifecycleEvents;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayConnectionEvents;
import net.fabricmc.fabric.api.client.rendering.v1.EntityRendererRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback;
import net.fabricmc.fabric.api.client.rendering.v1.WorldRenderEvents;
import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.player.PlayerEntity;

public class CinematicCreeperClient implements ClientModInitializer {

    @Override
    public void onInitializeClient() {
        PresetsManager.getInstance().load();
        NpcSkinManager.getInstance().load();
        EntityRendererRegistry.register(AllEntities.NPC_ENTITY, NpcEntityRenderer::new);
        AllPackets.registerS2CPackets();
        Keybindings.register();
        HudRenderCallback.EVENT.register(new NewPathHud());
        WorldRenderEvents.END.register(context -> {
            MinecraftClient client = MinecraftClient.getInstance();
            if (client == null) return;
            PlayerEntity player = client.player;
            if (player == null) return;
            PlayerPathState state = ((PlayerData) player).cinematiccreeper$getPathState();
            if (state.state() == PlayerPathState.State.NONE) return;
            PathRenderer.render(context, state);
        });
        ClientPlayConnectionEvents.DISCONNECT.register((handler, client) -> {
            PresetsManager.getInstance().save();
            NpcSkinManager.getInstance().save();
        });
        ClientLifecycleEvents.CLIENT_STOPPING.register(client -> PresetsManager.getInstance().save());
    }

}
