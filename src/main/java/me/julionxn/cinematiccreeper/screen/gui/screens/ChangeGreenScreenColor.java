package me.julionxn.cinematiccreeper.screen.gui.screens;

import me.julionxn.cinematiccreeper.networking.AllPackets;
import me.julionxn.cinematiccreeper.screen.gui.components.widgets.SliderWidget;
import me.julionxn.cinematiccreeper.util.colors.Color;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.gui.widget.TextFieldWidget;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.text.Text;
import net.minecraft.util.math.BlockPos;

public class ChangeGreenScreenColor extends Screen {

    private final BlockPos blockPos;
    private final Color color;

    public ChangeGreenScreenColor(BlockPos blockPos, int color) {
        super(Text.of("CHANGE_COLOR"));
        this.blockPos = blockPos;
        this.color = new Color(color);
    }

    @Override
    protected void init() {
        if (client == null) return;
        int x = client.getWindow().getScaledWidth() / 2;
        int y = client.getWindow().getScaledHeight() / 2 - 80;
        TextFieldWidget hexColorField = new TextFieldWidget(client.textRenderer,
                x, y + 130, 140, 20, Text.of("XD"));
        addDrawableChild(hexColorField);
        hexColorField.setText(String.format("#%06X", color.getColor() & 0xFFFFFF));
        hexColorField.setChangedListener(newColor -> {
            newColor = newColor.replace("#", "");
            try {
                int colorInt = Integer.parseInt(newColor, 16);
                color.setColor(colorInt);
            } catch (NumberFormatException e){
                // empty
            }
        });
        SliderWidget<Integer> redSlider = SliderWidget.builder(Integer.class,
                () -> color.toInt(color.getRed()),
                newValue -> {
                    color.setRed(newValue);
                    hexColorField.setText(String.format("#%06X", color.getColor() & 0xFFFFFF));
                })
                .pos(x, y).range(0, 255).message(Text.of("Red")).build();
        addDrawableChild(redSlider);
        SliderWidget<Integer> greenSlider = SliderWidget.builder(Integer.class,
                () -> color.toInt(color.getGreen()),
                newValue -> {
                    color.setGreen(newValue);
                    hexColorField.setText(String.format("#%06X", color.getColor() & 0xFFFFFF));
                })
                .pos(x, y + 40).range(0, 255).message(Text.of("Green")).build();
        addDrawableChild(greenSlider);
        SliderWidget<Integer> blueSlider = SliderWidget.builder(Integer.class,
                        () -> color.toInt(color.getBlue()),
                        newValue -> {
                            color.setBlue(newValue);
                            hexColorField.setText(String.format("#%06X", color.getColor() & 0xFFFFFF));
                        })
                .pos(x, y + 80).range(0, 255).message(Text.of("Blue")).build();
        addDrawableChild(blueSlider);
        ButtonWidget acceptButton = ButtonWidget.builder(Text.of("Done"), button -> {
            PacketByteBuf buf = PacketByteBufs.create();
            buf.writeBlockPos(blockPos);
            buf.writeInt(color.getColor());
            ClientPlayNetworking.send(AllPackets.C2S_CHANGE_GREEN_SCREEN_COLOR, buf);
            close();
        }).dimensions(x - 130, y + 130, 100, 20).build();
        addDrawableChild(acceptButton);
    }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        super.render(context, mouseX, mouseY, delta);
        context.fill(context.getScaledWindowWidth() / 2 - 130,
                context.getScaledWindowHeight() / 2 - 70,
                context.getScaledWindowWidth() / 2 - 30,
                context.getScaledWindowHeight() / 2 + 30,
                200, color.getColorWithAlpha());
    }
}
