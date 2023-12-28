package me.julionxn.cinematiccreeper.screen.gui.components;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.Drawable;
import net.minecraft.client.gui.Element;
import net.minecraft.client.gui.Selectable;
import org.jetbrains.annotations.Nullable;

public abstract class ExtendedWidget {

    private final ExtendedScreen screen;
    @Nullable
    protected final MinecraftClient client;

    public ExtendedWidget(ExtendedScreen screen){
        this.screen = screen;
        this.client = screen.getClient();
    }

    public abstract void init();
    public abstract void render(DrawContext context, int mouseX, int mouseY, float delta);

    protected <T extends Drawable & Element & Selectable> void addDrawableChild(T drawable){
        screen.addItemDrawable(drawable);
    }

    public void mouseClicked(double mouseX, double mouseY, int button){
    }

    public void mouseDragged(double mouseX, double mouseY, int button, double deltaX, double deltaY) {
    }

    public void mouseScrolled(double mouseX, double mouseY, double horizontalAmount, double verticalAmount) {
    }

    protected void clear(){
        screen.clear();
    }

}
