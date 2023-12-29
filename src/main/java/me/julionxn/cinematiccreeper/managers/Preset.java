package me.julionxn.cinematiccreeper.managers;

import com.google.gson.annotations.Expose;
import org.jetbrains.annotations.Nullable;

import java.io.Serializable;

public class Preset implements Serializable {

    @Expose
    private final String entityType;
    @Expose
    private final String id;
    @Nullable
    @Expose
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
