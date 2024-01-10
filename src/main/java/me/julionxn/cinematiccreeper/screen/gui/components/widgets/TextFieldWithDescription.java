package me.julionxn.cinematiccreeper.screen.gui.components.widgets;

import me.julionxn.cinematiccreeper.screen.gui.components.ExtendedScreen;
import me.julionxn.cinematiccreeper.screen.gui.components.ExtendedWidget;
import me.julionxn.cinematiccreeper.util.RenderUtils;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.widget.TextFieldWidget;
import net.minecraft.text.Text;

public class TextFieldWithDescription extends ExtendedWidget {

    private final TextRenderer textRenderer;
    private final int x;
    private final int y;
    private final int textWidth;
    private final int fieldWidth;
    private final int height;
    private final Text text;
    private TextFieldWidget textFieldWidget;

    public TextFieldWithDescription(ExtendedScreen screen, TextRenderer textRenderer, int x, int y, int textWidth, int fieldWidth, int height, Text text) {
        super(screen);
        this.textRenderer = textRenderer;
        this.x = x;
        this.y = y;
        this.textWidth = textWidth;
        this.fieldWidth = fieldWidth;
        this.height = height;
        this.text = text;
        this.textFieldWidget = new TextFieldWidget(textRenderer, x + textWidth, y, fieldWidth, height, text);
    }

    @Override
    public void init() {
        textFieldWidget = new TextFieldWidget(textRenderer, x + textWidth, y, fieldWidth, height, text);
        addDrawableChild(textFieldWidget);
    }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        RenderUtils.drawScrollableText(context, textRenderer, text,
                x + ((textWidth - 20) / 2),
                x + 10,
                y,
                x + textWidth - 10,
                y + height);
    }

    public TextFieldWidget getTextFieldWidget(){
        return textFieldWidget;
    }

}
