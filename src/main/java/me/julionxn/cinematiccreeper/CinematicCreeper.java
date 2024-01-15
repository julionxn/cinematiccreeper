package me.julionxn.cinematiccreeper;

import me.julionxn.cinematiccreeper.blockentities.AllBlockEntities;
import me.julionxn.cinematiccreeper.blocks.AllBlocks;
import me.julionxn.cinematiccreeper.core.managers.NpcsManager;
import me.julionxn.cinematiccreeper.entity.AllEntities;
import me.julionxn.cinematiccreeper.entity.NpcEntity;
import me.julionxn.cinematiccreeper.items.AllItems;
import me.julionxn.cinematiccreeper.networking.AllPackets;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerWorldEvents;
import net.fabricmc.fabric.api.object.builder.v1.entity.FabricDefaultAttributeRegistry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CinematicCreeper implements ModInitializer {

    public static final String MOD_ID = "cinematiccreeper";
    public static final Logger LOGGER = LoggerFactory.getLogger("cinematiccreeper");

    @Override
    public void onInitialize() {
        NpcsManager.getInstance().load();
        AllPackets.registerC2SPackets();
        AllItems.register();
        AllBlocks.register();
        AllBlockEntities.register();
        AllEntities.register();
        FabricDefaultAttributeRegistry.register(AllEntities.NPC_ENTITY, NpcEntity.createPlayerAttributes());
        ServerWorldEvents.UNLOAD.register((server, world) -> NpcsManager.getInstance().save());
        LOGGER.info("Hello Fabric world!");
    }
}