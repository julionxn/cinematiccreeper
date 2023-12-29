package me.julionxn.cinematiccreeper.managers.presets;

import com.google.gson.annotations.Expose;

import java.io.Serializable;

public class Preset implements Serializable {

    @Expose
    private final String entityType;
    @Expose
    private final String id;
    @Expose
    private final PresetOptions options;

    public Preset(String entityType, String id, PresetOptions presetOptions){
        this.entityType = entityType;
        this.id = id;
        options = presetOptions;
    }

    public Preset(String entityType, String id){
        this(entityType, id, new PresetOptions()
                .setDisplayName(id));
    }

    public String getId(){
        return id;
    }

    public String getEntityType(){
        return entityType;
    }

    public PresetOptions getOptions(){
        return options;
    }

}
