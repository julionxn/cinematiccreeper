package me.julionxn.cinematiccreeper.managers;

import com.google.gson.annotations.Expose;
import me.julionxn.cinematiccreeper.managers.presets.Preset;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

import java.util.*;

@Environment(EnvType.CLIENT)
public class PresetsManager extends SerializableJsonManager<PresetsManager> {

    private final Map<String, Integer> presetsIndex = new HashMap<>();
    @Expose
    private List<Preset> presets = new ArrayList<>();

    private PresetsManager() {
        super("cc_presets.json", PresetsManager.class);
    }

    public static PresetsManager getInstance() {
        return SingletonHolder.INSTANCE;
    }

    @Override
    protected void onLoad(PresetsManager data) {
        if (data == null) return;
        presets = data.presets;
        for (int i = 0; i < presets.size(); i++) {
            presetsIndex.put(presets.get(i).getId(), i);
        }
    }

    public void addPreset(Preset preset) {
        presets.add(preset);
    }

    public List<Preset> getPresets() {
        return presets;
    }

    public Optional<Preset> getPresetWithId(String id) {
        if (!presetsIndex.containsKey(id)) return Optional.empty();
        return Optional.of(presets.get(presetsIndex.get(id)));
    }

    private static final class SingletonHolder {
        public static final PresetsManager INSTANCE = new PresetsManager();
    }

}
