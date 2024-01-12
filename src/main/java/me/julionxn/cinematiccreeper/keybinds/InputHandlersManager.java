package me.julionxn.cinematiccreeper.keybinds;

import me.julionxn.cinematiccreeper.core.managers.CameraManager;
import me.julionxn.cinematiccreeper.keybinds.handlers.CameraHandler;
import me.julionxn.cinematiccreeper.keybinds.handlers.CameraOptionsHandler;
import me.julionxn.cinematiccreeper.keybinds.handlers.CameraRecordingHandler;
import me.julionxn.cinematiccreeper.keybinds.handlers.PathsHandler;
import me.julionxn.cinematiccreeper.util.TextUtils;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.util.InputUtil;
import org.lwjgl.glfw.GLFW;

import java.util.List;

public class InputHandlersManager {

    private static int key;
    private static PressModifier pressModifier;

    private static final List<InputHandler> inputHandlers = List.of(
        new PathsHandler(),  new CameraHandler(), new CameraRecordingHandler(), new CameraOptionsHandler()
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
            if (pressState == PressState.PRESS){
                for (InputAction onPressAction : inputHandler.getOnPressActions()) {
                    if (onPressAction.key.get() == key && onPressAction.predicate.get()){
                        onPressAction.action.accept(client, pressModifier);
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


    public static void render(MinecraftClient client, DrawContext context){
        if (client == null) return;
        List<InputHandler> toHandle = inputHandlers.stream()
                .filter(inputHandler -> inputHandler.getPredicate().apply(client))
                .filter(InputHandler::shouldRender)
                .toList();
        int startingY = 20;
        for (InputHandler inputHandler : toHandle) {
            for (InputAction onPressAction : inputHandler.getOnPressActions()) {
                int keyCode = onPressAction.key.get();
                InputUtil.Key key = InputUtil.fromKeyCode(keyCode, keyCode);
                String keyString = TextUtils.parseKey(key);
                context.drawTextWithShadow(client.textRenderer,  keyString + ": " + onPressAction.description.getString(), 20, startingY, 0xffffff);
                startingY += 12;
            }
            for (ScrollAndKeyAction scrollAndKeyAction : inputHandler.getScrollAndKeyActions()) {
                int keyCode = scrollAndKeyAction.key.get();
                InputUtil.Key key = InputUtil.fromKeyCode(keyCode, keyCode);
                String keyString = TextUtils.parseKey(key);
                context.drawTextWithShadow(client.textRenderer,  keyString + " + Scroll" + ": " + scrollAndKeyAction.description.getString(), 20, startingY, 0xffffff);
                startingY += 12;
            }
            inputHandler.render(context);
        }
        if (CameraManager.getInstance().getSettings().showGrid()){
            int width = context.getScaledWindowWidth();
            int height = context.getScaledWindowHeight();
            context.drawHorizontalLine(0, width, height / 3, 0x66000000);
            context.drawHorizontalLine(0, width, 2 * height / 3, 0x66000000);
            context.drawVerticalLine(width / 3, 0, height, 0x66000000);
            context.drawVerticalLine(2 * width / 3, 0, height, 0x66000000);
        }
    }

}
