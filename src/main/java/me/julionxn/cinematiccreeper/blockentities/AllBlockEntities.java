package me.julionxn.cinematiccreeper.blockentities;

import me.julionxn.cinematiccreeper.CinematicCreeper;
import me.julionxn.cinematiccreeper.blocks.AllBlocks;
import net.fabricmc.fabric.api.object.builder.v1.block.entity.FabricBlockEntityTypeBuilder;
import net.minecraft.block.Block;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;

public class AllBlockEntities {

    public static final BlockEntityType<GreenScreenBlockEntity> GREEN_SCREEN_BLOCK_ENTITY = registerBlockEntity("green_screen_block_entity",
            GreenScreenBlockEntity::new, AllBlocks.GREEN_SCREEN_BLOCK);

    private static <T extends BlockEntity> BlockEntityType<T> registerBlockEntity(String id, FabricBlockEntityTypeBuilder.Factory<T> factory, Block block){
        return Registry.register(Registries.BLOCK_ENTITY_TYPE, new Identifier(CinematicCreeper.MOD_ID, id), FabricBlockEntityTypeBuilder.create(factory, block).build());
    }

    public static void register() {
        CinematicCreeper.LOGGER.info("Registering block entities.");
    }

}
