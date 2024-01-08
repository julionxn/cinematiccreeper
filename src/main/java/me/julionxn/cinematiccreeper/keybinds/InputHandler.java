package me.julionxn.cinematiccreeper.keybinds;


import net.minecraft.client.MinecraftClient;

import java.util.HashSet;
import java.util.Set;
import java.util.function.Function;

public abstract class InputHandler {

    private Function<MinecraftClient, Boolean> predicate = (client) -> true;
    private final Set<InputAction> onPressActions = new HashSet<>();
    private final Set<InputAction> onHoldActions = new HashSet<>();
    private final Set<InputAction> onReleaseActions = new HashSet<>();
    private final Set<ScrollAndKeyAction> scrollAndKeyActions = new HashSet<>();

    public InputHandler(){
        init();
    }

    public abstract void init();

    protected void setPredicate(Function<MinecraftClient, Boolean> predicate){
        this.predicate = predicate;
    }

    protected void addPressAction(InputAction inputAction){
        onPressActions.add(inputAction);
    }

    protected void addHoldAction(InputAction inputAction){
        onHoldActions.add(inputAction);
    }

    protected void addReleaseAction(InputAction inputAction){
        onReleaseActions.add(inputAction);
    }

    protected void addScrollAndKeyAction(ScrollAndKeyAction action){
        scrollAndKeyActions.add(action);
    }

    public Set<InputAction> getOnPressActions(){
        return onPressActions;
    }

    public Set<InputAction> getOnHoldActions(){
        return onHoldActions;
    }

    public Set<InputAction> getOnReleaseActions(){
        return onReleaseActions;
    }

    public Set<ScrollAndKeyAction> getScrollAndKeyActions(){
        return scrollAndKeyActions;
    }

    public Function<MinecraftClient, Boolean> getPredicate(){
        return predicate;
    }

    public abstract boolean shouldCancelNext();

}
