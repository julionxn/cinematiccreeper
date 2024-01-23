package me.julionxn.cinematiccreeper.inputs.handlers;

import me.julionxn.cinematiccreeper.core.managers.CameraManager;
import me.julionxn.cinematiccreeper.inputs.InputAction;
import me.julionxn.cinematiccreeper.inputs.InputHandler;
import me.julionxn.cinematiccreeper.inputs.PressModifier;
import me.julionxn.cinematiccreeper.inputs.ScrollAndKeyAction;
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
                }));
        addScrollAndKeyAction(new ScrollAndKeyAction(GLFW.GLFW_KEY_X, Text.translatable("camera.cinematiccreeper.change_fov"),
                (client, modifier, vertical) -> {
                    double fovA = modifier == PressModifier.NONE ? 1 : 0.5;
                    CameraManager.getInstance().incrementFov(vertical * fovA);
                }));
        addPressAction(new InputAction(GLFW.GLFW_KEY_R, Text.translatable("camera.cinematiccreeper.reset"),
                (client, modifier) -> CameraManager.getInstance().resetToAnchor()));
    }

    @Override
    public boolean shouldRender() {
        return CameraManager.getInstance().getSettings().showOptions() || CameraManager.getInstance().isRecording();
    }

    @Override
    public boolean shouldCancelNext() {
        return false;
    }
}
