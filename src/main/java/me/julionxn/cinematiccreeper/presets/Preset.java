package me.julionxn.cinematiccreeper.presets;

import org.jetbrains.annotations.Nullable;

public class Preset {

    private final String entityType;
    private final String id;
    @Nullable
    private final String skin;

    public Preset(String entityType, String id, @Nullable String skin){
        this.entityType = entityType;
        this.id = id;
        this.skin = skin;
    }

    public Preset(String entityType, String id){
        this(entityType, id, null);
    }

    public String getId(){
        return id;
    }

    public String getEntityType(){
        return entityType;
    }

    public @Nullable String getSkin(){
        return skin;
    }

    @Override
    public String toString() {
        return "Preset{" +
                "entityType=" + entityType +
                ", id='" + id + '\'' +
                ", skin='" + skin + '\'' +
                '}';
    }
}
