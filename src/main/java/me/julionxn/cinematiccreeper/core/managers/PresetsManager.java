package me.julionxn.cinematiccreeper.core.managers;

import com.google.gson.annotations.Expose;
import me.julionxn.cinematiccreeper.core.presets.Preset;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Environment(EnvType.CLIENT)
public class PresetsManager extends SerializableJsonManager<PresetsManager> {

    private static final float VERSION = 1.1f;
    @Expose
    private List<Preset> presets = new ArrayList<>();

    private PresetsManager() {
        super("cc_presets.json", VERSION, PresetsManager.class);
    }

    public static PresetsManager getInstance() {
        return SingletonHolder.INSTANCE;
    }

    @Override
    protected PresetsManager getCurrentInstance() {
        return this;
    }

    @Override
    protected void afterLoad() {

    }

    public void addPreset(Preset preset) {
        presets.add(preset);
    }

    public List<Preset> getPresets() {
        return presets;
    }

    public Optional<Preset> getPresetWithId(String id) {
        return presets.stream().filter(preset -> preset.getId().equals(id)).findFirst();
    }

    public void removePresetWithId(String id) {
        presets.removeIf(preset -> preset.getId().equals(id));
    }

    private static final class SingletonHolder {
        public static final PresetsManager INSTANCE = new PresetsManager();
    }

}
