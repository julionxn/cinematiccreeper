package me.julionxn.cinematiccreeper.screen.gui.components.widgets;

import me.julionxn.cinematiccreeper.CinematicCreeper;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.narration.NarrationMessageBuilder;
import net.minecraft.client.gui.widget.ClickableWidget;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

import java.util.Objects;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

public class SliderWidget<T extends Number> extends ClickableWidget {

    private static final Identifier SCROLLBAR_TEXTURE = new Identifier(CinematicCreeper.MOD_ID, "textures/gui/scrollbar.png");
    private static final Identifier BACKGROUND_TEXTURE = new Identifier(CinematicCreeper.MOD_ID, "textures/gui/slider.png");
    private final boolean wrap;
    private final Supplier<T> value;
    private final Supplier<T> minValue;
    private final Supplier<T> maxValue;
    private final Consumer<T> onChange;
    private final Function<T, String> text;
    private final Class<T> tClass;
    private final Supplier<Text> message;

    public SliderWidget(Class<T> clazz, boolean wrap, int x, int y, Supplier<T> value, Supplier<T> minValue, Supplier<T> maxValue, Supplier<Text> message, Function<T, String> text, Consumer<T> onChange) {
        super(x, y, 140, 30, message.get());
        this.tClass = clazz;
        this.wrap = wrap;
        this.value = value;
        this.minValue = minValue;
        this.maxValue = maxValue;
        this.text = text;
        this.onChange = onChange;
        this.message = message;
    }

    @Override
    protected void renderWidget(DrawContext context, int mouseX, int mouseY, float delta) {
        MinecraftClient client = MinecraftClient.getInstance();
        if (client == null) return;
        context.drawTexture(BACKGROUND_TEXTURE, getX(), getY() + 10,
                0, 0, 0, 140, 20, 140, 20);
        context.drawCenteredTextWithShadow(client.textRenderer, message.get(), getX() + 70, getY(), 0xffffff);
        T currentValue = value.get();
        context.drawCenteredTextWithShadow(client.textRenderer, Text.of(text.apply(currentValue)),
                getX() + 70, getY() + 16, 0xffffff);
        int x = (int) (getX() + 1 + ((width - 6) * ((currentValue.doubleValue() - minValue.get().doubleValue()) / (maxValue.get().doubleValue() - minValue.get().doubleValue()))));
        context.drawTexture(SCROLLBAR_TEXTURE, x, getY() + 11,
                0, 0, 0, 4, 18, 4, 18);
    }

    @Override
    public void onClick(double mouseX, double mouseY) {
        setValue(mouseX);
    }

    @Override
    protected void onDrag(double mouseX, double mouseY, double deltaX, double deltaY) {
        if (mouseX >= getX() - 10 && mouseY >= getY() && mouseX < getX() + 10 + width && mouseY < getY() + height){
            setValue(mouseX);
        }
    }

    @SuppressWarnings("unchecked")
    private void setValue(double mouseX){
        double newValue = (mouseX - getX()) / width;
        if (newValue > 1){
            newValue = wrap ? newValue % 1 : 1;
        } else if (newValue < 0){
            newValue = wrap ? newValue + 1 : 0;
        }
        newValue = minValue.get().doubleValue() + newValue * (maxValue.get().doubleValue() - minValue.get().doubleValue());
        Number value = convertToType(newValue);
        onChange.accept((T) value);
    }

    private Number convertToType(double value) {
        if (tClass == Integer.class){
            return (int) value;
        }
        if (tClass == Float.class){
            return (float) value;
        }
        return value;
    }

    @Override
    protected void appendClickableNarrations(NarrationMessageBuilder builder) {
    }

    public static <T extends Number> Builder<T> builder(Class<T> clazz, Supplier<T> value, Consumer<T> onChange){
        return new Builder<>(clazz, value, onChange);
    }

    public static class Builder<T extends Number> {

        private final Class<T> clazz;
        private final Supplier<T> value;
        private final Consumer<T> onChange;
        private int x;
        private int y;
        private Supplier<T> minValue;
        private Supplier<T> maxValue;
        private Supplier<Text> message = () -> Text.of("Value");
        private Function<T, String> overlayText = String::valueOf;
        private boolean wrap = false;

        public Builder(Class<T> clazz, Supplier<T> value, Consumer<T> onChange) {
            this.clazz = clazz;
            this.value = value;
            this.onChange = onChange;
        }

        public Builder<T> pos(int x, int y){
            this.x = x;
            this.y = y;
            return this;
        }

        public Builder<T> range(Supplier<T> min, Supplier<T> max){
            this.minValue = min;
            this.maxValue = max;
            return this;
        }

        public Builder<T> message(Supplier<Text> message){
            this.message = message;
            return this;
        }

        public Builder<T> overlayText(Function<T, String> function){
            this.overlayText = function;
            return this;
        }

        public Builder<T> wrap(){
            wrap = true;
            return this;
        }

        public SliderWidget<T> build(){
            Objects.requireNonNull(minValue);
            Objects.requireNonNull(maxValue);
            return new SliderWidget<>(clazz, wrap, x, y, value, minValue, maxValue, message, overlayText, onChange);
        }

    }

}
