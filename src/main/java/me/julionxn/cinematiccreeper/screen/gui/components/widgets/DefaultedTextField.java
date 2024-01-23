package me.julionxn.cinematiccreeper.screen.gui.components.widgets;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.widget.TextFieldWidget;
import net.minecraft.text.Text;

public class DefaultedTextField extends TextFieldWidget {

    private final Text defaultText;

    public DefaultedTextField(TextRenderer textRenderer, Text defaultText, int x, int y, int width, int height) {
        super(textRenderer, x, y, width, height, defaultText);
        this.defaultText = defaultText;
    }

    @Override
    public void renderWidget(DrawContext context, int mouseX, int mouseY, float delta) {
        MinecraftClient client = MinecraftClient.getInstance();
        if (client != null && getText().isEmpty() && !isActive()) {
            context.drawCenteredTextWithShadow(client.textRenderer, defaultText, getX() + getWidth() / 2, getY() + 6, 0x999999);
        }
        super.renderWidget(context, mouseX, mouseY, delta);
    }
}
