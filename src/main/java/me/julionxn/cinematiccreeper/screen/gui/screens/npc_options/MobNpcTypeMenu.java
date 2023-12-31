package me.julionxn.cinematiccreeper.screen.gui.screens.npc_options;

import me.julionxn.cinematiccreeper.core.presets.PresetOptions;
import net.minecraft.client.gui.widget.TextFieldWidget;
import net.minecraft.entity.Entity;
import net.minecraft.text.Text;
import org.jetbrains.annotations.Nullable;

import java.util.function.Consumer;

public class MobNpcTypeMenu extends NpcTypeMenu {

    public MobNpcTypeMenu(String entityType, Consumer<PresetOptions> onReady, Runnable onCancel, PresetOptions presetOptions, @Nullable Entity entity) {
        super(entityType, onReady, onCancel, presetOptions, entity);
    }

    @Override
    public void addDrawables() {
        super.addDrawables();
        if (client == null) return;
        TextFieldWidget holdingItemField = new TextFieldWidget(client.textRenderer, x + 20, y + 20, 200, 20, Text.of("Holding"));
        holdingItemField.setChangedListener(presetOptions::setHoldingItem);
        holdingItemField.setText(presetOptions.getHoldingItem());
        addDrawableChild(holdingItemField);
    }
}
