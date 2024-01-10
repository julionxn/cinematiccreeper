package me.julionxn.cinematiccreeper.core.managers;

import com.google.gson.annotations.Expose;
import me.julionxn.cinematiccreeper.core.poses.NpcPose;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

import java.util.HashMap;
import java.util.Optional;

@Environment(EnvType.CLIENT)
public class NpcPosesManager extends SerializableJsonManager<NpcPosesManager> {

    private static final float VERSION = 1.1f;
    @Expose
    private final HashMap<String, NpcPose> loadedPoses = new HashMap<>();

    protected NpcPosesManager() {
        super("cc_poses.json", VERSION, NpcPosesManager.class);
    }

    private static class SingletonHolder {
        private static final NpcPosesManager INSTANCE = new NpcPosesManager();
    }

    public static NpcPosesManager getInstance(){
        return SingletonHolder.INSTANCE;
    }

    public Optional<NpcPose> getNpcPose(String id){
        return Optional.ofNullable(loadedPoses.get(id));
    }

    public void addNpcPose(String id, NpcPose pose){
        loadedPoses.put(id, pose);
    }

    public HashMap<String, NpcPose> getLoadedPoses(){
        return loadedPoses;
    }

    public void removeNpcPose(String id){
        loadedPoses.remove(id);
    }

    @Override
    protected NpcPosesManager getCurrentInstance() {
        return this;
    }

    @Override
    protected void afterLoad() {

    }

}
