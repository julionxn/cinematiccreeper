package me.julionxn.cinematiccreeper.managers.presets;

import com.google.gson.annotations.Expose;
import me.julionxn.cinematiccreeper.entity.NpcEntity;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
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

public class PresetOptions implements Serializable {

    @Expose private String skinUrl = ""; //player
    @Expose private boolean showDisplayName; //basic
    @Expose private String displayName = ""; //basic
    @Expose private boolean sneaking; //basic
    @Expose private String holdingItem = ""; //mob

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

    public boolean isSneaking() {
        return sneaking;
    }

    public PresetOptions setSneaking(boolean sneaking) {
        this.sneaking = sneaking;
        return this;
    }

    public String getHoldingItem() {
        return holdingItem;
    }

    public PresetOptions setHoldingItem(String holdingItem) {
        this.holdingItem = holdingItem;
        return this;
    }

    public static void addToBuf(PacketByteBuf buf, PresetOptions presetOptions){
        buf.writeString(presetOptions.skinUrl);
        buf.writeBoolean(presetOptions.showDisplayName);
        buf.writeString(presetOptions.displayName);
        buf.writeBoolean(presetOptions.sneaking);
        buf.writeString(presetOptions.holdingItem);
    }

    public static PresetOptions fromBuf(PacketByteBuf buf){
        return new PresetOptions()
                .setSkinUrl(buf.readString())
                .setShowDisplayName(buf.readBoolean())
                .setDisplayName(buf.readString())
                .setSneaking(buf.readBoolean())
                .setHoldingItem(buf.readString());
    }

    public static PresetOptions fromEntity(Entity entity){
        PresetOptions presetOptions = new PresetOptions();
        presetOptions.displayName = entity.getDisplayName().getString();
        presetOptions.showDisplayName = entity.isCustomNameVisible();
        presetOptions.sneaking = entity.isSneaking();
        if (entity instanceof MobEntity mobEntity){
            presetOptions.holdingItem = Registries.ITEM.getId(mobEntity.getMainHandStack().getItem()).toString();
        }
        if (entity instanceof PathAwareEntity){

        }
        if (entity instanceof NpcEntity){
            presetOptions.skinUrl = ((NpcEntity) entity).getSkinUrl();
        }
        return presetOptions;
    }

    public static void applyPresetOptions(Entity entity, PresetOptions presetOptions){
        entity.setCustomNameVisible(presetOptions.showDisplayName);
        entity.setCustomName(Text.of(presetOptions.displayName));
        entity.setSneaking(presetOptions.sneaking);
        if (entity instanceof MobEntity mobEntity){
            setStackInHand(mobEntity, presetOptions.holdingItem);
        }
        if (entity instanceof PathAwareEntity){

        }
    }

    private static void setStackInHand(MobEntity mobEntity, String id){
        Identifier identifier = Identifier.tryParse(id);
        if (identifier == null) return;
        Item item = Registries.ITEM.get(identifier);
        mobEntity.setStackInHand(Hand.MAIN_HAND, new ItemStack(item));
    }

}
