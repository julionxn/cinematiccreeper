package me.julionxn.cinematiccreeper.keybinds.handlers;

import me.julionxn.cinematiccreeper.core.notifications.Notification;
import me.julionxn.cinematiccreeper.core.notifications.NotificationManager;
import me.julionxn.cinematiccreeper.core.paths.Path;
import me.julionxn.cinematiccreeper.core.paths.PathAction;
import me.julionxn.cinematiccreeper.core.paths.PlayerPathHolder;
import me.julionxn.cinematiccreeper.keybinds.InputAction;
import me.julionxn.cinematiccreeper.keybinds.InputHandler;
import me.julionxn.cinematiccreeper.keybinds.Keybindings;
import me.julionxn.cinematiccreeper.util.mixins.PathAwareData;
import me.julionxn.cinematiccreeper.util.mixins.PlayerData;
import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.text.Text;

public class PathsHandler extends InputHandler {

    private static boolean shouldHandlePaths(MinecraftClient client) {
        PlayerEntity player = client.player;
        if (player == null) return false;
        PlayerData playerData = (PlayerData) player;
        PlayerPathHolder playerPathHolder = playerData.cinematiccreeper$getPathHolder();
        PlayerPathHolder.State state = playerPathHolder.state();
        return state != PlayerPathHolder.State.NONE;
    }

    @Override
    public void init() {
        setPredicate(PathsHandler::shouldHandlePaths);
        addPressAction(new InputAction(256, Text.translatable("screen.cinematiccreeper.exit"), (client, modifier) -> {
            PlayerEntity player = client.player;
            if (player == null) return;
            PlayerData playerData = (PlayerData) player;
            if (playerData.cinematiccreeper$getPathHolder().state() != PlayerPathHolder.State.NONE) {
                playerData.cinematiccreeper$setPathHolder(PlayerPathHolder.none());
            }
        }));
        addPressAction(new InputAction(Keybindings.firstAction, Text.translatable("screen.cinematiccreeper.add_point"),
                (client, modifier) -> {
                    PlayerEntity player = client.player;
                    if (player == null) return;
                    PlayerPathHolder holder = ((PlayerData) player).cinematiccreeper$getPathHolder();
                    if (holder.state() == PlayerPathHolder.State.ADDING) {
                        Path path = holder.path();
                        PathAction pathAction = PathAction.getCurrentPathAction(player, false);
                        path.addAction(pathAction);
                        return;
                    }
                    if (holder.state() == PlayerPathHolder.State.RECORDING) {

                    }
                }));
        addPressAction(new InputAction(Keybindings.secondAction, Text.translatable("screen.cinematiccreeper.remove_last_point"),
                (client, modifier) -> {
                    PlayerEntity player = client.player;
                    if (player == null) return;
                    PlayerPathHolder holder = ((PlayerData) player).cinematiccreeper$getPathHolder();
                    if (holder.state() == PlayerPathHolder.State.ADDING) {
                        Path path = holder.path();
                        if (path.isEmpty()) {
                            NotificationManager.getInstance().add(Notification.NO_POINTS);
                            return;
                        }
                        path.popAction();
                    }
                }));
        addPressAction(new InputAction(Keybindings.thirdAction, Text.translatable("screen.cinematiccreeper.done"),
                (client, pressModifier) -> {
                    PlayerEntity player = client.player;
                    if (player == null) return;
                    PlayerPathHolder holder = ((PlayerData) player).cinematiccreeper$getPathHolder();
                    PlayerPathHolder.State state = holder.state();
                    Path path = holder.path();
                    if (path.isEmpty()) {
                        NotificationManager.getInstance().add(Notification.NO_POINTS);
                        return;
                    }
                    if (state == PlayerPathHolder.State.ADDING) {
                        Entity entity = player.getWorld().getEntityById(path.getEntityId());
                        ((PlayerData) player).cinematiccreeper$setPathHolder(PlayerPathHolder.none());
                        if (entity == null) return;
                        ((PathAwareData) entity).cinematiccreeper$addPath(path);
                        NotificationManager.getInstance().add(Notification.SAVED);
                    }
                    if (state == PlayerPathHolder.State.RECORDING) {

                    }
                }));
    }

    @Override
    public boolean shouldCancelNext() {
        return true;
    }
}
