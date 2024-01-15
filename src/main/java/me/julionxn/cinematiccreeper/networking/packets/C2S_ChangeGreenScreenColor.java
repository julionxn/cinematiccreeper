package me.julionxn.cinematiccreeper.networking.packets;

import me.julionxn.cinematiccreeper.blocks.AllBlocks;
import net.fabricmc.fabric.api.networking.v1.PacketSender;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayNetworkHandler;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.*;

public class C2S_ChangeGreenScreenColor {
    public static void onServer(MinecraftServer server, ServerPlayerEntity player,
                                ServerPlayNetworkHandler serverPlayNetworkHandler, PacketByteBuf buf, PacketSender sender) {

        BlockPos blockPos = buf.readBlockPos();
        int color = buf.readInt();

        server.execute(() -> {
            List<BlockPos> affected = new ArrayList<>();
            World world = player.getWorld();
            if (world == null) return;
            checkColliders(world, blockPos, affected);
            for (BlockPos pos : affected) {
                world.addSyncedBlockEvent(pos, AllBlocks.GREEN_SCREEN_BLOCK, 0, color);
            }
        });
    }

    private static void checkColliders(World world, BlockPos startBlockPos, List<BlockPos> affected) {
        Set<BlockPos> visited = new HashSet<>();
        Queue<BlockPos> queue = new LinkedList<>();
        queue.add(startBlockPos);
        while (!queue.isEmpty()) {
            BlockPos currentPos = queue.poll();
            if (!visited.contains(currentPos) && world.getBlockState(currentPos).getBlock() == AllBlocks.GREEN_SCREEN_BLOCK) {
                affected.add(currentPos);
                visited.add(currentPos);
                queue.add(currentPos.add(1, 0, 0));
                queue.add(currentPos.add(-1, 0, 0));
                queue.add(currentPos.add(0, 1, 0));
                queue.add(currentPos.add(0, -1, 0));
                queue.add(currentPos.add(0, 0, 1));
                queue.add(currentPos.add(0, 0, -1));
            }
        }
    }

}
