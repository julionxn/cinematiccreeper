package me.julionxn.cinematiccreeper.networking.packets;

import net.fabricmc.fabric.api.networking.v1.PacketSender;
import net.minecraft.entity.Entity;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayNetworkHandler;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;

import java.util.UUID;

public class C2S_RemoveEntity {

    public static void onServer(MinecraftServer server, ServerPlayerEntity player,
                                ServerPlayNetworkHandler serverPlayNetworkHandler, PacketByteBuf buf, PacketSender sender) {

        UUID uuid = buf.readUuid();
        server.execute(() -> {
            ServerWorld world = (ServerWorld) player.getWorld();
            Entity entity = world.getEntity(uuid);
            if (entity == null) return;
            entity.remove(Entity.RemovalReason.DISCARDED);
        });

    }

}
