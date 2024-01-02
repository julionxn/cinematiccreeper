package me.julionxn.cinematiccreeper.screen.gui.screens.npc_options;

import me.julionxn.cinematiccreeper.entity.NpcEntity;
import me.julionxn.cinematiccreeper.managers.PresetsManager;
import me.julionxn.cinematiccreeper.managers.paths.Path;
import me.julionxn.cinematiccreeper.managers.presets.PresetOptions;
import me.julionxn.cinematiccreeper.managers.presets.PresetOptionsHandlers;
import me.julionxn.cinematiccreeper.networking.AllPackets;
import me.julionxn.cinematiccreeper.screen.gui.components.widgets.RemovableItemsScrollWidget;
import me.julionxn.cinematiccreeper.screen.gui.components.widgets.ScrollWidget;
import me.julionxn.cinematiccreeper.screen.gui.screens.NewPathMenu;
import me.julionxn.cinematiccreeper.util.mixins.PathAwareData;
import me.julionxn.cinematiccreeper.util.mixins.PlayerData;
import me.julionxn.cinematiccreeper.managers.paths.PlayerPathState;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.text.Text;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public class PathAwareNpcTypeMenu extends NpcTypeMenu {

    private final List<ScrollWidget.ScrollItem> scrollItems = new ArrayList<>();

    public PathAwareNpcTypeMenu(String entityType, Consumer<PresetOptions> onReady, Runnable onCancel, PresetOptions presetOptions, @Nullable Entity entity) {
        super(entityType, onReady, onCancel, presetOptions, entity);
        setItems();
    }

    private void setItems(){
        if (entity == null) return;
        scrollItems.clear();
        scrollItems.add(new ScrollWidget.ScrollItem("Ninguno", buttonWidget -> {
            PacketByteBuf buf = PacketByteBufs.create();
            buf.writeInt(entity.getId());
            ClientPlayNetworking.send(AllPackets.C2S_CLEAR_PATH_OF_ENTITY, buf);
        }));
        PathAwareData data = (PathAwareData) entity;
        List<Path> paths = data.cinematiccreeper$getPaths();
        for (Path path : paths) {
            scrollItems.add(new ScrollWidget.ScrollItem(path.getId(), buttonWidget -> {
                PacketByteBuf buf = PacketByteBufs.create();
                Path.addToBuf(buf, path);
                ClientPlayNetworking.send(AllPackets.C2S_SET_PATH_TO_ENTITY, buf);
                close();
            }));
        }
    }

    @Override
    public void addWidgets() {
        super.addWidgets();
        ScrollWidget scrollWidget = new ScrollWidget(this, x + 20, y + 20,
                80, 20,
                8, scrollItems);
        addWidget(scrollWidget);
    }

    @Override
    public void addDrawables() {
        super.addDrawables();
        if (client == null) return;
        if (entity == null) return;
        PlayerEntity player = client.player;
        if (player == null) return;
        ButtonWidget addPath = ButtonWidget.builder(Text.of("Añadir path"), button -> {
            client.setScreen(new NewPathMenu(entity.getId(), PlayerPathState.State.ADDING));
        }).dimensions(x + 130, y + 20, 150, 20).build();
        addDrawableChild(addPath);
        ButtonWidget recordPath = ButtonWidget.builder(Text.of("Grabar path"), button -> {
            client.setScreen(new NewPathMenu(entity.getId(), PlayerPathState.State.RECORDING));
        }).dimensions(x + 130, y + 40, 150, 20).build();
        addDrawableChild(recordPath);
    }
}
