package me.julionxn.cinematiccreeper;

import me.julionxn.cinematiccreeper.entity.AllEntities;
import me.julionxn.cinematiccreeper.entity.NpcEntity;
import me.julionxn.cinematiccreeper.items.AllItems;
import me.julionxn.cinematiccreeper.networking.AllPackets;
import me.julionxn.cinematiccreeper.presets.PresetsManager;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.object.builder.v1.entity.FabricDefaultAttributeRegistry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CinematicCreeper implements ModInitializer {

    public static final String MOD_ID = "cinematiccreeper";
	public static final Logger LOGGER = LoggerFactory.getLogger("cinematiccreeper");

	@Override
	public void onInitialize() {
		PresetsManager.getInstance().load();
		AllPackets.registerC2SPackets();
		AllItems.register();
		AllEntities.register();
		FabricDefaultAttributeRegistry.register(AllEntities.NPC_ENTITY, NpcEntity.createPlayerAttributes());
		LOGGER.info("Hello Fabric world!");
	}
}