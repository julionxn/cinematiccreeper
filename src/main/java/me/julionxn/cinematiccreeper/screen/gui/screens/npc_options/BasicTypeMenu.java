package me.julionxn.cinematiccreeper.screen.gui.screens.npc_options;

import me.julionxn.cinematiccreeper.core.presets.PresetOptions;
import me.julionxn.cinematiccreeper.screen.gui.components.widgets.ToggleWidget;
import net.minecraft.client.gui.widget.TextFieldWidget;
import net.minecraft.entity.Entity;
import net.minecraft.text.Text;
import org.jetbrains.annotations.Nullable;

import java.util.function.Consumer;

public class BasicTypeMenu extends NpcTypeMenu {

    public BasicTypeMenu(String entityType, Consumer<PresetOptions> onReady, Runnable onCancel, PresetOptions presetOptions, @Nullable Entity entity) {
        super(entityType, onReady, onCancel, presetOptions, entity);
    }

    @Override
    public void addDrawables() {
        super.addDrawables();
        if (client == null) return;

        TextFieldWidget customNameField = new TextFieldWidget(client.textRenderer, x + 20, y + 20, 100, 20, Text.of("Name"));
        customNameField.setChangedListener(presetOptions::setDisplayName);
        customNameField.setText(presetOptions.getDisplayName());
        addDrawableChild(customNameField);

        ToggleWidget showNameWidget = new ToggleWidget(x + 130, y + 20, 150, 20, Text.translatable("gui.cinematiccreeper.show_name"),
                () -> presetOptions.setShowDisplayName(true),
                () -> presetOptions.setShowDisplayName(false));
        showNameWidget.setActive(presetOptions.showDisplayName());
        addDrawableChild(showNameWidget);

        ToggleWidget sneakingWidget = new ToggleWidget(x + 20, y + 50, 130, 20, Text.translatable("gui.cinematiccreeper.sneaking"),
                () -> presetOptions.setSneaking(true),
                () -> presetOptions.setSneaking(false));
        sneakingWidget.setActive(presetOptions.isSneaking());
        addDrawableChild(sneakingWidget);
    }
}
