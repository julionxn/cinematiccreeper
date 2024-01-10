package me.julionxn.cinematiccreeper.keybinds.handlers;

import me.julionxn.cinematiccreeper.core.managers.CameraManager;
import me.julionxn.cinematiccreeper.keybinds.InputAction;
import me.julionxn.cinematiccreeper.keybinds.InputHandler;
import me.julionxn.cinematiccreeper.keybinds.Keybindings;
import me.julionxn.cinematiccreeper.screen.gui.screens.camera.SettingsCameraMenu;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.util.math.Vec3d;
import org.lwjgl.glfw.GLFW;

public class CameraOptionsHandler extends InputHandler {
    @Override
    public void init() {
        setPredicate(client -> !CameraManager.getInstance().isRecording());
        addPressAction(new InputAction(Keybindings.firstAction, Text.translatable("camera.cinematiccreeper.detach_camera"),
                (client, modifier) -> {
                    CameraManager.State newState = CameraManager.getInstance().isActive() ? CameraManager.State.NONE : CameraManager.State.STATIC;
                    if (newState == CameraManager.State.STATIC) {
                        PlayerEntity player = client.player;
                        if (player == null) return;
                        Vec3d pos = player.getPos();
                        CameraManager.getInstance().setAnchorValues(pos.x, pos.y + 1.5, pos.z, player.getYaw(), player.getPitch());
                    }
                    CameraManager.getInstance().setState(newState);
                }));
        addPressAction(new InputAction(Keybindings.secondAction, Text.translatable("camera.cinematiccreeper.control_mode"),
                (client, modifier) -> {
                    CameraManager.State newState = CameraManager.getInstance().getState() == CameraManager.State.MOVING ? CameraManager.State.STATIC : CameraManager.State.MOVING;
                    CameraManager.getInstance().setState(newState);
                }, () -> CameraManager.getInstance().isActive()));
        addPressAction(new InputAction(GLFW.GLFW_KEY_O, Text.translatable("camera.cinematiccreeper.camera_settings"),
                (client, pressModifier) -> client.setScreen(new SettingsCameraMenu())));
    }

    @Override
    public boolean shouldCancelNext() {
        return false;
    }
}
