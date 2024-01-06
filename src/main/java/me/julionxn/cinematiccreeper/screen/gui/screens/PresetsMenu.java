package me.julionxn.cinematiccreeper.screen.gui.screens;

import me.julionxn.cinematiccreeper.core.managers.PresetsManager;
import me.julionxn.cinematiccreeper.core.presets.Preset;
import me.julionxn.cinematiccreeper.core.presets.PresetOptionsHandlers;
import me.julionxn.cinematiccreeper.entity.NpcEntity;
import me.julionxn.cinematiccreeper.networking.AllPackets;
import me.julionxn.cinematiccreeper.screen.gui.components.ExtendedScreen;
import me.julionxn.cinematiccreeper.screen.gui.components.widgets.RemovableItemsScrollWidget;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.text.Text;
import net.minecraft.util.math.BlockPos;

import java.util.ArrayList;
import java.util.List;

public class PresetsMenu extends ExtendedScreen {

    private final List<RemovableItemsScrollWidget.RemovableScrollItem> scrollItems = new ArrayList<>();
    private final BlockPos blockPos;

    public PresetsMenu(BlockPos blockPos) {
        super(Text.of("NpcsMenu"));
        setItems();
        this.blockPos = blockPos;
    }

    private void setItems() {
        scrollItems.clear();
        for (Preset preset : PresetsManager.getInstance().getPresets()) {
            String entityType = preset.getEntityType();
            scrollItems.add(new RemovableItemsScrollWidget.RemovableScrollItem(preset.getId(), buttonWidget -> {
                PacketByteBuf buf = PacketByteBufs.create();
                buf.writeBlockPos(blockPos).writeString(preset.getId());
                if (entityType.equals(NpcEntity.ENTITY_ID)) {
                    buf.writeString(preset.getOptions().getSkinUrl());
                    PresetOptionsHandlers.addToBuf(buf, preset.getOptions());
                    ClientPlayNetworking.send(AllPackets.C2S_SPAWN_NPC_PRESET, buf);
                } else {
                    buf.writeString(entityType);
                    PresetOptionsHandlers.addToBuf(buf, preset.getOptions());
                    ClientPlayNetworking.send(AllPackets.C2S_SPAWN_PRESET, buf);
                }
                close();
            }, buttonWidget -> {
                PresetsManager.getInstance().removePresetWithId(preset.getId());
                setItems();
            }));
        }
    }

    @Override
    public void addWidgets() {
        RemovableItemsScrollWidget scrollWidget = new RemovableItemsScrollWidget(this,
                windowWidth / 2 - 95, windowHeight / 2 - 40,
                150, 20, 5,
                () -> this.scrollItems);
        addWidget(scrollWidget);
    }

    @Override
    public void addDrawables() {
        ButtonWidget addPresetButton = ButtonWidget.builder(Text.translatable("gui.cinematiccreeper.new"), button -> {
            if (client == null) return;
            client.setScreen(new NewPresetMenu(blockPos));
        }).dimensions(windowWidth / 2 - 30, windowHeight / 2 - 60, 60, 20).build();
        addDrawableChild(addPresetButton);
    }

}
