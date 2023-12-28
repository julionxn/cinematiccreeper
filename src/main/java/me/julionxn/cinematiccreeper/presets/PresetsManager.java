package me.julionxn.cinematiccreeper.presets;

import me.julionxn.cinematiccreeper.entity.AllEntities;
import net.minecraft.entity.EntityType;
import net.minecraft.registry.Registries;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PresetsManager {


    private final Map<String, EntityType<?>> loadedEntityTypes = new HashMap<>();
    private final List<Preset> presets = new ArrayList<>();

    private PresetsManager(){

    }

    private static final class SingletonHolder {
        public static final PresetsManager INSTANCE = new PresetsManager();
    }

    public static PresetsManager getInstance(){
        return SingletonHolder.INSTANCE;
    }

    public void load(){
        loadedEntityTypes.put("cinematiccreeper:npc_entity", AllEntities.TEST_ENTITY);
        for (EntityType<?> entityType : Registries.ENTITY_TYPE){
            String identifier = Registries.ENTITY_TYPE.getId(entityType).toString();
            if (identifier.equals("cinematiccreeper:npc_entity")) continue;
            loadedEntityTypes.put(identifier, entityType);
        }
    }

    public List<String> getLoadedEntityTypes(){
        return loadedEntityTypes.keySet().stream().sorted().toList();
    }

    public EntityType<?> getEntityTypeFromId(String id){
        return loadedEntityTypes.get(id);
    }

    public void addPreset(Preset preset){
        presets.add(preset);
        System.out.println(preset.toString());
    }

    public List<Preset> getPresets(){
        return presets;
    }

}
