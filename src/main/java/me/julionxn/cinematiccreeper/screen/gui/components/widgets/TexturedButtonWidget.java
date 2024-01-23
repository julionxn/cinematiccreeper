package me.julionxn.cinematiccreeper.screen.gui.components.widgets;

import me.julionxn.cinematiccreeper.CinematicCreeper;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

import java.util.function.Supplier;

public class TexturedButtonWidget extends ButtonWidget {

    private static final Identifier BACKGROUND_TEXTURE = new Identifier(CinematicCreeper.MOD_ID, "textures/gui/textured_button_bg.png");
    private static final Identifier BACKGROUND_TEXTURE_HOVERED = new Identifier(CinematicCreeper.MOD_ID, "textures/gui/textured_button_hovered_bg.png");

    private final Identifier texture;

    public TexturedButtonWidget(Identifier texture, int x, int y, PressAction onPress) {
        super(x, y, 20, 20, Text.of(""), onPress, Supplier::get);
        this.texture = texture;
    }

    @Override
    protected void renderWidget(DrawContext context, int mouseX, int mouseY, float delta) {
        Identifier bgTexture = isHovered() ? BACKGROUND_TEXTURE_HOVERED : BACKGROUND_TEXTURE;
        context.drawTexture(bgTexture, getX(), getY(), 5, 0, 0, getWidth(), getHeight(), getWidth(), getHeight());
        context.drawTexture(texture, getX(), getY(), 5, 0, 0, getWidth(), getHeight(), getWidth(), getHeight());
    }

}
