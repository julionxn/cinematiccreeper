package me.julionxn.cinematiccreeper.managers.paths;

import com.google.gson.annotations.Expose;
import net.minecraft.network.PacketByteBuf;

import java.util.ArrayList;
import java.util.List;

public class Path {

    private final String id;
    private final Type type;
    private final int entityId;
    private final List<PathAction> actions = new ArrayList<>();

    public Path(String id, Type type, int entityId) {
        this.id = id;
        this.type = type;
        this.entityId = entityId;
    }

    public String getId(){
        return id;
    }

    public Type getType(){
        return type;
    }

    public int getEntityId(){
        return entityId;
    }

    public void addAction(PathAction pathAction){
        actions.add(pathAction);
    }

    public void popAction(){
        actions.remove(actions.size() - 1);
    }

    public List<PathAction> getActions(){
        return actions;
    }

    public static void addToBuf(PacketByteBuf buf, Path path){
        buf.writeString(path.id);
        buf.writeEnumConstant(path.type);
        buf.writeInt(path.entityId);
        buf.writeInt(path.actions.size());
        for (PathAction action : path.actions) {
            PathAction.addToBuf(buf, action);
        }
    }

    public static Path fromBuf(PacketByteBuf buf){
        String id = buf.readString();
        Type type = buf.readEnumConstant(Type.class);
        int entityId = buf.readInt();
        Path path = new Path(id, type, entityId);
        int size = buf.readInt();
        for (int i = 0; i < size; i++) {
            PathAction pathAction = PathAction.fromBuf(buf);
            path.actions.add(pathAction);
        }
        return path;
    }

    public enum Type {
        PING_PONG, LOOP
    }

}
