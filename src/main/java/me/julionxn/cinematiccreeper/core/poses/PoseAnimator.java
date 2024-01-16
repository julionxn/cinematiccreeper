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
            applyTransformations(model.body, model.jacket, Interpolation.LINEAR, 0, currentPose.torso, currentPose.torso, 1f);
            applyTransformations(model.head, model.hat, Interpolation.LINEAR, 0, currentPose.head, currentPose.head, 0.666666f);
            applyTransformations(model.leftArm, model.leftSleeve, Interpolation.LINEAR, 0, currentPose.leftArm, currentPose.leftArm, 1);
            applyTransformations(model.rightArm, model.rightSleeve, Interpolation.LINEAR, 0, currentPose.rightArm, currentPose.rightArm, 1);
            applyTransformations(model.leftLeg, model.leftPants, Interpolation.LINEAR, 0, currentPose.leftLeg, currentPose.leftLeg, 1);
            applyTransformations(model.rightLeg, model.rightPants, Interpolation.LINEAR, 0, currentPose.rightLeg, currentPose.rightLeg, 1);
            return;
        }
        float t = (tick - currentPoseTick) / (nextPoseTick - currentPoseTick);
        t = MathHelper.clamp(t, 0, 1);
        PosePoint nextPose = npcPose.getPoseOfTick(nextPoseTick);
        Interpolation interpolation = nextPose.interpolation;
        applyTransformations(model.body, model.jacket, interpolation, t, currentPose.torso, nextPose.torso, 1);
        applyTransformations(model.head, model.hat, interpolation, t, currentPose.head, nextPose.head,  0.6666666f);
        applyTransformations(model.leftArm, model.leftSleeve, interpolation, t, currentPose.leftArm, nextPose.leftArm, 1);
        applyTransformations(model.rightArm, model.rightSleeve, interpolation, t, currentPose.rightArm, nextPose.rightArm, 1);
        applyTransformations(model.leftLeg, model.leftPants, interpolation, t, currentPose.leftLeg, nextPose.leftLeg, 1);
        applyTransformations(model.rightLeg, model.rightPants, interpolation, t, currentPose.rightLeg, nextPose.rightLeg, 1);
    }

    private void applyTransformations(ModelPart part, ModelPart part2, Interpolation interpolation, float t, PoseData current, PoseData next, float headCorrection){
        float yaw = interpolation.interpolate(t, current.yaw, next.yaw);
        float pitch = interpolation.interpolate(t, current.pitch, next.pitch);
        float roll = interpolation.interpolate(t, current.roll, next.roll);
        part.setAngles(pitch, yaw, roll);
        part2.setAngles(pitch, yaw, roll);
        float tX = interpolation.interpolate(t, current.translationX, next.translationX);
        float tY = interpolation.interpolate(t, current.translationY, next.translationY);
        float tZ = interpolation.interpolate(t, current.translationZ, next.translationZ);
        part.setPivot(tX, tY, tZ);
        part2.setPivot(tX, tY, tZ);
        float sX = interpolation.interpolate(t, current.scaleX, next.scaleX) * headCorrection;
        float sY = interpolation.interpolate(t, current.scaleY, next.scaleY) * headCorrection;
        float sZ = interpolation.interpolate(t, current.scaleZ, next.scaleZ) * headCorrection;
        setScale(part, sX, sY, sZ);
        setScale(part2, sX, sY, sZ);
    }
    private void setScale(ModelPart modelPart, float x, float y, float z){
        modelPart.xScale = x;
        modelPart.yScale = y;
        modelPart.zScale = z;
    }

    public void render(MatrixStack matrixStack, VertexConsumer vertexConsumer, int light){
        model.render(matrixStack, vertexConsumer, light, OverlayTexture.DEFAULT_UV, 1f, 1f, 1f, 1f);
    }

    public int getTick(){
        return (int) tick;
    }

}
