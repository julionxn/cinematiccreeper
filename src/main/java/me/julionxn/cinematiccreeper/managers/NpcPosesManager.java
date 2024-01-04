package me.julionxn.cinematiccreeper.managers;

import com.google.gson.annotations.Expose;
import me.julionxn.cinematiccreeper.poses.NpcPose;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

import java.util.HashMap;

@Environment(EnvType.CLIENT)
public class NpcPosesManager extends SerializableJsonManager<NpcPosesManager> {

    @Expose
    private final HashMap<String, NpcPose> loadedPoses = new HashMap<>();

    protected NpcPosesManager() {
        super("cc_poses.json", NpcPosesManager.class);
    }

    private static class SingletonHolder {
        private static final NpcPosesManager INSTANCE = new NpcPosesManager();
    }

    public static NpcPosesManager getInstance(){
        return SingletonHolder.INSTANCE;
    }

    public void addNpcPose(String id, NpcPose pose){
        loadedPoses.put(id, pose);
    }

    public NpcPose getNpcPose(String id){
        return loadedPoses.get(id);
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
