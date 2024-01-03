package me.julionxn.cinematiccreeper.networking.packets;

import me.julionxn.cinematiccreeper.managers.NpcsManager;
import me.julionxn.cinematiccreeper.managers.presets.PresetOptions;
import me.julionxn.cinematiccreeper.managers.presets.PresetOptionsHandlers;
import me.julionxn.cinematiccreeper.util.mixins.MobNpcData;
import me.julionxn.cinematiccreeper.util.mixins.NpcData;
import net.fabricmc.fabric.api.networking.v1.PacketSender;
import net.minecraft.entity.Entity;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayNetworkHandler;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

public class C2S_SpawnPreset {

    public static void onServer(MinecraftServer server, ServerPlayerEntity player,
                                ServerPlayNetworkHandler serverPlayNetworkHandler, PacketByteBuf buf, PacketSender sender) {

        BlockPos blockPos = buf.readBlockPos();
        String id = buf.readString();
        String entityType = buf.readString();
        PresetOptions presetOptions = PresetOptionsHandlers.fromBuf(buf);
        server.execute(() -> {
            World world = player.getWorld();
            Entity entity = NpcsManager.getInstance().getEntityTypeFromId(entityType).create(world);
            if (entity == null) return;
            Vec3d spawnPosition = blockPos.toCenterPos().subtract(0, 0.5, 0);
            entity.setPosition(spawnPosition);
            ((NpcData) entity).cinematiccreeper$setId(id);
            if (entity instanceof MobEntity mobEntity) {
                mobEntity.setAiDisabled(true);
                ((MobNpcData) mobEntity).cinematiccreeper$setSpawnPosition(spawnPosition.toVector3f());
            }
            PresetOptionsHandlers.applyPresetOptions(entity, presetOptions);
            world.spawnEntity(entity);
        });

    }

}
