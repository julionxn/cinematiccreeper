package me.julionxn.cinematiccreeper.screen.gui.components.widgets;

import me.julionxn.cinematiccreeper.CinematicCreeper;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.narration.NarrationMessageBuilder;
import net.minecraft.client.gui.widget.ClickableWidget;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.MathHelper;

public class ToggleWidget extends ClickableWidget {

    private static final Identifier TOGGLER_TEXTURE = new Identifier(CinematicCreeper.MOD_ID, "icon.png");
    private boolean active;
    private final Runnable onActive;
    private final Runnable onDisabled;

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
        int offset = active ? 20 : 0;
        context.drawTexture(TOGGLER_TEXTURE, getX() + getWidth() - 40 + offset, getY(),
                0, 0, 0, 20, 20, 20, 20);
    }

    private void drawText(DrawContext context, TextRenderer textRenderer){
        int i = this.getX() + 2;
        int j = this.getX() + this.getWidth() - 2 - 50;
        int c = this.active ? 16777215 : 10526880;
        drawScrollableText(context, textRenderer, this.getMessage(),
                i, this.getY(),
                j, this.getY() + this.getHeight(),
                c | MathHelper.ceil(this.alpha * 255.0F) << 24);
    }

    @Override
    public void onClick(double mouseX, double mouseY) {
        setActive(!active);
    }

    public void setActive(boolean state){
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
