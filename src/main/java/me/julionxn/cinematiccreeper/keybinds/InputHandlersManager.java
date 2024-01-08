package me.julionxn.cinematiccreeper.keybinds;

import me.julionxn.cinematiccreeper.keybinds.handlers.CameraHandler;
import me.julionxn.cinematiccreeper.keybinds.handlers.PathsHandler;
import me.julionxn.cinematiccreeper.keybinds.handlers.RecordingCameraHandler;
import net.minecraft.client.MinecraftClient;
import org.lwjgl.glfw.GLFW;

import java.util.List;

public class InputHandlersManager {

    private static int key;
    private static PressModifier pressModifier;

    private static final List<InputHandler> inputHandlers = List.of(
        new PathsHandler(), new RecordingCameraHandler(), new CameraHandler()
    );

    public static void handleKeyboard(MinecraftClient client, int key, int action, int modifier){
        PressState pressState = PressState.get(action);
        if (key != GLFW.GLFW_KEY_LEFT_SHIFT && key != GLFW.GLFW_KEY_LEFT_CONTROL){
            InputHandlersManager.key = pressState != PressState.RELEASE ? key : -1;
        }
        PressModifier pressModifier = PressModifier.get(modifier);
        InputHandlersManager.pressModifier = pressState != PressState.RELEASE ? pressModifier : PressModifier.NONE;
        List<InputHandler> toHandle = inputHandlers.stream().filter(inputHandler -> inputHandler.getPredicate().apply(client)).toList();
        for (InputHandler inputHandler : toHandle) {
            switch (pressState){
                case PRESS -> {
                    for (InputAction onPressAction : inputHandler.getOnPressActions()) {
                        if (onPressAction.key.get() == key && onPressAction.predicate.get()){
                            onPressAction.action.accept(client, pressModifier);
                        }
                    }
                }
                case HOLD -> {
                    for (InputAction onPressAction : inputHandler.getOnHoldActions()) {
                        if (onPressAction.key.get() == key && onPressAction.predicate.get()){
                            onPressAction.action.accept(client, pressModifier);
                        }
                    }
                }
                case RELEASE -> {
                    for (InputAction onPressAction : inputHandler.getOnReleaseActions()) {
                        if (onPressAction.key.get() == key && onPressAction.predicate.get()){
                            onPressAction.action.accept(client, pressModifier);
                        }
                    }
                }
            }
            if (inputHandler.shouldCancelNext()) break;
        }
    }

    public static void handleScroll(MinecraftClient client, double vertical){
        List<InputHandler> toHandle = inputHandlers.stream().filter(inputHandler -> inputHandler.getPredicate().apply(client)).toList();
        for (InputHandler inputHandler : toHandle) {
            for (ScrollAndKeyAction scrollAndKeyAction : inputHandler.getScrollAndKeyActions()) {
                if (scrollAndKeyAction.key.get() == key && scrollAndKeyAction.predicate.get()){
                    scrollAndKeyAction.action.accept(client, pressModifier, vertical);
                }
            }
            if(inputHandler.shouldCancelNext()) break;
        }
    }

}
