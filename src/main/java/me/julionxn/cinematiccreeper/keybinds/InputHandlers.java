package me.julionxn.cinematiccreeper.keybinds;

import me.julionxn.cinematiccreeper.core.managers.CameraManager;
import me.julionxn.cinematiccreeper.core.notifications.Notification;
import me.julionxn.cinematiccreeper.core.notifications.NotificationManager;
import me.julionxn.cinematiccreeper.core.paths.Path;
import me.julionxn.cinematiccreeper.core.paths.PathAction;
import me.julionxn.cinematiccreeper.core.paths.PlayerPathHolder;
import me.julionxn.cinematiccreeper.util.mixins.PathAwareData;
import me.julionxn.cinematiccreeper.util.mixins.PlayerData;
import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.text.Text;

import java.util.Optional;

public class InputHandlers {

    public static void handleFirstAction(MinecraftClient client) {
        Optional<Data> dataOptional = shouldHandlePaths(client);
        //handle paths
        if (dataOptional.isPresent()){
            Data data = dataOptional.get();
            PlayerEntity player = client.player;
            if (player == null) return;
            if (data.state == PlayerPathHolder.State.ADDING) {
                Path path = data.pathState.path();
                PathAction pathAction = PathAction.getCurrentPathAction(player, false);
                path.addAction(pathAction);
                return;
            }
            if (data.state == PlayerPathHolder.State.RECORDING) {

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
            if (data.state == PlayerPathHolder.State.ADDING) {
                Path path = data.pathState.path();
                if (path.isEmpty()){
                    NotificationManager.getInstance().add(Notification.Type.WARNING, Text.translatable("messages.cinematiccreeper.no_points"));
                    return;
                }
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
            if (path.isEmpty()){
                NotificationManager.getInstance().add(Notification.Type.WARNING, Text.translatable("messages.cinematiccreeper.no_points"));
                return;
            }
            if (data.state == PlayerPathHolder.State.ADDING) {
                Entity entity = ((PlayerEntity) data.playerData).getWorld().getEntityById(path.getEntityId());
                data.playerData.cinematiccreeper$setPathHolder(PlayerPathHolder.none());
                if (entity == null) return;
                ((PathAwareData) entity).cinematiccreeper$addPath(path);
            }
            if (data.state == PlayerPathHolder.State.RECORDING) {

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
        PlayerPathHolder playerPathHolder = playerData.cinematiccreeper$getPathHolder();
        PlayerPathHolder.State state = playerPathHolder.state();
        if (state == PlayerPathHolder.State.NONE) return Optional.empty();
        return Optional.of(new Data(playerData, playerPathHolder, state));
    }

    private record Data(PlayerData playerData, PlayerPathHolder pathState, PlayerPathHolder.State state) {

    }

}
