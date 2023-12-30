package me.julionxn.cinematiccreeper.managers.presets;

import com.google.gson.annotations.Expose;
import me.julionxn.cinematiccreeper.entity.NpcEntity;
import net.minecraft.entity.Entity;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.entity.mob.PathAwareEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.registry.Registries;
import net.minecraft.text.Text;
import net.minecraft.util.Hand;
import net.minecraft.util.Identifier;

import java.io.Serializable;
import java.lang.reflect.Field;

public class PresetOptions implements Serializable {

    @Expose String skinUrl = ""; //player
    @Expose boolean showDisplayName; //basic
    @Expose String displayName = ""; //basic
    @Expose boolean sneaking; //basic
    @Expose String holdingItem = ""; //mob

    public PresetOptions setDisplayName(String displayName) {
        this.displayName = displayName;
        return this;
    }

    public String getDisplayName() {
        return displayName;
    }

    public PresetOptions setSkinUrl(String skinUrl) {
        this.skinUrl = skinUrl;
        return this;
    }

    public String getSkinUrl() {
        return skinUrl;
    }

    public void setShowDisplayName(boolean showDisplayName) {
        this.showDisplayName = showDisplayName;
    }

    public boolean showDisplayName() {
        return showDisplayName;
    }

    public void setSneaking(boolean sneaking) {
        this.sneaking = sneaking;
    }

    public boolean isSneaking() {
        return sneaking;
    }

    public void setHoldingItem(String holdingItem) {
        this.holdingItem = holdingItem;
    }

    public String getHoldingItem() {
        return holdingItem;
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
