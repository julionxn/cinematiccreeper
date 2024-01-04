package me.julionxn.cinematiccreeper.screen.gui.screens.npc_options;

import me.julionxn.cinematiccreeper.entity.NpcEntity;
import me.julionxn.cinematiccreeper.managers.NpcPosesManager;
import me.julionxn.cinematiccreeper.managers.presets.PresetOptions;
import me.julionxn.cinematiccreeper.poses.NpcPose;
import me.julionxn.cinematiccreeper.screen.gui.components.widgets.RemovableItemsScrollWidget;
import me.julionxn.cinematiccreeper.screen.gui.screens.npc_options.poses.AddNewNpcPoseMenu;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.entity.Entity;
import net.minecraft.text.Text;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

public class NpcMenu extends NpcTypeMenu {

    private final List<RemovableItemsScrollWidget.RemovableScrollItem> poses = new ArrayList<>();

    public NpcMenu(String entityType, Consumer<PresetOptions> onReady, Runnable onCancel, PresetOptions presetOptions, @Nullable Entity entity) {
        super(entityType, onReady, onCancel, presetOptions, entity);
    }

    @Override
    public void addWidgets() {
        super.addWidgets();
        if (entity == null) return;
        NpcEntity npcEntity = (NpcEntity) entity;
        setPoses(npcEntity);
        RemovableItemsScrollWidget scrollWidget = new RemovableItemsScrollWidget(this,
                x + 80, y + 40, 100, 20, 7, () -> {
            setPoses(npcEntity);
            return poses;
        });
        addWidget(scrollWidget);
    }

    private void setPoses(NpcEntity npcEntity){
        poses.clear();
        poses.add(new RemovableItemsScrollWidget.RemovableScrollItem("Ninguno", buttonWidget -> {
            npcEntity.clearNpcPose();
        }, buttonWidget -> {}));
        for (Map.Entry<String, NpcPose> entry : NpcPosesManager.getInstance().getLoadedPoses().entrySet()) {
            poses.add(new RemovableItemsScrollWidget.RemovableScrollItem(entry.getKey(), buttonWidget -> {
                npcEntity.setNpcPose(entry.getValue());
            }, buttonWidget -> {
                NpcPosesManager.getInstance().removeNpcPose(entry.getKey());
                NpcPose npcPose = npcEntity.getNpcPose();
                if (npcPose != null && npcPose.equals(entry.getValue())){
                    npcEntity.clearNpcPose();
                }
                clear();
            }));
        }
    }

    @Override
    public void addDrawables() {
        super.addDrawables();
        ButtonWidget addNewButton = ButtonWidget.builder(Text.of("+"), button -> {
            if (client == null) return;
            client.setScreen(new AddNewNpcPoseMenu(this));
        }).dimensions(x + 140, y + 20, 20, 20).build();
        addDrawableChild(addNewButton);
    }

}
