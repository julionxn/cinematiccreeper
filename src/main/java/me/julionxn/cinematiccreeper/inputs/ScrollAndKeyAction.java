package me.julionxn.cinematiccreeper.inputs;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.text.Text;
import org.apache.logging.log4j.util.TriConsumer;

import java.util.function.Supplier;

public class ScrollAndKeyAction {

    public final Supplier<Integer> key;
    public final Text description;
    public final TriConsumer<MinecraftClient, PressModifier, Double> action;
    public final Supplier<Boolean> predicate;

    public ScrollAndKeyAction(int key, Text description, TriConsumer<MinecraftClient, PressModifier, Double> action, Supplier<Boolean> predicate){
        this.key = () -> key;
        this.description = description;
        this.action = action;
        this.predicate = predicate;
    }

    public ScrollAndKeyAction(int key, Text description, TriConsumer<MinecraftClient, PressModifier, Double> action){
        this(key, description, action, InputAction::alwaysTrue);
    }


    public ScrollAndKeyAction(KeyBinding keyBinding, Text description, TriConsumer<MinecraftClient, PressModifier, Double> action, Supplier<Boolean> predicate){
        this.key = () -> keyBinding.boundKey.getCode();
        this.description = description;
        this.action = action;
        this.predicate = predicate;
    }

    public ScrollAndKeyAction(KeyBinding keyBinding, Text description, TriConsumer<MinecraftClient, PressModifier, Double> action){
        this(keyBinding, description, action, InputAction::alwaysTrue);
    }

    protected static boolean alwaysTrue(){
        return true;
    }
}
