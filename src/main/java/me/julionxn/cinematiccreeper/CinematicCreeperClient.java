package me.julionxn.cinematiccreeper;

import me.julionxn.cinematiccreeper.core.camera.CameraRecording;
import me.julionxn.cinematiccreeper.core.managers.CameraManager;
import me.julionxn.cinematiccreeper.core.managers.NpcPosesManager;
import me.julionxn.cinematiccreeper.core.managers.NpcSkinManager;
import me.julionxn.cinematiccreeper.core.managers.PresetsManager;
import me.julionxn.cinematiccreeper.core.paths.PathRenderer;
import me.julionxn.cinematiccreeper.core.paths.PlayerPathHolder;
import me.julionxn.cinematiccreeper.entity.AllEntities;
import me.julionxn.cinematiccreeper.entity.NpcEntityRenderer;
import me.julionxn.cinematiccreeper.keybinds.Keybindings;
import me.julionxn.cinematiccreeper.networking.AllPackets;
import me.julionxn.cinematiccreeper.util.mixins.PlayerData;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientLifecycleEvents;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayConnectionEvents;
import net.fabricmc.fabric.api.client.rendering.v1.EntityRendererRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.WorldRenderEvents;
import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.player.PlayerEntity;

public class CinematicCreeperClient implements ClientModInitializer {

    @Override
    public void onInitializeClient() {
        PresetsManager.getInstance().load();
        NpcSkinManager.getInstance().load();
        NpcPosesManager.getInstance().load();
        CameraManager.getInstance().load();
        EntityRendererRegistry.register(AllEntities.NPC_ENTITY, NpcEntityRenderer::new);
        AllPackets.registerS2CPackets();
        Keybindings.register();
        WorldRenderEvents.END.register(context -> {
            MinecraftClient client = MinecraftClient.getInstance();
            if (client == null) return;
            PlayerEntity player = client.player;
            if (player == null) return;
            PlayerPathHolder holder = ((PlayerData) player).cinematiccreeper$getPathHolder();
            if (holder.state() != PlayerPathHolder.State.NONE) {
                PathRenderer.render(client, context, holder);
            }
            CameraRecording recording = CameraManager.getInstance().getCurrentCameraRecording();
            if (recording == null) return;
            recording.getPath().render(client, context.matrixStack());
        });
        ClientTickEvents.END_CLIENT_TICK.register((client) -> {
            CameraManager.getInstance().tick();
        });
        ClientPlayConnectionEvents.DISCONNECT.register((handler, client) -> {
            PresetsManager.getInstance().save();
            NpcSkinManager.getInstance().save();
            NpcPosesManager.getInstance().save();
            CameraManager.getInstance().save();
        });
        ClientLifecycleEvents.CLIENT_STOPPING.register(client -> {
            PresetsManager.getInstance().save();
            NpcSkinManager.getInstance().save();
            NpcPosesManager.getInstance().save();
            CameraManager.getInstance().save();
        });
    }

}
