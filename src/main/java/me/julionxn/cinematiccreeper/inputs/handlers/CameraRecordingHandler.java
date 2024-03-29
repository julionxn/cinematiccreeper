package me.julionxn.cinematiccreeper.inputs.handlers;

import me.julionxn.cinematiccreeper.core.camera.CameraRecording;
import me.julionxn.cinematiccreeper.core.managers.CameraManager;
import me.julionxn.cinematiccreeper.core.notifications.Notification;
import me.julionxn.cinematiccreeper.core.notifications.NotificationManager;
import me.julionxn.cinematiccreeper.inputs.*;
import net.minecraft.text.Text;
import org.lwjgl.glfw.GLFW;

public class CameraRecordingHandler extends InputHandler {

    @Override
    public void init() {
        setPredicate(client -> CameraManager.getInstance().isRecording());
        addPressAction(new InputAction(GLFW.GLFW_KEY_ESCAPE, Text.translatable("screen.cinematiccreeper.exit"),
                (client, pressModifier) -> {
                    CameraManager manager = CameraManager.getInstance();
                    manager.stopRecording();
                }));
        addPressAction(new InputAction(Keybindings.firstAction, Text.translatable("screen.cinematiccreeper.add_frame"),
                (client, pressModifier) -> {
                    CameraManager manager = CameraManager.getInstance();
                    CameraRecording recording = manager.getCurrentCameraRecording();
                    if (recording == null) return;
                    recording.addSnap(manager.takeSnap());
                }));
        addPressAction(new InputAction(Keybindings.secondAction, Text.translatable("screen.cinematiccreeper.remove_frame"),
                (client, pressModifier) -> {
                    CameraManager manager = CameraManager.getInstance();
                    CameraRecording recording = manager.getCurrentCameraRecording();
                    if (recording == null) return;
                    recording.removeSnap();
                }));
        addPressAction(new InputAction(Keybindings.thirdAction, Text.translatable("screen.cinematiccreeper.done"),
                (client, pressModifier) -> {
                    CameraManager manager = CameraManager.getInstance();
                    CameraRecording recording = manager.getCurrentCameraRecording();
                    if (recording == null) return;
                    int size = recording.getSize();
                    if (size < 2) {
                        NotificationManager.getInstance().add(Notification.NO_POINTS);
                        return;
                    }
                    manager.addRecording(recording);
                    manager.stopRecording();
                    NotificationManager.getInstance().add(Notification.SAVED);
                }));
        addScrollAndKeyAction(new ScrollAndKeyAction(GLFW.GLFW_KEY_C, Text.translatable("camera.cinematiccreeper.change_tick"),
                (client, modifier, vertical) -> {
                    CameraManager manager = CameraManager.getInstance();
                    CameraRecording recording = manager.getCurrentCameraRecording();
                    if (recording == null) return;
                    if (modifier != PressModifier.CTRL) {
                        int amount = vertical.intValue();
                        recording.adjustTick(amount);
                    } else {
                        if (vertical > 0) {
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
