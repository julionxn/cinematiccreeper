package me.julionxn.cinematiccreeper.screen.gui.components.widgets;

import me.julionxn.cinematiccreeper.CinematicCreeper;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.narration.NarrationMessageBuilder;
import net.minecraft.client.gui.widget.ClickableWidget;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.MathHelper;

import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

public class SliderWidget extends ClickableWidget {

    private static final Identifier SCROLLBAR_TEXTURE = new Identifier(CinematicCreeper.MOD_ID, "textures/gui/scrollbar.png");
    private static final Identifier BACKGROUND_TEXTURE = new Identifier(CinematicCreeper.MOD_ID, "textures/gui/slider.png");
    private Supplier<Float> value;
    private final float maxValue;
    private final Consumer<Float> onChange;
    private final Function<Float, String> text;

    public SliderWidget(int x, int y, Supplier<Float> startingValue, float maxValue, Text message, Function<Float, String> text, Consumer<Float> onChange) {
        super(x, y, 140, 30, message);
        this.value = startingValue;
        this.maxValue = maxValue;
        this.text = text;
        this.onChange = onChange;
    }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        super.render(context, mouseX, mouseY, delta);
        MinecraftClient client = MinecraftClient.getInstance();
        if (client == null) return;
        context.drawCenteredTextWithShadow(client.textRenderer, getMessage(), getX() + 70, getY(), 0xffffff);
        float tValue = value.get();
        int x = (int) (getX() + 1 + ((width - 6) * tValue));
        context.drawCenteredTextWithShadow(client.textRenderer, Text.of(text.apply(tValue * maxValue)),
                getX() + 70, getY() + 16, 0xffffff);
        context.drawTexture(SCROLLBAR_TEXTURE, x, getY() + 11,
                0, 0, 0, 4, 18, 4, 18);
    }

    @Override
    protected void renderButton(DrawContext context, int mouseX, int mouseY, float delta) {
        MinecraftClient client = MinecraftClient.getInstance();
        if (client == null) return;
        context.drawTexture(BACKGROUND_TEXTURE, getX(), getY() + 10,
                0, 0, 0, 140, 20, 140, 20);
    }

    @Override
    public void onClick(double mouseX, double mouseY) {
        setValue(mouseX);
    }

    @Override
    protected void onDrag(double mouseX, double mouseY, double deltaX, double deltaY) {
        setValue(mouseX);
    }

    private void setValue(double mouseX){
        float newValue = (float) (mouseX - getX()) / width;
        float value = MathHelper.clamp(newValue, 0, 1);
        onChange.accept(value);
    }

    @Override
    protected void appendClickableNarrations(NarrationMessageBuilder builder) {
    }

}
