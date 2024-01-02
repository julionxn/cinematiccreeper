package me.julionxn.cinematiccreeper.networking.packets;

import me.julionxn.cinematiccreeper.managers.paths.Path;
import me.julionxn.cinematiccreeper.util.mixins.PathAwareData;
import net.fabricmc.fabric.api.networking.v1.PacketSender;
import net.minecraft.entity.Entity;
import net.minecraft.entity.mob.PathAwareEntity;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayNetworkHandler;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

public class C2S_ClearPathOfEntity {
    public static void onServer(MinecraftServer server, ServerPlayerEntity player,
                                ServerPlayNetworkHandler serverPlayNetworkHandler, PacketByteBuf buf, PacketSender sender) {

        int id = buf.readInt();
        server.execute(() -> {
            World world = player.getWorld();
            Entity entity = world.getEntityById(id);
            if (!(entity instanceof PathAwareEntity)) return;
            ((PathAwareData) entity).cinematiccreeper$setPath(null);
        });

    }
}
