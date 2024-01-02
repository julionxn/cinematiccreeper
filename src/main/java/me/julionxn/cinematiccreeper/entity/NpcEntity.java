package me.julionxn.cinematiccreeper.entity;

import me.julionxn.cinematiccreeper.managers.presets.PresetOptions;
import me.julionxn.cinematiccreeper.managers.skins.NpcSkinManager;
import net.minecraft.client.util.DefaultSkinHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.attribute.DefaultAttributeContainer;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.data.DataTracker;
import net.minecraft.entity.data.TrackedData;
import net.minecraft.entity.data.TrackedDataHandlerRegistry;
import net.minecraft.entity.mob.PathAwareEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.listener.ClientPlayPacketListener;
import net.minecraft.network.packet.Packet;
import net.minecraft.network.packet.s2c.play.EntitySpawnS2CPacket;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class NpcEntity extends PathAwareEntity {

    public static final String ENTITY_ID = "cinematiccreeper:npc_entity";

    private static final TrackedData<String> SKIN_URL = DataTracker.registerData(NpcEntity.class, TrackedDataHandlerRegistry.STRING);
    private Identifier skin = DefaultSkinHelper.getTexture();

    public NpcEntity(EntityType<? extends PathAwareEntity> entityType, World world) {
        super(entityType, world);
    }

    public static DefaultAttributeContainer.Builder createPlayerAttributes() {
        return PlayerEntity.createPlayerAttributes()
                .add(EntityAttributes.GENERIC_MOVEMENT_SPEED, 0.3111D)
                .add(EntityAttributes.GENERIC_FOLLOW_RANGE, 24.0);
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
    protected ActionResult interactMob(PlayerEntity player, Hand hand) {
        if (hand == Hand.OFF_HAND) return ActionResult.PASS;
        if (getWorld().isClient){
            lookAt(player);
        }
        return super.interactMob(player, hand);
    }

    public void lookAt(Entity targetEntity) {
        double f;
        double d = targetEntity.getX() - this.getX();
        double e = targetEntity.getZ() - this.getZ();
        if (targetEntity instanceof LivingEntity livingEntity) {
            f = livingEntity.getEyeY() - this.getEyeY();
        } else {
            f = (targetEntity.getBoundingBox().minY + targetEntity.getBoundingBox().maxY) / 2.0 - this.getEyeY();
        }
        lookRelativeTo(d, f, e);
    }

    public void lookAt(Vec3d position){
        double y = position.y - this.getEyeY();
        double x = position.x - this.getX();
        double z = position.z - this.getZ();
        lookRelativeTo(x, y, z);
    }

    private void lookRelativeTo(double x, double y, double z){
        double g = Math.sqrt(x * x + z * z);
        float yaw = (float)(MathHelper.atan2(z, x) * 57.2957763671875) - 90.0f;
        float pitch = (float)(-(MathHelper.atan2(y, g) * 57.2957763671875));
        setPitch(pitch);
        setHeadYaw(yaw);
        setYaw(yaw);
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

}
