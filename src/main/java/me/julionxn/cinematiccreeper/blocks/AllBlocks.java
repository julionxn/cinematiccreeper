package me.julionxn.cinematiccreeper.blocks;

import me.julionxn.cinematiccreeper.CinematicCreeper;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;

public class AllBlocks {

    public static final Block GREEN_SCREEN_BLOCK = registerBlock("green_screen",
            new GreenScreenBlock(AbstractBlock.Settings.create()));

    private static Block registerBlock(String id, Block block) {
        return Registry.register(Registries.BLOCK, new Identifier(CinematicCreeper.MOD_ID, id), block);
    }

    public static void register() {
        CinematicCreeper.LOGGER.info("Registering blocks.");
    }

}
