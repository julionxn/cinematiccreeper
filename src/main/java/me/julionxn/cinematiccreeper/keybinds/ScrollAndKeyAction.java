package me.julionxn.cinematiccreeper.keybinds;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.option.KeyBinding;
import org.apache.logging.log4j.util.TriConsumer;

import java.util.function.Supplier;

public class ScrollAndKeyAction {

    public final Supplier<Boolean> predicate;
    public final Supplier<Integer> key;
    public final TriConsumer<MinecraftClient, PressModifier, Double> action;

    public ScrollAndKeyAction(int key, TriConsumer<MinecraftClient, PressModifier, Double> action, Supplier<Boolean> predicate){
        this.key = () -> key;
        this.action = action;
        this.predicate = predicate;
    }

    public ScrollAndKeyAction(int key, TriConsumer<MinecraftClient, PressModifier, Double> action){
        this(key, action, InputAction::alwaysTrue);
    }


    public ScrollAndKeyAction(KeyBinding keyBinding, TriConsumer<MinecraftClient, PressModifier, Double> action, Supplier<Boolean> predicate){
        this.key = () -> keyBinding.boundKey.getCode();
        this.action = action;
        this.predicate = predicate;
    }

    public ScrollAndKeyAction(KeyBinding keyBinding, TriConsumer<MinecraftClient, PressModifier, Double> action){
        this(keyBinding, action, InputAction::alwaysTrue);
    }

    protected static boolean alwaysTrue(){
        return true;
    }
}
