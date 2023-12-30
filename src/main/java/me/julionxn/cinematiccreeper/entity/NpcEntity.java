package me.julionxn.cinematiccreeper.entity;

import me.julionxn.cinematiccreeper.managers.presets.PresetOptions;
import me.julionxn.cinematiccreeper.managers.skins.NpcSkinManager;
import net.minecraft.client.util.DefaultSkinHelper;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.attribute.DefaultAttributeContainer;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.data.DataTracker;
import net.minecraft.entity.data.TrackedData;
import net.minecraft.entity.data.TrackedDataHandlerRegistry;
import net.minecraft.entity.mob.PathAwareEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class NpcEntity extends PathAwareEntity {

    public static final String ENTITY_ID = "cinematiccreeper:npc_entity";

    private static final TrackedData<String> SKIN_URL = DataTracker.registerData(NpcEntity.class, TrackedDataHandlerRegistry.STRING);
    private Identifier skin = DefaultSkinHelper.getTexture();
    private PresetOptions presetOptions;

    public NpcEntity(EntityType<? extends PathAwareEntity> entityType, World world) {
        super(entityType, world);
    }

    public static DefaultAttributeContainer.Builder createPlayerAttributes() {
        return PlayerEntity.createPlayerAttributes()
                .add(EntityAttributes.GENERIC_MOVEMENT_SPEED, 0.3111D)
                .add(EntityAttributes.GENERIC_FOLLOW_RANGE, 48.0);
    }

    @Override
    protected void initDataTracker() {
        super.initDataTracker();
        dataTracker.startTracking(SKIN_URL, "");
    }

    @Override
    public void readCustomDataFromNbt(NbtCompound nbt) {
        super.readCustomDataFromNbt(nbt);
        dataTracker.set(SKIN_URL, nbt.getString("SkinUrl"));
        setFlag(1, nbt.getBoolean("Sneaking"));
    }

    @Override
    public void writeCustomDataToNbt(NbtCompound nbt) {
        super.writeCustomDataToNbt(nbt);
        nbt.putString("SkinUrl", dataTracker.get(SKIN_URL));
        nbt.putBoolean("Sneaking", getFlag(1));
    }

    @Override
    public void tickMovement() {
        super.tickMovement();
    }

    @Override
    protected ActionResult interactMob(PlayerEntity player, Hand hand) {
        if (hand == Hand.OFF_HAND) return ActionResult.PASS;
        Vec3d target = player.getPos();
        float mult = isSneaking() ? 0.5f : 1.0f;
        getNavigation().startMovingTo(target.x, target.y, target.z, 1.0D * mult);
        return super.interactMob(player, hand);
    }

    @Override
    public void onDataTrackerUpdate(List<DataTracker.SerializedEntry<?>> dataEntries) {
        dataEntries.forEach(entry -> {
            if (entry.id() == SKIN_URL.getId()) {
                NpcSkinManager.getInstance().updateSkinOf(this);
            }
        });
        super.onDataTrackerUpdate(dataEntries);
    }

    public String getSkinUrl() {
        return dataTracker.get(SKIN_URL);
    }

    public void setSkinUrl(String skinUrl) {
        dataTracker.set(SKIN_URL, skinUrl);
    }

    public Identifier getSkin() {
        return skin;
    }

    public void setSkin(Identifier identifier) {
        skin = identifier;
    }

    @Nullable
    public PresetOptions getPresetOptions() {
        return presetOptions;
    }

    public void setPresetOptions(PresetOptions presetOptions) {
        this.presetOptions = presetOptions;
    }

}
