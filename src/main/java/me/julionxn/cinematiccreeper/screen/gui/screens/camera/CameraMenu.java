package me.julionxn.cinematiccreeper.screen.gui.screens.camera;

import me.julionxn.cinematiccreeper.CinematicCreeper;
import me.julionxn.cinematiccreeper.screen.gui.components.ExtendedScreen;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

import java.util.ArrayList;
import java.util.List;
import java.util.function.BiConsumer;

public abstract class CameraMenu extends ExtendedScreen {

    private static final Identifier BACKGROUND = new Identifier(CinematicCreeper.MOD_ID, "textures/gui/npc_type_bg.png");
    private final List<Tab> tabs = new ArrayList<>();
    protected int x;
    protected int y;
    protected int width = 300;
    protected int height = 200;

    public CameraMenu() {
        super(Text.of("CameraOptions"));
    }

    @Override
    public void addWidgets() {

    }

    @Override
    public void addDrawables() {
        x = (windowWidth / 2) - width / 2;
        y = (windowHeight / 2) - height / 2;
        tabs.clear();
        addTab(Text.translatable("gui.cinematiccreeper.options"), ((buttonWidget, minecraftClient) -> {
            if (minecraftClient.currentScreen == null) return;
            if (minecraftClient.currentScreen.getClass() == OptionsCameraMenu.class) return;
            minecraftClient.setScreen(new OptionsCameraMenu());
        }));

        addTab(Text.translatable("gui.cinematiccreeper.recordings"), ((buttonWidget, minecraftClient) -> {
            if (minecraftClient.currentScreen == null) return;
            if (minecraftClient.currentScreen.getClass() == RecordingsCameraMenu.class) return;
            minecraftClient.setScreen(new RecordingsCameraMenu());
        }));

        int startingX = x + 25;
        int tabWidth = (width - 50) / tabs.size();
        for (int i = 0; i < tabs.size(); i++) {
            Tab tab = tabs.get(i);
            ButtonWidget buttonWidget = ButtonWidget.builder(tab.text, button -> tab.onClick.accept(button, client))
                    .dimensions(startingX + (i * tabWidth), y - 20, tabWidth, 20).build();
            addDrawableChild(buttonWidget);
        }
    }

    protected void addTab(Text text, BiConsumer<ButtonWidget, MinecraftClient> onClick) {
        tabs.add(new Tab(text, onClick));
    }

    @Override
    public void renderInGameBackground(DrawContext context) {
        super.renderInGameBackground(context);
        context.drawTexture(BACKGROUND, x, y, 0, 0, 300, 200, 300, 200);
        context.fill(x + 3, y + 3, x + width - 3, y + height - 3, 0, 0x1fffffff);
    }

    private record Tab(Text text, BiConsumer<ButtonWidget, MinecraftClient> onClick) {
    }

}
