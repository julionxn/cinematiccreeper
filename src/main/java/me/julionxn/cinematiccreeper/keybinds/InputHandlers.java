package me.julionxn.cinematiccreeper.keybinds;

import me.julionxn.cinematiccreeper.managers.CameraManager;
import me.julionxn.cinematiccreeper.managers.paths.Path;
import me.julionxn.cinematiccreeper.managers.paths.PathAction;
import me.julionxn.cinematiccreeper.managers.paths.PlayerPathState;
import me.julionxn.cinematiccreeper.util.mixins.PathAwareData;
import me.julionxn.cinematiccreeper.util.mixins.PlayerData;
import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;

import java.util.Optional;

public class InputHandlers {

    public static void handleFirstAction(MinecraftClient client) {
        Optional<Data> dataOptional = shouldHandlePaths(client);
        //handle paths
        if (dataOptional.isPresent()){
            Data data = dataOptional.get();
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
            return;
        }
        //handle camera
        CameraManager.getInstance().toggleActive();
    }

    public static void handleSecondAction(MinecraftClient client) {
        Optional<Data> dataOptional = shouldHandlePaths(client);
        if (dataOptional.isPresent()){
            Data data = dataOptional.get();
            if (data.state == PlayerPathState.State.ADDING) {
                Path path = data.pathState.path();
                path.popAction();
            }
            return;
        }
        CameraManager cameraManager = CameraManager.getInstance();
        if (cameraManager.isActive()){
            cameraManager.setMoving(!cameraManager.isMoving());
        }
    }

    public static void handleThirdAction(MinecraftClient client) {
        Optional<Data> dataOptional = shouldHandlePaths(client);
        if (dataOptional.isPresent()) {
            Data data = dataOptional.get();
            Path path = data.pathState.path();
            if (data.state == PlayerPathState.State.ADDING) {
                Entity entity = ((PlayerEntity) data.playerData).getWorld().getEntityById(path.getEntityId());
                data.playerData.cinematiccreeper$setPathState(PlayerPathState.none());
                if (entity == null) return;
                ((PathAwareData) entity).cinematiccreeper$addPath(path);
            }
            if (data.state == PlayerPathState.State.RECORDING) {

            }
            return;
        }
        CameraManager cameraManager = CameraManager.getInstance();
        if (cameraManager.isActive()){
            cameraManager.setFov(cameraManager.getFov() + 5);
        }
    }

    public static void handleFourthAction(MinecraftClient client){
        CameraManager cameraManager = CameraManager.getInstance();
        if (cameraManager.isActive()){
            cameraManager.setFov(cameraManager.getFov() - 5);
        }
    }

    private static Optional<Data> shouldHandlePaths(MinecraftClient client) {
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
