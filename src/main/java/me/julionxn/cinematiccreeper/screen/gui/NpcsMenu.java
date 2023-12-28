package me.julionxn.cinematiccreeper.screen.gui;

import me.julionxn.cinematiccreeper.presets.Preset;
import me.julionxn.cinematiccreeper.presets.PresetsManager;
import me.julionxn.cinematiccreeper.screen.gui.widgets.ExtendedScreen;
import me.julionxn.cinematiccreeper.screen.gui.widgets.ScrollItem;
import me.julionxn.cinematiccreeper.screen.gui.widgets.ScrollWidget;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.text.Text;

import java.util.ArrayList;
import java.util.List;

public class NpcsMenu extends ExtendedScreen {

    private final List<ScrollItem> scrollItems = new ArrayList<>();

    public NpcsMenu() {
        super(Text.of("NpcsMenu"));
        for (Preset preset : PresetsManager.getInstance().getPresets()) {
            scrollItems.add(new ScrollItem(preset.id(), buttonWidget -> {
                System.out.println(preset.id());
            }));
        }
    }

    @Override
    public void addWidgets() {
        ScrollWidget scrollWidget = new ScrollWidget(this, windowWidth / 2 - 85, windowHeight / 2 - 40, 150, 20, 5, scrollItems);
        addWidget(scrollWidget);
    }

    @Override
    public void addDrawables() {
        ButtonWidget addPresetButton = ButtonWidget.builder(Text.of("Nuevo Preset"), button -> {
            if (client == null) return;
            client.setScreen(new NewPresetMenu());
        }).dimensions(windowWidth / 2 - 85, windowHeight / 2 - 60, 150, 20).build();
        addDrawableChild(addPresetButton);
    }

}
