package me.julionxn.cinematiccreeper.items;

import me.julionxn.cinematiccreeper.blocks.AllBlocks;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.item.BlockItem;
import net.minecraft.item.ItemStack;
import net.minecraft.text.Text;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class GreenScreenBlockItem extends BlockItem {

    public GreenScreenBlockItem(Settings settings) {
        super(AllBlocks.GREEN_SCREEN_BLOCK, settings);
    }

    @Override
    public void appendTooltip(ItemStack stack, @Nullable World world, List<Text> tooltip, TooltipContext context) {
        super.appendTooltip(stack, world, tooltip, context);
        tooltip.add(Text.translatable("tooltip.cinematiccreeper.green_screen"));
    }
}
