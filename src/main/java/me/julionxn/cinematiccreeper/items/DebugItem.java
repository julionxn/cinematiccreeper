package me.julionxn.cinematiccreeper.items;

import me.julionxn.cinematiccreeper.core.presets.PresetOptions;
import me.julionxn.cinematiccreeper.core.presets.PresetOptionsHandlers;
import me.julionxn.cinematiccreeper.screen.ScreenWrappers;
import me.julionxn.cinematiccreeper.util.mixins.NpcData;
import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUsageContext;
import net.minecraft.text.Text;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class DebugItem extends Item {

    public DebugItem() {
        super(new FabricItemSettings());
    }

    @Override
    public ActionResult useOnEntity(ItemStack stack, PlayerEntity user, LivingEntity entity, Hand hand) {
        if (((NpcData) entity).cinematiccreeper$isNpc()) {
            if (user.getWorld().isClient) {
                PresetOptions presetOptions = PresetOptionsHandlers.fromEntity(entity);
                ScreenWrappers.openBasicTypeMenu(entity, presetOptions);
            }
        }
        return super.useOnEntity(stack, user, entity, hand);
    }

    @Override
    public ActionResult useOnBlock(ItemUsageContext context) {
        if (context.getWorld().isClient) {
            BlockPos blockPos = context.getBlockPos().offset(context.getSide());
            ScreenWrappers.openPresetMenu(blockPos);
        }
        return super.useOnBlock(context);
    }

    @Override
    public void appendTooltip(ItemStack stack, @Nullable World world, List<Text> tooltip, TooltipContext context) {
        super.appendTooltip(stack, world, tooltip, context);
        tooltip.add(Text.translatable("tooltip.cinematiccreeper.npc_stick"));
    }

}
