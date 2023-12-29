package me.julionxn.cinematiccreeper.managers;

import com.google.gson.annotations.Expose;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

import java.util.ArrayList;
import java.util.List;

@Environment(EnvType.CLIENT)
public class PresetsManager extends SerializableJsonManager<PresetsManager> {

    @Expose
    private List<Preset> presets = new ArrayList<>();

    private <K extends Class<PresetsManager>> PresetsManager(String path, K clazz) {
        super(path, clazz);
    }

    private static final class SingletonHolder {
        public static final PresetsManager INSTANCE = new PresetsManager("cc_presets.json", PresetsManager.class);
    }

    public static PresetsManager getInstance() {
        return SingletonHolder.INSTANCE;
    }

    @Override
    protected void onLoad(PresetsManager data) {
        if (data == null) return;
        presets = data.presets;
    }

    public void addPreset(Preset preset) {
        presets.add(preset);
    }

    public List<Preset> getPresets() {
        return presets;
    }


}
