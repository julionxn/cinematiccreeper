package me.julionxn.cinematiccreeper.networking.packets;

import me.julionxn.cinematiccreeper.managers.NpcsManager;
import me.julionxn.cinematiccreeper.util.mixins.NpcData;
import net.fabricmc.fabric.api.networking.v1.PacketSender;
import net.minecraft.entity.Entity;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayNetworkHandler;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.text.Text;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class C2S_SpawnPreset {

    public static void onServer(MinecraftServer server, ServerPlayerEntity player,
                                ServerPlayNetworkHandler serverPlayNetworkHandler, PacketByteBuf buf, PacketSender sender) {

        BlockPos blockPos = buf.readBlockPos();
        String id = buf.readString();
        String entityType = buf.readString();
        server.execute(() -> {
            World world = player.getWorld();
            Entity entity = NpcsManager.getInstance().getEntityTypeFromId(entityType).create(world);
            if (entity == null) return;
            entity.setPosition(blockPos.toCenterPos());
            entity.setCustomName(Text.of(id));
            if (entity instanceof MobEntity mobEntity){
                mobEntity.setAiDisabled(true);
            }
            ((NpcData) entity).cinematiccreeper$setNpc(true);
            world.spawnEntity(entity);
            NpcsManager.getInstance().trackEntity((ServerWorld) world, entity);
        });

    }

}
