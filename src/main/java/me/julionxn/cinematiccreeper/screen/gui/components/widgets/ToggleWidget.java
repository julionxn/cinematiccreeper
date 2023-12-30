package me.julionxn.cinematiccreeper.screen.gui.components.widgets;

import com.mojang.blaze3d.systems.RenderSystem;
import me.julionxn.cinematiccreeper.CinematicCreeper;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.narration.NarrationMessageBuilder;
import net.minecraft.client.gui.widget.ClickableWidget;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

public class ToggleWidget extends ClickableWidget {

    private static final Identifier OFF_TOGGLE_TEXTURE = new Identifier(CinematicCreeper.MOD_ID, "textures/gui/toggle_off.png");
    private static final Identifier ON_TOGGLE_TEXTURE = new Identifier(CinematicCreeper.MOD_ID, "textures/gui/toggle_on.png");
    private final Runnable onActive;
    private final Runnable onDisabled;
    private boolean active;

    public ToggleWidget(int x, int y, int width, int height, Text message, Runnable onActive, Runnable onDisabled) {
        super(x, y, width, height, message);
        this.onActive = onActive;
        this.onDisabled = onDisabled;
    }

    @Override
    protected void renderButton(DrawContext context, int mouseX, int mouseY, float delta) {
        MinecraftClient client = MinecraftClient.getInstance();
        if (client == null) return;
        drawText(context, client.textRenderer);
        Identifier identifier = active ? ON_TOGGLE_TEXTURE : OFF_TOGGLE_TEXTURE;
        RenderSystem.enableBlend();
        context.drawTexture(identifier, getX() + getWidth() - 40, getY(),
                0, 0, 0, 40, 20, 40, 20);
        RenderSystem.disableBlend();
    }

    private void drawText(DrawContext context, TextRenderer textRenderer) {
        int i = this.getX() + 2;
        int j = this.getX() + this.getWidth() - 2 - 50;
        drawScrollableText(context, textRenderer, this.getMessage(),
                i, this.getY(),
                j, this.getY() + this.getHeight(),
                0xffffff);
    }

    @Override
    public void onClick(double mouseX, double mouseY) {
        setActive(!active);
    }

    public void setActive(boolean state) {
        active = state;
        if (active) {
            onActive.run();
        } else {
            onDisabled.run();
        }
    }

    @Override
    protected void appendClickableNarrations(NarrationMessageBuilder builder) {

    }
}
