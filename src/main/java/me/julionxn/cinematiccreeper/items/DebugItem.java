package me.julionxn.cinematiccreeper.items;

import me.julionxn.cinematiccreeper.screen.gui.PresetsMenu;
import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.minecraft.client.MinecraftClient;
import net.minecraft.item.Item;
import net.minecraft.item.ItemUsageContext;
import net.minecraft.util.ActionResult;
import net.minecraft.util.math.BlockPos;

public class DebugItem extends Item {
    public DebugItem() {
        super(new FabricItemSettings());
    }

    @Override
    public ActionResult useOnBlock(ItemUsageContext context) {
        if (context.getWorld().isClient){
            BlockPos blockPos = context.getBlockPos().offset(context.getSide());
            openPresetsMenu(blockPos);
        }
        return super.useOnBlock(context);
    }

    private void openPresetsMenu(BlockPos blockPos){
        MinecraftClient client = MinecraftClient.getInstance();
        client.setScreen(new PresetsMenu(blockPos));
    }
}
