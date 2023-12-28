package me.julionxn.cinematiccreeper.items;

import me.julionxn.cinematiccreeper.screen.gui.PresetsMenu;
import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.world.World;

public class DebugItem extends Item {
    public DebugItem() {
        super(new FabricItemSettings());
    }

    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
        if (world.isClient){
            MinecraftClient client = MinecraftClient.getInstance();
            client.setScreen(new PresetsMenu());
        }
        return super.use(world, user, hand);
    }
}
