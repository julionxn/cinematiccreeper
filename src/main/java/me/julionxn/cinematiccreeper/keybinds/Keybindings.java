package me.julionxn.cinematiccreeper.keybinds;

import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;
import org.lwjgl.glfw.GLFW;

public class Keybindings {

    public static final String KEY_CATEGORY_CINEMATICCREEPER = "key.category.cinematiccreeper";
    public static final String KEY_FIRST_ACTION = "key.cinematiccreeper.first_action";
    public static final String KEY_SECOND_ACTION = "key.cinematiccreeper.second_action";
    public static final String KEY_ACCEPT_ACTION = "key.cinematiccreeper.accept_action";

    public static KeyBinding firstAction;
    public static KeyBinding secondAction;
    public static KeyBinding acceptAction;

    public static void register(){
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
        acceptAction = KeyBindingHelper.registerKeyBinding(new KeyBinding(
                KEY_ACCEPT_ACTION,
                InputUtil.Type.KEYSYM,
                GLFW.GLFW_KEY_M,
                KEY_CATEGORY_CINEMATICCREEPER
        ));
    }

}
