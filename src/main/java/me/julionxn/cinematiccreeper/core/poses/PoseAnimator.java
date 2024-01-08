package me.julionxn.cinematiccreeper.core.poses;

import me.julionxn.cinematiccreeper.core.Interpolation;
import me.julionxn.cinematiccreeper.entity.NpcEntity;
import me.julionxn.cinematiccreeper.util.MathHelper;
import net.minecraft.client.model.ModelPart;
import net.minecraft.client.render.OverlayTexture;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.entity.model.PlayerEntityModel;
import net.minecraft.client.util.math.MatrixStack;
import org.jetbrains.annotations.Nullable;

public class PoseAnimator {

    private final PlayerEntityModel<NpcEntity> model;
    float tick = 0;
    boolean playing;

    public PoseAnimator(PlayerEntityModel<NpcEntity> model) {
        this.model = model;
    }

    public void play(){
        playing = true;
    }

    public void stop(){
        playing = false;
        reset();
    }

    public void reset(){
        tick = 0;
    }

    public void delta(NpcPose npcPose, float tickDelta){
        if (!playing) return;
        tick += tickDelta;
        tick %= npcPose.getLength();
        applyAnimation(npcPose);
    }

    private void applyAnimation(NpcPose npcPose){
        Integer currentPoseTick = npcPose.currentPoseOfTick((int) tick);
        Integer nextPoseTick = npcPose.nextPoseOfTick((int) tick);
        if (currentPoseTick == null) return;
        PosePoint currentPose = npcPose.getPoseOfTick(currentPoseTick);
        if (nextPoseTick == null) {
            applyTransformations(model.head, null, Interpolation.LINEAR, 0, currentPose.head, currentPose.head);
            applyTransformations(model.leftArm, model.leftSleeve, Interpolation.LINEAR, 0, currentPose.leftArm, currentPose.leftArm);
            applyTransformations(model.rightArm, model.rightSleeve, Interpolation.LINEAR, 0, currentPose.rightArm, currentPose.rightArm);
            applyTransformations(model.leftLeg, model.leftPants, Interpolation.LINEAR, 0, currentPose.leftLeg, currentPose.leftLeg);
            applyTransformations(model.rightLeg, model.rightPants, Interpolation.LINEAR, 0, currentPose.rightLeg, currentPose.rightLeg);
            return;
        }
        float t = (tick - currentPoseTick) / (nextPoseTick - currentPoseTick);
        t = MathHelper.clamp(t, 0, 1);
        PosePoint nextPose = npcPose.getPoseOfTick(nextPoseTick);
        Interpolation interpolation = nextPose.interpolation;
        applyTransformations(model.head, model.hat, interpolation, t, currentPose.head, nextPose.head);
        applyTransformations(model.leftArm, model.leftSleeve, interpolation, t, currentPose.leftArm, nextPose.leftArm);
        applyTransformations(model.rightArm, model.rightSleeve, interpolation, t, currentPose.rightArm, nextPose.rightArm);
        applyTransformations(model.leftLeg, model.leftPants, interpolation, t, currentPose.leftLeg, nextPose.leftLeg);
        applyTransformations(model.rightLeg, model.rightPants, interpolation, t, currentPose.rightLeg, nextPose.rightLeg);
    }

    private void applyTransformations(ModelPart part, @Nullable ModelPart part2, Interpolation interpolation, float t, PoseData current, PoseData next){
        float yaw = interpolation.interpolate(t, current.yaw, next.yaw);
        float pitch = interpolation.interpolate(t, current.pitch, next.pitch);
        float roll = interpolation.interpolate(t, current.roll, next.roll);
        part.setAngles(pitch, yaw, roll);
        if (part2 != null) part2.setAngles(pitch, yaw, roll);
    }

    public void render(MatrixStack matrixStack, VertexConsumer vertexConsumer, int light){
        model.render(matrixStack, vertexConsumer, light, OverlayTexture.DEFAULT_UV, 1f, 1f, 1f, 1f);
    }

    public int getTick(){
        return (int) tick;
    }

}
