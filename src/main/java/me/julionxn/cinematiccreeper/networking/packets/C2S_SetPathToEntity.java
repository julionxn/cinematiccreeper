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

public class C2S_SetPathToEntity {
    public static void onServer(MinecraftServer server, ServerPlayerEntity player,
                                ServerPlayNetworkHandler serverPlayNetworkHandler, PacketByteBuf buf, PacketSender sender) {

        Path path = Path.fromBuf(buf);
        server.execute(() -> {
            World world = player.getWorld();
            Entity entity = world.getEntityById(path.getEntityId());
            if (!(entity instanceof PathAwareEntity)) return;
            ((PathAwareData) entity).cinematiccreeper$setPath(path);
            Vec3d pos = path.getActions().get(0).getPos();
            entity.teleport(pos.x, pos.y, pos.z);
        });


    }
}
