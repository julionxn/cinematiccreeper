package me.julionxn.cinematiccreeper.keybinds;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.text.Text;

import java.util.function.BiConsumer;
import java.util.function.Supplier;

public class InputAction {

    public final Supplier<Integer> key;
    public final Text description;
    public final BiConsumer<MinecraftClient, PressModifier> action;
    public final Supplier<Boolean> predicate;

    public InputAction(int key, Text description, BiConsumer<MinecraftClient, PressModifier> action, Supplier<Boolean> predicate){
        this.key = () -> key;
        this.description = description;
        this.action = action;
        this.predicate = predicate;
    }

    public InputAction(int key, Text description, BiConsumer<MinecraftClient, PressModifier> action){
        this(key, description, action, InputAction::alwaysTrue);
    }


    public InputAction(KeyBinding keyBinding, Text description, BiConsumer<MinecraftClient, PressModifier> action, Supplier<Boolean> predicate){
        this.key = () -> keyBinding.boundKey.getCode();
        this.description = description;
        this.action = action;
        this.predicate = predicate;
    }

    public InputAction(KeyBinding keyBinding, Text description, BiConsumer<MinecraftClient, PressModifier> action){
        this(keyBinding, description, action, InputAction::alwaysTrue);
    }

    protected static boolean alwaysTrue(){
        return true;
    }

}
