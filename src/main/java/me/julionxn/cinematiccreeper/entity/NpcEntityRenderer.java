package me.julionxn.cinematiccreeper.entity;

import me.julionxn.cinematiccreeper.poses.NpcPose;
import me.julionxn.cinematiccreeper.poses.PoseData;
import me.julionxn.cinematiccreeper.poses.PosePoint;
import net.minecraft.client.model.ModelPart;
import net.minecraft.client.render.OverlayTexture;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.render.entity.MobEntityRenderer;
import net.minecraft.client.render.entity.model.EntityModelLayers;
import net.minecraft.client.render.entity.model.PlayerEntityModel;
import net.minecraft.client.util.DefaultSkinHelper;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.RotationAxis;
import org.joml.Quaternionf;

import java.util.HashMap;

public class NpcEntityRenderer extends MobEntityRenderer<NpcEntity, PlayerEntityModel<NpcEntity>> {

    private final EntityRendererFactory.Context ctx;
    private static final HashMap<Integer, PlayerEntityModel<NpcEntity>> models = new HashMap<>();

    public NpcEntityRenderer(EntityRendererFactory.Context ctx) {
        super(ctx, new PlayerEntityModel<>(ctx.getPart(EntityModelLayers.PLAYER), false), 0.5f);
        this.ctx = ctx;
    }

    @Override
    public void render(NpcEntity livingEntity, float f, float g, MatrixStack matrixStack, VertexConsumerProvider vertexConsumerProvider, int i) {
        NpcPose npcPose = livingEntity.getNpcPose();
        if (npcPose != null){
            models.computeIfAbsent(livingEntity.getId(), id -> new PlayerEntityModel<>(ctx.getPart(EntityModelLayers.PLAYER), false));
            hijackRendering(f, npcPose, livingEntity, matrixStack, vertexConsumerProvider, i);
            return;
        }
        setModelPose(livingEntity);
        super.render(livingEntity, f, g, matrixStack, vertexConsumerProvider, i);
    }

    private void setModelPose(NpcEntity player) {
        PlayerEntityModel<NpcEntity> playerEntityModel = this.getModel();
        playerEntityModel.setVisible(true);
        playerEntityModel.sneaking = player.isSneaking();
    }

    private void hijackRendering(float yaw, NpcPose npcPose, NpcEntity livingEntity, MatrixStack matrixStack, VertexConsumerProvider vertexConsumerProvider, int i){
        PlayerEntityModel<NpcEntity> currentModel = models.get(livingEntity.getId());
        PosePoint posePoint = npcPose.getPoseOfTick(0);
        matrixStack.push();
        matrixStack.multiply(RotationAxis.POSITIVE_Z.rotationDegrees(180));
        matrixStack.multiply(RotationAxis.POSITIVE_Y.rotationDegrees(180 + yaw));
        matrixStack.translate(0, -1.5, 0);
        VertexConsumer vertexConsumer = vertexConsumerProvider.getBuffer(getRenderLayer(livingEntity, true, false, true));
        renderPart(matrixStack, vertexConsumer, currentModel.head, posePoint.head, i);
        renderLimb(matrixStack, vertexConsumer, currentModel.leftArm, currentModel.leftSleeve, posePoint.leftArm, i);
        renderLimb(matrixStack, vertexConsumer, currentModel.rightArm, currentModel.rightSleeve, posePoint.rightArm, i);
        renderLimb(matrixStack, vertexConsumer, currentModel.leftLeg, currentModel.leftPants, posePoint.leftLeg, i);
        renderLimb(matrixStack, vertexConsumer, currentModel.rightLeg, currentModel.rightPants, posePoint.rightLeg, i);
        currentModel.body.render(matrixStack, vertexConsumer, i, OverlayTexture.DEFAULT_UV);
        matrixStack.pop();
    }

    private void renderPart(MatrixStack matrixStack, VertexConsumer vertexConsumer, ModelPart modelPart, PoseData data, int light){
        matrixStack.push();
        matrixStack.multiply(new Quaternionf().rotationZYX(-data.roll, -data.yaw, data.pitch));
        modelPart.render(matrixStack, vertexConsumer, light, OverlayTexture.DEFAULT_UV);
        matrixStack.pop();
    }

    private void renderLimb(MatrixStack matrixStack, VertexConsumer vertexConsumer, ModelPart modelPart, ModelPart modelPart2, PoseData data, int light){
        matrixStack.push();
        matrixStack.translate(modelPart.pivotX / 18f, modelPart.pivotY / 18.0f, modelPart.pivotZ / 18.0f);
        matrixStack.multiply(new Quaternionf().rotationZYX(data.roll, data.yaw, data.pitch));
        matrixStack.translate(-modelPart.pivotX / 18f, -modelPart.pivotY / 18.0f, -modelPart.pivotZ / 18.0f);
        modelPart.render(matrixStack, vertexConsumer, light, OverlayTexture.DEFAULT_UV);
        modelPart2.render(matrixStack, vertexConsumer, light, OverlayTexture.DEFAULT_UV);
        matrixStack.pop();
    }

    @Override
    protected void renderLabelIfPresent(NpcEntity entity, Text text, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light) {
        if (!entity.shouldRenderName()) return;
        super.renderLabelIfPresent(entity, text, matrices, vertexConsumers, light);
    }

    @Override
    public Identifier getTexture(NpcEntity entity) {
        Identifier texture = entity.getSkin();
        if (texture == null) return DefaultSkinHelper.getTexture();
        return texture;
    }

}
