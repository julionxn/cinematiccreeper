package me.julionxn.cinematiccreeper.screen.gui.screens.npc_options;

import me.julionxn.cinematiccreeper.core.managers.NpcPosesManager;
import me.julionxn.cinematiccreeper.core.notifications.Notification;
import me.julionxn.cinematiccreeper.core.notifications.NotificationManager;
import me.julionxn.cinematiccreeper.core.poses.NpcPose;
import me.julionxn.cinematiccreeper.core.poses.PoseAnimator;
import me.julionxn.cinematiccreeper.core.presets.PresetOptions;
import me.julionxn.cinematiccreeper.entity.NpcEntity;
import me.julionxn.cinematiccreeper.entity.NpcEntityRenderer;
import me.julionxn.cinematiccreeper.screen.gui.components.widgets.RemovableItemsScrollWidget;
import me.julionxn.cinematiccreeper.screen.gui.screens.poses.AddNewNpcPoseMenu;
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
        RemovableItemsScrollWidget scrollWidget = RemovableItemsScrollWidget.builder(this, () -> {
                    setPoses(npcEntity);
                    return poses;
                })
                .pos(x + 80, y + 40)
                .itemsDimensions(100, 20)
                .itemsPerPage(7)
                .build();
        addWidget(scrollWidget);
    }

    private void setPoses(NpcEntity npcEntity){
        poses.clear();
        poses.add(new RemovableItemsScrollWidget.RemovableScrollItem(Text.translatable("screen.cinematiccreeper.none").getString(), buttonWidget -> {
            npcEntity.clearNpcPose();
            PoseAnimator poseAnimator = NpcEntityRenderer.models.get(npcEntity.getId());
            if (poseAnimator == null) return;
            poseAnimator.stop();
        }, buttonWidget -> {}));
        for (Map.Entry<String, NpcPose> entry : NpcPosesManager.getInstance().getLoadedPoses().entrySet()) {
            poses.add(new RemovableItemsScrollWidget.RemovableScrollItem(entry.getKey(), buttonWidget -> {
                npcEntity.setNpcPose(entry.getValue());
                PoseAnimator poseAnimator = NpcEntityRenderer.models.get(npcEntity.getId());
                if (poseAnimator == null) return;
                poseAnimator.reset();
                poseAnimator.play();
                NotificationManager.getInstance().add(Notification.SAVED);
            }, buttonWidget -> {
                NpcPosesManager.getInstance().removeNpcPose(entry.getKey());
                NpcPose npcPose = npcEntity.getNpcPose();
                if (npcPose != null && npcPose.equals(entry.getValue())){
                    npcEntity.clearNpcPose();
                    PoseAnimator poseAnimator = NpcEntityRenderer.models.get(npcEntity.getId());
                    if (poseAnimator == null) return;
                    poseAnimator.stop();
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
