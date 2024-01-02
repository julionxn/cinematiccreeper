package me.julionxn.cinematiccreeper.keybinds;

import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;
import org.lwjgl.glfw.GLFW;

public class Keybindings {

    public static final String KEY_CATEGORY_CINEMATICCREEPER = "key.category.cinematiccreeper";
    public static final String KEY_FIRST_ACTION = "key.cinematiccreeper.first_action";
    public static final String KEY_SECOND_ACTION = "key.cinematiccreeper.second_action";
    public static final String KEY_THIRD_ACTION = "key.cinematiccreeper.third_action";
    public static final String KEY_FOURTH_ACTION = "key.cinematiccreeper.fourth_action";

    public static KeyBinding firstAction;
    public static KeyBinding secondAction;
    public static KeyBinding thirdAction;
    public static KeyBinding fourthAction;

    public static void register() {
        firstAction = KeyBindingHelper.registerKeyBinding(new KeyBinding(
                KEY_FIRST_ACTION,
                InputUtil.Type.KEYSYM,
                GLFW.GLFW_KEY_J,
                KEY_CATEGORY_CINEMATICCREEPER
        ));
        secondAction = KeyBindingHelper.registerKeyBinding(new KeyBinding(
                KEY_SECOND_ACTION,
                InputUtil.Type.KEYSYM,
                GLFW.GLFW_KEY_K,
                KEY_CATEGORY_CINEMATICCREEPER
        ));
        thirdAction = KeyBindingHelper.registerKeyBinding(new KeyBinding(
                KEY_THIRD_ACTION,
                InputUtil.Type.KEYSYM,
                GLFW.GLFW_KEY_M,
                KEY_CATEGORY_CINEMATICCREEPER
        ));
        fourthAction= KeyBindingHelper.registerKeyBinding(new KeyBinding(
                KEY_FOURTH_ACTION,
                InputUtil.Type.KEYSYM,
                GLFW.GLFW_KEY_N,
                KEY_CATEGORY_CINEMATICCREEPER
        ));
    }

}
