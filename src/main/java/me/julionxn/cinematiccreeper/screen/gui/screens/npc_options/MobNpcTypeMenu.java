package me.julionxn.cinematiccreeper.screen.gui.screens.npc_options;

import me.julionxn.cinematiccreeper.core.presets.PresetOptions;
import me.julionxn.cinematiccreeper.screen.gui.components.widgets.TextFieldWithDescription;
import net.minecraft.entity.Entity;
import net.minecraft.text.Text;
import org.jetbrains.annotations.Nullable;

import java.util.function.Consumer;

public class MobNpcTypeMenu extends NpcTypeMenu {

    private TextFieldWithDescription holdingItemField;

    public MobNpcTypeMenu(String entityType, Consumer<PresetOptions> onReady, Runnable onCancel, PresetOptions presetOptions, @Nullable Entity entity) {
        super(entityType, onReady, onCancel, presetOptions, entity);
    }

    @Override
    public void addWidgets() {
        super.addWidgets();
        if (client == null) return;
        holdingItemField = new TextFieldWithDescription(this,
                client.textRenderer, x + 20, y + 20, 140, 100, 20, Text.translatable("screen.cinematiccreeper.item_in_hand"));
        addWidget(holdingItemField);
    }

    @Override
    public void addDrawables() {
        super.addDrawables();
        if (client == null) return;
        holdingItemField.getTextFieldWidget().setChangedListener(presetOptions::setHoldingItem);
        holdingItemField.getTextFieldWidget().setText(presetOptions.getHoldingItem());
    }
}
