package me.julionxn.cinematiccreeper.presets;

public class Preset {

    private final String entityType;
    private final String id;
    private String skin;

    public Preset(String entityType, String id, String skin){
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

    @Override
    public String toString() {
        return "Preset{" +
                "entityType=" + entityType +
                ", id='" + id + '\'' +
                ", skin='" + skin + '\'' +
                '}';
    }
}
