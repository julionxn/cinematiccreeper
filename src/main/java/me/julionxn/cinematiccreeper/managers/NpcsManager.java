package me.julionxn.cinematiccreeper.managers;

import com.google.gson.annotations.Expose;
import me.julionxn.cinematiccreeper.entity.AllEntities;
import me.julionxn.cinematiccreeper.entity.NpcEntity;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.entity.mob.PathAwareEntity;
import net.minecraft.registry.Registries;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.world.World;

import java.util.*;

public class NpcsManager extends SerializableJsonManager<NpcsManager> {

    private final Map<String, EntityType<?>> loadedEntityTypes = new HashMap<>();
    @Expose
    private Map<String, List<UUID>> trackedEntities = new HashMap<>();

    private NpcsManager() {
        super("cc_tracked.json", NpcsManager.class);
    }

    public static NpcsManager getInstance() {
        return NpcsManager.SingletonHolder.INSTANCE;
    }

    public static String worldIdentifier(ServerWorld world) {
        return world.worldProperties.getLevelName();
    }

    @Override
    protected void onLoad(NpcsManager data) {
        loadedEntityTypes.put(NpcEntity.ENTITY_ID, AllEntities.NPC_ENTITY);
        for (EntityType<?> entityType : Registries.ENTITY_TYPE) {
            String identifier = Registries.ENTITY_TYPE.getId(entityType).toString();
            if (identifier.equals(NpcEntity.ENTITY_ID)) continue;
            loadedEntityTypes.put(identifier, entityType);
        }
        if (data == null) return;
        trackedEntities = data.trackedEntities;
    }

    public List<String> getLoadedEntityTypes() {
        return loadedEntityTypes.keySet().stream().sorted().toList();
    }

    public EntityType<?> getEntityTypeFromId(String id) {
        return loadedEntityTypes.get(id);
    }

    public void trackEntity(ServerWorld serverWorld, Entity entity) {
        String worldHash = worldIdentifier(serverWorld);
        trackedEntities.computeIfAbsent(worldHash, k -> new ArrayList<>()).add(entity.getUuid());
    }

    public boolean isMobEntity(World world, String sEntityType) {
        EntityType<?> entityType = loadedEntityTypes.get(sEntityType);
        Entity entity = entityType.create(world);
        return entity instanceof MobEntity;
    }

    public boolean isPathAwareEntity(World world, String sEntityType) {
        EntityType<?> entityType = loadedEntityTypes.get(sEntityType);
        Entity entity = entityType.create(world);
        return entity instanceof PathAwareEntity;
    }

    private static final class SingletonHolder {
        public static final NpcsManager INSTANCE = new NpcsManager();
    }

}
