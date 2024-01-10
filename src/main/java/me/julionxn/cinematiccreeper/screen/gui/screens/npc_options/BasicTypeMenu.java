package me.julionxn.cinematiccreeper.screen.gui.screens.npc_options;

import me.julionxn.cinematiccreeper.core.presets.PresetOptions;
import me.julionxn.cinematiccreeper.screen.gui.components.widgets.TextFieldWithDescription;
import me.julionxn.cinematiccreeper.screen.gui.components.widgets.ToggleWidget;
import net.minecraft.entity.Entity;
import net.minecraft.text.Text;
import org.jetbrains.annotations.Nullable;

import java.util.function.Consumer;

public class BasicTypeMenu extends NpcTypeMenu {

    private TextFieldWithDescription displayNameField;

    public BasicTypeMenu(String entityType, Consumer<PresetOptions> onReady, Runnable onCancel, PresetOptions presetOptions, @Nullable Entity entity) {
        super(entityType, onReady, onCancel, presetOptions, entity);
    }

    @Override
    public void addWidgets() {
        super.addWidgets();
        if (client == null) return;
        displayNameField = new TextFieldWithDescription(this,
                client.textRenderer, x + 20, y + 20, 140, 100, 20, Text.translatable("screen.cinematiccreeper.display_name"));
        addWidget(displayNameField);
    }

    @Override
    public void addDrawables() {
        super.addDrawables();
        if (client == null) return;

        displayNameField.getTextFieldWidget().setChangedListener(presetOptions::setDisplayName);
        displayNameField.getTextFieldWidget().setText(presetOptions.getDisplayName());

        ToggleWidget showNameWidget = new ToggleWidget(x + 145, y + 45, 120, 20, Text.translatable("screen.cinematiccreeper.show_name"),
                () -> presetOptions.setShowDisplayName(true),
                () -> presetOptions.setShowDisplayName(false));
        showNameWidget.setActive(presetOptions.showDisplayName());
        addDrawableChild(showNameWidget);

        ToggleWidget sneakingWidget = new ToggleWidget(x + 15, y + 45, 120, 20, Text.translatable("screen.cinematiccreeper.sneaking"),
                () -> presetOptions.setSneaking(true),
                () -> presetOptions.setSneaking(false));
        sneakingWidget.setActive(presetOptions.isSneaking());
        addDrawableChild(sneakingWidget);
    }
}
