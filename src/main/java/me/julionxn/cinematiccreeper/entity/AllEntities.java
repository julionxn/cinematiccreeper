package me.julionxn.cinematiccreeper.entity;

import me.julionxn.cinematiccreeper.CinematicCreeper;
import net.fabricmc.fabric.api.object.builder.v1.entity.FabricEntityTypeBuilder;
import net.minecraft.entity.EntityDimensions;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnGroup;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;

public class AllEntities {

    public static final EntityType<NpcEntity> NPC_ENTITY = Registry.register(
            Registries.ENTITY_TYPE,
            new Identifier(CinematicCreeper.MOD_ID, "npc_entity"),
            FabricEntityTypeBuilder.create(SpawnGroup.MONSTER, NpcEntity::new)
                    .dimensions(EntityDimensions.fixed(0.6f, 1.8f))
                    .build()
    );

    public static void register() {
        CinematicCreeper.LOGGER.info("xd");
    }

}
