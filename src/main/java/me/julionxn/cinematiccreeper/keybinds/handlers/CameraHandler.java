package me.julionxn.cinematiccreeper.keybinds.handlers;

import me.julionxn.cinematiccreeper.core.managers.CameraManager;
import me.julionxn.cinematiccreeper.keybinds.*;
import me.julionxn.cinematiccreeper.screen.gui.screens.camera.OptionsCameraMenu;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.Vec3d;
import org.lwjgl.glfw.GLFW;

public class CameraHandler extends InputHandler {
    @Override
    public void init() {
        setPredicate(client -> !CameraManager.getInstance().isRecording());
        addPressAction(new InputAction(Keybindings.firstAction, (client, modifier) -> {
            CameraManager.State newState = CameraManager.getInstance().isActive() ? CameraManager.State.NONE : CameraManager.State.STATIC;
            if (newState == CameraManager.State.STATIC){
                PlayerEntity player = client.player;
                if (player == null) return;
                Vec3d pos = player.getPos();
                CameraManager.getInstance().setAnchorPos(pos.x, pos.y + 1.5, pos.z, player.getYaw(), player.getPitch());
            }
            CameraManager.getInstance().setState(newState);
        }));
        addPressAction(new InputAction(Keybindings.secondAction , (client, modifier) -> {
            CameraManager.State newState = CameraManager.getInstance().getState() == CameraManager.State.MOVING ? CameraManager.State.STATIC : CameraManager.State.MOVING;
            CameraManager.getInstance().setState(newState);
        }, () -> CameraManager.getInstance().isActive()));
        addScrollAndKeyAction(new ScrollAndKeyAction(GLFW.GLFW_KEY_Z, (client, modifier, vertical) -> {
            double zoomA = modifier == PressModifier.NONE ? 0.5 : modifier == PressModifier.SHIFT ? 1.0 : 0.1;
            CameraManager.getInstance().adjustZoom(vertical * zoomA);
        }, () -> CameraManager.getInstance().isActive()));
        addScrollAndKeyAction(new ScrollAndKeyAction(GLFW.GLFW_KEY_X, (client, modifier, vertical) -> {
            double fovA = modifier == PressModifier.NONE ? 1 : modifier == PressModifier.SHIFT ? 5 : 0.5;
            CameraManager.getInstance().adjustFov(vertical * fovA);
        }, () -> CameraManager.getInstance().isActive()));
        addPressAction(new InputAction(GLFW.GLFW_KEY_O, (client, pressModifier) -> {
            client.setScreen(new OptionsCameraMenu());
        }));
    }

    @Override
    public boolean shouldCancelNext() {
        return false;
    }
}
