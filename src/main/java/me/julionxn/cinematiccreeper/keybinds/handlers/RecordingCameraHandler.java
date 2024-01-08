package me.julionxn.cinematiccreeper.keybinds.handlers;

import me.julionxn.cinematiccreeper.core.camera.CameraRecording;
import me.julionxn.cinematiccreeper.core.managers.CameraManager;
import me.julionxn.cinematiccreeper.core.notifications.Notification;
import me.julionxn.cinematiccreeper.core.notifications.NotificationManager;
import me.julionxn.cinematiccreeper.keybinds.*;
import net.minecraft.text.Text;
import org.lwjgl.glfw.GLFW;

public class RecordingCameraHandler extends InputHandler {

    @Override
    public void init() {
        setPredicate(client -> CameraManager.getInstance().isRecording());
        addPressAction(new InputAction(GLFW.GLFW_KEY_ESCAPE, (client, pressModifier) -> {
            CameraManager manager = CameraManager.getInstance();
            manager.stopRecording();
        }));
        addPressAction(new InputAction(Keybindings.firstAction, (client, pressModifier) -> {
            CameraManager manager = CameraManager.getInstance();
            CameraRecording recording = manager.getCameraRecording();
            if (recording == null) return;
            recording.addSnap(manager.takeSnap());
        }));
        addPressAction(new InputAction(Keybindings.secondAction, (client, pressModifier) -> {
            CameraManager manager = CameraManager.getInstance();
            CameraRecording recording = manager.getCameraRecording();
            if (recording == null) return;
            recording.removeSnap();
        }));
        addPressAction(new InputAction(Keybindings.thirdAction, (client, pressModifier) -> {
            CameraManager manager = CameraManager.getInstance();
            CameraRecording recording = manager.getCameraRecording();
            if (recording == null) return;
            int size = recording.getSize();
            if (size < 2){
                NotificationManager.getInstance().add(Notification.Type.WARNING, Text.translatable("messages.cinematiccreeper.no_points"));
                return;
            }
            manager.addRecording(recording);
        }));
        addScrollAndKeyAction(new ScrollAndKeyAction(GLFW.GLFW_KEY_Z, (client, modifier, vertical) -> {
            CameraManager manager = CameraManager.getInstance();
            CameraRecording recording = manager.getCameraRecording();
            if (recording == null) return;
            if (modifier != PressModifier.CTRL){
                int amount = vertical.intValue();
                if (modifier == PressModifier.SHIFT){
                    amount += 20;
                }
                recording.adjustTick(amount);
            } else {
                if (vertical > 0){
                    recording.adjustToNextSnap();
                } else if (vertical < 0) {
                    recording.adjustToPrevSnap();
                }
            }
        }));
    }

    @Override
    public boolean shouldCancelNext() {
        return true;
    }
}
