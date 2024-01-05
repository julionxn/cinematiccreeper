package me.julionxn.cinematiccreeper.networking.packets;

import me.julionxn.cinematiccreeper.core.presets.PresetOptions;
import me.julionxn.cinematiccreeper.core.presets.PresetOptionsHandlers;
import me.julionxn.cinematiccreeper.util.mixins.MobNpcData;
import net.fabricmc.fabric.api.networking.v1.PacketSender;
import net.minecraft.entity.Entity;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayNetworkHandler;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.Vec3d;
import org.joml.Vector3f;

import java.util.UUID;

public class C2S_ApplyPresetOptions {
    public static void onServer(MinecraftServer server, ServerPlayerEntity player,
                                ServerPlayNetworkHandler serverPlayNetworkHandler, PacketByteBuf buf, PacketSender sender) {

        UUID uuid = buf.readUuid();
        boolean reset = buf.readBoolean();
        PresetOptions presetOptions = PresetOptionsHandlers.fromBuf(buf);
        server.execute(() -> {
            ServerWorld serverWorld = (ServerWorld) player.getWorld();
            Entity entity = serverWorld.getEntity(uuid);
            if (entity == null) return;
            PresetOptionsHandlers.applyPresetOptions(entity, presetOptions);
            if (reset && entity instanceof MobEntity mob) {
                Vector3f position = ((MobNpcData) mob).cinematiccreeper$getSpawnPosition();
                mob.setPosition(new Vec3d(position));
            }
        });

    }
}
