package me.julionxn.cinematiccreeper.networking.packets;

import me.julionxn.cinematiccreeper.entity.AllEntities;
import me.julionxn.cinematiccreeper.entity.NpcEntity;
import me.julionxn.cinematiccreeper.managers.NpcsManager;
import me.julionxn.cinematiccreeper.managers.presets.PresetOptions;
import me.julionxn.cinematiccreeper.util.mixins.NpcData;
import net.fabricmc.fabric.api.networking.v1.PacketSender;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayNetworkHandler;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class C2S_SpawnNpcPreset {

    public static void onServer(MinecraftServer server, ServerPlayerEntity player,
                                ServerPlayNetworkHandler serverPlayNetworkHandler, PacketByteBuf buf, PacketSender sender) {

        BlockPos blockPos = buf.readBlockPos();
        String id = buf.readString();
        String skin = buf.readString();
        PresetOptions presetOptions = PresetOptions.fromBuf(buf);
        server.execute(() -> {
            World world = player.getWorld();
            NpcEntity entity = new NpcEntity(AllEntities.NPC_ENTITY, world);
            entity.setNpcId(id);
            entity.setPosition(blockPos.toCenterPos());
            entity.setSkinUrl(skin);
            ((NpcData) entity).cinematiccreeper$setNpc(true);
            PresetOptions.applyPresetOptions(entity, presetOptions);
            world.spawnEntity(entity);
            NpcsManager.getInstance().trackEntity((ServerWorld) world, entity);
        });

    }

}
