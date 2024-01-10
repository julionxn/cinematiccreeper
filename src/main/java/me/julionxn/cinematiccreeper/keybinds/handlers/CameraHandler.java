package me.julionxn.cinematiccreeper.keybinds.handlers;

import me.julionxn.cinematiccreeper.core.managers.CameraManager;
import me.julionxn.cinematiccreeper.keybinds.InputHandler;
import me.julionxn.cinematiccreeper.keybinds.PressModifier;
import me.julionxn.cinematiccreeper.keybinds.ScrollAndKeyAction;
import net.minecraft.text.Text;
import org.lwjgl.glfw.GLFW;

public class CameraHandler extends InputHandler {
    @Override
    public void init() {
        setPredicate(client -> CameraManager.getInstance().isActive());
        addScrollAndKeyAction(new ScrollAndKeyAction(GLFW.GLFW_KEY_Z, Text.translatable("camera.cinematiccreeper.change_zoom"),
                (client, modifier, vertical) -> {
                    double zoomA = modifier == PressModifier.NONE ? 0.5 : 0.1;
                    CameraManager.getInstance().incrementZoom(vertical * zoomA);
                }, () -> CameraManager.getInstance().isActive()));
        addScrollAndKeyAction(new ScrollAndKeyAction(GLFW.GLFW_KEY_X, Text.translatable("camera.cinematiccreeper.change_fov"),
                (client, modifier, vertical) -> {
                    double fovA = modifier == PressModifier.NONE ? 1 : 0.5;
                    CameraManager.getInstance().incrementFov(vertical * fovA);
                }, () -> CameraManager.getInstance().isActive()));
    }

    @Override
    public boolean shouldCancelNext() {
        return false;
    }
}
