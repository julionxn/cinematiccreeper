package me.julionxn.cinematiccreeper.screen.gui.screens.npc_options;

import me.julionxn.cinematiccreeper.managers.presets.PresetOptions;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.entity.Entity;
import net.minecraft.text.Text;
import org.jetbrains.annotations.Nullable;

import java.util.function.Consumer;

public class PathAwareNpcTypeMenu extends NpcTypeMenu{

    public PathAwareNpcTypeMenu(String entityType, Consumer<PresetOptions> onReady, Runnable onCancel, PresetOptions presetOptions, @Nullable Entity entity) {
        super(entityType, onReady, onCancel, presetOptions, entity);
    }

    @Override
    public void addDrawables() {
        super.addDrawables();
        x += 20;
        y += 20;
        if (entity == null) return;
        ButtonWidget addPath = ButtonWidget.builder(Text.of("Añadir path"), button -> {
            
        }).dimensions(x, y, 150, 20).build();
    }
}
