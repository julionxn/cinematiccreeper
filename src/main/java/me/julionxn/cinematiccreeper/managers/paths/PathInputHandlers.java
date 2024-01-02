package me.julionxn.cinematiccreeper.managers.paths;

import me.julionxn.cinematiccreeper.util.mixins.PathAwareData;
import me.julionxn.cinematiccreeper.util.mixins.PlayerData;
import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;

import java.util.Optional;

public class PathInputHandlers {

    public static void handleFirstAction(MinecraftClient client) {
        shouldHandle(client).ifPresent(data -> {
            PlayerEntity player = client.player;
            if (player == null) return;
            if (data.state == PlayerPathState.State.ADDING) {
                Path path = data.pathState.path();
                PathAction pathAction = PathAction.getCurrentPathAction(player, false);
                path.addAction(pathAction);
                return;
            }
            if (data.state == PlayerPathState.State.RECORDING) {

            }
        });
    }

    public static void handleSecondAction(MinecraftClient client) {
        shouldHandle(client).ifPresent(data -> {
            if (data.state == PlayerPathState.State.ADDING) {
                Path path = data.pathState.path();
                path.popAction();
            }
        });
    }

    public static void handleAcceptAction(MinecraftClient client) {
        shouldHandle(client).ifPresent(data -> {
            Path path = data.pathState.path();
            if (data.state == PlayerPathState.State.ADDING) {
                Entity entity = ((PlayerEntity) data.playerData).getWorld().getEntityById(path.getEntityId());
                data.playerData.cinematiccreeper$setPathState(PlayerPathState.none());
                if (entity == null) return;
                ((PathAwareData) entity).cinematiccreeper$addPath(path);
            }
            if (data.state == PlayerPathState.State.RECORDING) {

            }
        });
    }

    private static Optional<Data> shouldHandle(MinecraftClient client) {
        PlayerEntity player = client.player;
        if (player == null) return Optional.empty();
        PlayerData playerData = (PlayerData) player;
        PlayerPathState playerPathState = playerData.cinematiccreeper$getPathState();
        PlayerPathState.State state = playerPathState.state();
        if (state == PlayerPathState.State.NONE) return Optional.empty();
        return Optional.of(new Data(playerData, playerPathState, state));
    }

    private record Data(PlayerData playerData, PlayerPathState pathState, PlayerPathState.State state) {

    }

}
