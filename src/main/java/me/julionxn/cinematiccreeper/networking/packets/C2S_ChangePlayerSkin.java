package me.julionxn.cinematiccreeper.networking.packets;

import me.julionxn.cinematiccreeper.util.mixins.PlayerData;
import net.fabricmc.fabric.api.networking.v1.PacketSender;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayNetworkHandler;
import net.minecraft.server.network.ServerPlayerEntity;

public class C2S_ChangePlayerSkin {

    public static void onServer(MinecraftServer server, ServerPlayerEntity player,
                                ServerPlayNetworkHandler serverPlayNetworkHandler, PacketByteBuf buf, PacketSender sender) {

        String url = buf.readString();
        server.execute(() -> ((PlayerData) player).cinematiccreeper$setSkinUrl(url));

    }

}
