package me.julionxn.cinematiccreeper.screen.gui.screens.npc_options;

import me.julionxn.cinematiccreeper.managers.presets.PresetOptions;

import java.util.function.Consumer;

public class MobNpcTypeMenu extends NpcTypeMenu{

    public MobNpcTypeMenu(String entityType, Consumer<PresetOptions> onReady, Runnable onCancel, PresetOptions presetOptions) {
        super(entityType, onReady, onCancel, presetOptions);
    }

}
