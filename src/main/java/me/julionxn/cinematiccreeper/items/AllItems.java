package me.julionxn.cinematiccreeper.items;

import me.julionxn.cinematiccreeper.CinematicCreeper;
import net.minecraft.item.Item;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;

public class AllItems {

    public static final Item DEBUG_STICK = registerItem("debug_item", new DebugItem());

    private static Item registerItem(String id, Item item) {
        return Registry.register(Registries.ITEM, new Identifier(CinematicCreeper.MOD_ID, id), item);
    }

    public static void register() {
        CinematicCreeper.LOGGER.info("Registering items.");
    }

}
