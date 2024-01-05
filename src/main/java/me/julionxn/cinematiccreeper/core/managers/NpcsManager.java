package me.julionxn.cinematiccreeper.core.managers;

import com.google.gson.annotations.Expose;
import me.julionxn.cinematiccreeper.entity.AllEntities;
import me.julionxn.cinematiccreeper.entity.NpcEntity;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.entity.mob.PathAwareEntity;
import net.minecraft.registry.Registries;
import net.minecraft.world.World;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class NpcsManager extends SerializableJsonManager<NpcsManager> {

    private final Map<String, EntityType<?>> loadedEntityTypes = new HashMap<>();
    @Expose
    private Map<String, Boolean> cachedMobEntities = new HashMap<>();
    @Expose Map<String, Boolean> cachedPathAwareEntities = new HashMap<>();

    private NpcsManager() {
        super("cc_types.json", NpcsManager.class);
    }

    public static NpcsManager getInstance() {
        return NpcsManager.SingletonHolder.INSTANCE;
    }

    @Override
    protected NpcsManager getCurrentInstance() {
        return this;
    }

    @Override
    protected void afterLoad() {
        loadedEntityTypes.put(NpcEntity.ENTITY_ID, AllEntities.NPC_ENTITY);
        for (EntityType<?> entityType : Registries.ENTITY_TYPE) {
            String identifier = Registries.ENTITY_TYPE.getId(entityType).toString();
            if (identifier.equals(NpcEntity.ENTITY_ID)) continue;
            loadedEntityTypes.put(identifier, entityType);
        }
    }

    public List<String> getLoadedEntityTypes() {
        return loadedEntityTypes.keySet().stream().sorted().toList();
    }

    public EntityType<?> getEntityTypeFromId(String id) {
        return loadedEntityTypes.get(id);
    }

    public boolean isMobEntity(World world, String sEntityType) {
        System.out.println(loadedEntityTypes);
        if (cachedMobEntities.containsKey(sEntityType)) {
            return cachedMobEntities.get(sEntityType);
        }
        EntityType<?> entityType = loadedEntityTypes.get(sEntityType);
        Entity entity = entityType.create(world);
        boolean isMobEntity = entity instanceof MobEntity;
        cachedMobEntities.put(sEntityType, isMobEntity);
        return isMobEntity;
    }

    public boolean isPathAwareEntity(World world, String sEntityType) {
        if (cachedPathAwareEntities.containsKey(sEntityType)) {
            return cachedPathAwareEntities.get(sEntityType);
        }
        EntityType<?> entityType = loadedEntityTypes.get(sEntityType);
        Entity entity = entityType.create(world);
        boolean isPathAware = entity instanceof PathAwareEntity;
        cachedPathAwareEntities.put(sEntityType, isPathAware);
        return isPathAware;
    }

    private static final class SingletonHolder {
        public static final NpcsManager INSTANCE = new NpcsManager();
    }

}
