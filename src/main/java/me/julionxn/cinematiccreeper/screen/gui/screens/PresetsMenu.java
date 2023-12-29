package me.julionxn.cinematiccreeper.screen.gui.screens;

import me.julionxn.cinematiccreeper.entity.NpcEntity;
import me.julionxn.cinematiccreeper.managers.PresetsManager;
import me.julionxn.cinematiccreeper.managers.presets.Preset;
import me.julionxn.cinematiccreeper.managers.presets.PresetOptions;
import me.julionxn.cinematiccreeper.networking.AllPackets;
import me.julionxn.cinematiccreeper.screen.gui.components.ExtendedScreen;
import me.julionxn.cinematiccreeper.screen.gui.components.widgets.ScrollItem;
import me.julionxn.cinematiccreeper.screen.gui.components.widgets.ScrollWidget;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.text.Text;
import net.minecraft.util.math.BlockPos;

import java.util.ArrayList;
import java.util.List;

public class PresetsMenu extends ExtendedScreen {

    private final List<ScrollItem> scrollItems = new ArrayList<>();
    private final BlockPos blockPos;

    public PresetsMenu(BlockPos blockPos) {
        super(Text.of("NpcsMenu"));
        for (Preset preset : PresetsManager.getInstance().getPresets()) {
            String entityType = preset.getEntityType();
            scrollItems.add(new ScrollItem(preset.getId(), buttonWidget -> {
                PacketByteBuf buf = PacketByteBufs.create();
                buf.writeBlockPos(blockPos).writeString(preset.getId());
                if (entityType.equals(NpcEntity.ENTITY_ID)){
                    buf.writeString(preset.getOptions().getSkinUrl());
                    PresetOptions.addToBuf(buf, preset.getOptions());
                    ClientPlayNetworking.send(AllPackets.C2S_SPAWN_NPC_PRESET, buf);
                } else {
                    buf.writeString(entityType);
                    PresetOptions.addToBuf(buf, preset.getOptions());
                    ClientPlayNetworking.send(AllPackets.C2S_SPAWN_PRESET, buf);
                }
                close();
            }));
        }
        this.blockPos = blockPos;
    }

    @Override
    public void addWidgets() {
        ScrollWidget scrollWidget = new ScrollWidget(this, windowWidth / 2 - 85, windowHeight / 2 - 40, 150, 20, 5, scrollItems);
        addWidget(scrollWidget);
    }

    @Override
    public void addDrawables() {
        ButtonWidget addPresetButton = ButtonWidget.builder(Text.of("Nuevo Preset"), button -> {
            if (client == null) return;
            client.setScreen(new NewPresetMenu(blockPos));
        }).dimensions(windowWidth / 2 - 85, windowHeight / 2 - 60, 150, 20).build();
        addDrawableChild(addPresetButton);
    }

}
