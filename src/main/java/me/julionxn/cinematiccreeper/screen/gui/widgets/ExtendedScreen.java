package me.julionxn.cinematiccreeper.screen.gui.widgets;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.Drawable;
import net.minecraft.client.gui.Element;
import net.minecraft.client.gui.Selectable;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.util.Window;
import net.minecraft.text.Text;

import java.util.HashSet;
import java.util.Set;

public abstract class ExtendedScreen extends Screen {

    private final Set<ExtendedWidget> widgets = new HashSet<>();
    protected int windowWidth = 0;
    protected int windowHeight = 0;

    protected ExtendedScreen(Text title) {
        super(title);
    }

    public void addWidget(ExtendedWidget widget){
        if (widgets.contains(widget)) return;
        widgets.add(widget);
    }

    public <T extends Drawable & Element & Selectable> void addItemDrawable(T drawable){
        addDrawableChild(drawable);
    }

    public abstract void addWidgets();

    public abstract void addDrawables();

    @Override
    protected void init() {
        if (client == null) return;
        Window window = client.getWindow();
        windowWidth = window.getScaledWidth();
        windowHeight = window.getScaledHeight();
        widgets.clear();
        addWidgets();
        for (ExtendedWidget widget : widgets) {
            widget.init();
        }
        addDrawables();
    }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        for (ExtendedWidget widget : widgets) {
            widget.render(context, mouseX, mouseY, delta);
        }
        super.render(context, mouseX, mouseY, delta);
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        for (ExtendedWidget widget : widgets) {
            widget.mouseClicked(mouseX, mouseY, button);
        }
        return super.mouseClicked(mouseX, mouseY, button);
    }

    @Override
    public boolean mouseDragged(double mouseX, double mouseY, int button, double deltaX, double deltaY) {
        for (ExtendedWidget widget : widgets) {
            widget.mouseDragged(mouseX, mouseY, button, deltaX, deltaY);
        }
        return super.mouseDragged(mouseX, mouseY, button, deltaX, deltaY);
    }

    @Override
    public boolean mouseScrolled(double mouseX, double mouseY, double horizontalAmount, double verticalAmount) {
        for (ExtendedWidget widget : widgets) {
            widget.mouseScrolled(mouseX, mouseY, horizontalAmount, verticalAmount);
        }
        return super.mouseScrolled(mouseX, mouseY, horizontalAmount, verticalAmount);
    }

    public MinecraftClient getClient(){
        return client;
    }

    public void clearChildrenS(){
        clearChildren();
        for (ExtendedWidget widget : widgets) {
            widget.init();
        }
        addDrawables();
    }
}
