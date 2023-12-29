package me.julionxn.cinematiccreeper.managers.presets;

import com.google.gson.annotations.Expose;

import java.io.Serializable;

public class PresetOptions implements Serializable {

    @Expose private String skinUrl;
    @Expose private boolean showDisplayName;
    @Expose private String displayName;

    public PresetOptions(){

    }

    public String getSkinUrl() {
        return skinUrl;
    }

    public PresetOptions setSkinUrl(String skinUrl) {
        this.skinUrl = skinUrl;
        return this;
    }

    public boolean showDisplayName() {
        return showDisplayName;
    }

    public PresetOptions setShowDisplayName(boolean showDisplayName) {
        this.showDisplayName = showDisplayName;
        return this;
    }

    public String getDisplayName() {
        return displayName;
    }

    public PresetOptions setDisplayName(String displayName) {
        this.displayName = displayName;
        return this;
    }

}
