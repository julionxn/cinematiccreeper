package me.julionxn.cinematiccreeper.keybinds;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.option.KeyBinding;

import java.util.function.BiConsumer;
import java.util.function.Supplier;

public class InputAction {

    public final Supplier<Boolean> predicate;
    public final Supplier<Integer> key;
    public final BiConsumer<MinecraftClient, PressModifier> action;

    public InputAction(int key, BiConsumer<MinecraftClient, PressModifier> action, Supplier<Boolean> predicate){
        this.key = () -> key;
        this.action = action;
        this.predicate = predicate;
    }

    public InputAction(int key, BiConsumer<MinecraftClient, PressModifier> action){
        this(key, action, InputAction::alwaysTrue);
    }


    public InputAction(KeyBinding keyBinding, BiConsumer<MinecraftClient, PressModifier> action, Supplier<Boolean> predicate){
        this.key = () -> keyBinding.boundKey.getCode();
        this.action = action;
        this.predicate = predicate;
    }

    public InputAction(KeyBinding keyBinding, BiConsumer<MinecraftClient, PressModifier> action){
        this(keyBinding, action, InputAction::alwaysTrue);
    }

    protected static boolean alwaysTrue(){
        return true;
    }

}
