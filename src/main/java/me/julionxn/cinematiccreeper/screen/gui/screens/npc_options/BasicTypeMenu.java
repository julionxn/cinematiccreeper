package me.julionxn.cinematiccreeper.screen.gui.screens.npc_options;

import me.julionxn.cinematiccreeper.managers.presets.PresetOptions;
import me.julionxn.cinematiccreeper.screen.gui.components.widgets.ToggleWidget;
import net.minecraft.client.gui.widget.TextFieldWidget;
import net.minecraft.text.Text;

import java.util.function.Consumer;

public class BasicTypeMenu extends NpcTypeMenu{

    public BasicTypeMenu(String entityType, Consumer<PresetOptions> onReady, Runnable onCancel, PresetOptions presetOptions) {
        super(entityType, onReady, onCancel, presetOptions);
    }

    @Override
    public void addDrawables() {
        super.addDrawables();
        if (client == null) return;

        TextFieldWidget customNameField = new TextFieldWidget(client.textRenderer, x + 20, y + 20, 100, 20, Text.of("Name"));
        customNameField.setChangedListener(presetOptions::setDisplayName);
        customNameField.setText(presetOptions.getDisplayName());
        addDrawableChild(customNameField);

        ToggleWidget showNameWidget = new ToggleWidget( x + 130, y + 20, 150, 20, Text.of("Mostrar nombre"),
                () -> presetOptions.setShowDisplayName(true),
                () -> presetOptions.setShowDisplayName(false));
        showNameWidget.setActive(presetOptions.showDisplayName());
        addDrawableChild(showNameWidget);

        ToggleWidget sneakingWidget = new ToggleWidget(x + 20, y + 50, 130, 20, Text.of("Sneaking"),
                () -> presetOptions.setSneaking(true),
                () -> presetOptions.setSneaking(false));
        sneakingWidget.setActive(presetOptions.isSneaking());
        addDrawableChild(sneakingWidget);
    }
}
