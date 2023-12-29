package me.julionxn.cinematiccreeper.items;

import me.julionxn.cinematiccreeper.screen.gui.PresetsMenu;
import me.julionxn.cinematiccreeper.util.mixins.NpcData;
import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUsageContext;
import net.minecraft.text.Text;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;

public class DebugItem extends Item {
    public DebugItem() {
        super(new FabricItemSettings());
    }

    @Override
    public ActionResult useOnEntity(ItemStack stack, PlayerEntity user, LivingEntity entity, Hand hand) {
        if (entity instanceof MobEntity mobEntity && ((NpcData) mobEntity).cinematiccreeper$isNpc()){
            user.sendMessage(Text.of("HOLA"));
        }
        return super.useOnEntity(stack, user, entity, hand);
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
