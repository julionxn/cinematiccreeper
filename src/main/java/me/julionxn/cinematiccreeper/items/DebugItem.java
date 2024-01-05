package me.julionxn.cinematiccreeper.items;

import me.julionxn.cinematiccreeper.core.presets.PresetOptions;
import me.julionxn.cinematiccreeper.core.presets.PresetOptionsHandlers;
import me.julionxn.cinematiccreeper.networking.AllPackets;
import me.julionxn.cinematiccreeper.screen.gui.screens.PresetsMenu;
import me.julionxn.cinematiccreeper.screen.gui.screens.npc_options.BasicTypeMenu;
import me.julionxn.cinematiccreeper.util.mixins.NpcData;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUsageContext;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.registry.Registries;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;

public class DebugItem extends Item {

    public DebugItem() {
        super(new FabricItemSettings());
    }

    @Override
    public ActionResult useOnEntity(ItemStack stack, PlayerEntity user, LivingEntity entity, Hand hand) {
        if (((NpcData) entity).cinematiccreeper$isNpc()) {
            if (user.getWorld().isClient) {
                PresetOptions presetOptions = PresetOptionsHandlers.fromEntity(entity);
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
        }
        return super.useOnEntity(stack, user, entity, hand);
    }

    @Override
    public ActionResult useOnBlock(ItemUsageContext context) {
        if (context.getWorld().isClient) {
            BlockPos blockPos = context.getBlockPos().offset(context.getSide());
            openPresetsMenu(blockPos);
        }
        return super.useOnBlock(context);
    }

    private void openPresetsMenu(BlockPos blockPos) {
        MinecraftClient client = MinecraftClient.getInstance();
        client.setScreen(new PresetsMenu(blockPos));
    }
}
