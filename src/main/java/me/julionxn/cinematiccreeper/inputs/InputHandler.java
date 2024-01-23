package me.julionxn.cinematiccreeper.inputs;


import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

public abstract class InputHandler {

    private Function<MinecraftClient, Boolean> predicate = (client) -> true;
    private final List<InputAction> onPressActions = new ArrayList<>();
    private final List<ScrollAndKeyAction> scrollAndKeyActions = new ArrayList<>();

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

    protected void addScrollAndKeyAction(ScrollAndKeyAction action){
        scrollAndKeyActions.add(action);
    }

    public List<InputAction> getOnPressActions(){
        return onPressActions;
    }

    public List<ScrollAndKeyAction> getScrollAndKeyActions(){
        return scrollAndKeyActions;
    }

    public Function<MinecraftClient, Boolean> getPredicate(){
        return predicate;
    }

    public abstract boolean shouldCancelNext();

    public boolean shouldRender(){
        return true;
    }

    public void render(DrawContext context){
    }

}
