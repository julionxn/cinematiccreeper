package me.julionxn.cinematiccreeper.items;

import me.julionxn.cinematiccreeper.CinematicCreeper;
import net.fabricmc.fabric.api.itemgroup.v1.FabricItemGroup;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

public class AllItems {

    public static final Item NPC_STICK = registerItem("npc_stick", new DebugItem());
    public static final Item GREEN_SCREEN_BLOCK_ITEM = registerItem("green_screen", new GreenScreenBlockItem(new Item.Settings()));

    public static final ItemGroup ITEM_GROUP = Registry.register(Registries.ITEM_GROUP,
            new Identifier(CinematicCreeper.MOD_ID, "group"),
            FabricItemGroup.builder()
                    .displayName(Text.translatable("cinematiccreeper.item_group"))
                    .icon(NPC_STICK::getDefaultStack)
                    .entries((ctx, entries) -> {
                        entries.add(NPC_STICK);
                        entries.add(GREEN_SCREEN_BLOCK_ITEM);
                    })
                    .build()
    );

    private static Item registerItem(String id, Item item) {
        return Registry.register(Registries.ITEM, new Identifier(CinematicCreeper.MOD_ID, id), item);
    }

    public static void register() {
        CinematicCreeper.LOGGER.info("Registering items.");
    }

}
