package me.julionxn.cinematiccreeper.screen;

import me.julionxn.cinematiccreeper.core.presets.PresetOptions;
import me.julionxn.cinematiccreeper.core.presets.PresetOptionsHandlers;
import me.julionxn.cinematiccreeper.networking.AllPackets;
import me.julionxn.cinematiccreeper.screen.gui.screens.ChangeGreenScreenColor;
import me.julionxn.cinematiccreeper.screen.gui.screens.PresetsMenu;
import me.julionxn.cinematiccreeper.screen.gui.screens.npc_options.BasicTypeMenu;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.Entity;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.registry.Registries;
import net.minecraft.util.math.BlockPos;

public class ScreenWrappers {

    public static void openBasicTypeMenu(Entity entity, PresetOptions presetOptions){
        MinecraftClient.getInstance().setScreen(
                new BasicTypeMenu(
                        Registries.ENTITY_TYPE.getId(entity.getType()).toString(),
                        presetOptions1 -> {
                            PacketByteBuf buf = PacketByteBufs.create();
                            buf.writeUuid(entity.getUuid());
                            buf.writeBoolean(false);
                            PresetOptionsHandlers.addToBuf(buf, presetOptions1);
                            ClientPlayNetworking.send(AllPackets.C2S_APPLY_PRESET_OPTIONS, buf);
                            MinecraftClient.getInstance().setScreen(null);
                        },
                        () -> MinecraftClient.getInstance().setScreen(null),
                        presetOptions, entity
                ));
    }

    public static void openPresetMenu(BlockPos blockPos){
        MinecraftClient client = MinecraftClient.getInstance();
        client.setScreen(new PresetsMenu(blockPos));
    }

    public static void openGreenScreenMenu(BlockPos blockPos, int color){
        MinecraftClient.getInstance().setScreen(new ChangeGreenScreenColor(blockPos, color));
    }

}
