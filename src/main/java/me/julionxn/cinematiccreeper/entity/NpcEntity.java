package me.julionxn.cinematiccreeper.entity;

import me.julionxn.cinematiccreeper.util.SkinHelper;
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

import java.util.List;

public class NpcEntity extends PathAwareEntity {

    public static final String ENTITY_ID = "cinematiccreeper:npc_entity";

    private static final TrackedData<String> SKIN_URL = DataTracker.registerData(NpcEntity.class, TrackedDataHandlerRegistry.STRING);
    private static final TrackedData<String> NPC_ID = DataTracker.registerData(NpcEntity.class, TrackedDataHandlerRegistry.STRING);
    private Identifier skin = DefaultSkinHelper.getTexture();

    public NpcEntity(EntityType<? extends PathAwareEntity> entityType, World world) {
        super(entityType, world);
    }

    @Override
    protected void initDataTracker() {
        super.initDataTracker();
        dataTracker.startTracking(SKIN_URL, "");
        dataTracker.startTracking(NPC_ID, "npc");
    }

    @Override
    public void readCustomDataFromNbt(NbtCompound nbt) {
        super.readCustomDataFromNbt(nbt);
        dataTracker.set(SKIN_URL, nbt.getString("SkinUrl"));
    }

    @Override
    public void writeCustomDataToNbt(NbtCompound nbt) {
        super.writeCustomDataToNbt(nbt);
        nbt.putString("SkinUrl", dataTracker.get(SKIN_URL));
    }

    public static DefaultAttributeContainer.Builder createPlayerAttributes() {
        return PlayerEntity.createPlayerAttributes()
                .add(EntityAttributes.GENERIC_MOVEMENT_SPEED, 0.3111D)
                .add(EntityAttributes.GENERIC_FOLLOW_RANGE, 48.0);
    }

    @Override
    protected ActionResult interactMob(PlayerEntity player, Hand hand) {
        if (hand == Hand.OFF_HAND) return ActionResult.PASS;
        Vec3d target = player.getPos();
        getNavigation().startMovingTo(target.x, target.y, target.z, 1.0D);
        return super.interactMob(player, hand);
    }

    @Override
    public void onDataTrackerUpdate(List<DataTracker.SerializedEntry<?>> dataEntries) {
        dataEntries.forEach(entry -> {
            if (entry.id() == SKIN_URL.getId()){
                SkinHelper.updateSkinOf(getWorld(), this);
            }
        });
        super.onDataTrackerUpdate(dataEntries);
    }

    public void setNpcId(String id){
        dataTracker.set(NPC_ID, id);
    }

    public String getNpcId(){
        return dataTracker.get(NPC_ID);
    }

    public void setSkinUrl(String skinUrl){
        dataTracker.set(SKIN_URL, skinUrl);
    }

    public String getSkinUrl(){
        return dataTracker.get(SKIN_URL);
    }

    public void setSkin(Identifier identifier){
        skin = identifier;
    }

    public Identifier getSkin(){
        return skin;
    }

}
