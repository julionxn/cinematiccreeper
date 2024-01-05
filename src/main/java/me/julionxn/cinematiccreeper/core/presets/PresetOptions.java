package me.julionxn.cinematiccreeper.core.presets;

import com.google.gson.annotations.Expose;

import java.io.Serializable;

public class PresetOptions implements Serializable {

    @Expose
    String skinUrl = ""; //player
    @Expose
    boolean showDisplayName; //basic
    @Expose
    String displayName = ""; //basic
    @Expose
    boolean sneaking; //basic
    @Expose
    String holdingItem = ""; //mob

    public String getDisplayName() {
        return displayName;
    }

    public PresetOptions setDisplayName(String displayName) {
        this.displayName = displayName;
        return this;
    }

    public String getSkinUrl() {
        return skinUrl;
    }

    public PresetOptions setSkinUrl(String skinUrl) {
        this.skinUrl = skinUrl;
        return this;
    }

    public void setShowDisplayName(boolean showDisplayName) {
        this.showDisplayName = showDisplayName;
    }

    public boolean showDisplayName() {
        return showDisplayName;
    }

    public boolean isSneaking() {
        return sneaking;
    }

    public void setSneaking(boolean sneaking) {
        this.sneaking = sneaking;
    }

    public String getHoldingItem() {
        return holdingItem;
    }

    public void setHoldingItem(String holdingItem) {
        this.holdingItem = holdingItem;
    }

    @Override
    public String toString() {
        return "PresetOptions{" +
                "skinUrl='" + skinUrl + '\'' +
                ", showDisplayName=" + showDisplayName +
                ", displayName='" + displayName + '\'' +
                ", sneaking=" + sneaking +
                ", holdingItem='" + holdingItem + '\'' +
                '}';
    }

}
