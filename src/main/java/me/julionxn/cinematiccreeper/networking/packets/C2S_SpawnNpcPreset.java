package me.julionxn.cinematiccreeper.networking.packets;

import me.julionxn.cinematiccreeper.entity.AllEntities;
import me.julionxn.cinematiccreeper.entity.NpcEntity;
import net.fabricmc.fabric.api.networking.v1.PacketSender;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayNetworkHandler;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class C2S_SpawnNpcPreset {

    public static void onServer(MinecraftServer server, ServerPlayerEntity player,
                                ServerPlayNetworkHandler serverPlayNetworkHandler, PacketByteBuf buf, PacketSender sender) {

        BlockPos blockPos = buf.readBlockPos();
        String id = buf.readString();
        String skin = buf.readString();
        server.execute(() -> {
            World world = player.getWorld();
            NpcEntity entity = new NpcEntity(AllEntities.NPC_ENTITY, world);
            entity.setNpcId(id);
            entity.setPosition(blockPos.toCenterPos());
            entity.setCustomName(Text.of(id));
            entity.setSkinUrl(skin);
            world.spawnEntity(entity);
        });

    }

}
