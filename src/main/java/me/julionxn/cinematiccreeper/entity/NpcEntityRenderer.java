package me.julionxn.cinematiccreeper.entity;

import me.julionxn.cinematiccreeper.poses.NpcPose;
import me.julionxn.cinematiccreeper.poses.PoseTicker;
import net.minecraft.client.MinecraftClient;
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
import org.joml.Vector3f;

import java.util.HashMap;

public class NpcEntityRenderer extends MobEntityRenderer<NpcEntity, PlayerEntityModel<NpcEntity>> {

    private final EntityRendererFactory.Context ctx;
    public static final HashMap<Integer, PoseTicker> models = new HashMap<>();

    public NpcEntityRenderer(EntityRendererFactory.Context ctx) {
        super(ctx, new PlayerEntityModel<>(ctx.getPart(EntityModelLayers.PLAYER), false), 0.5f);
        this.ctx = ctx;
    }

    @Override
    public void render(NpcEntity livingEntity, float entityYaw, float delta, MatrixStack matrixStack, VertexConsumerProvider vertexConsumerProvider, int i) {
        NpcPose npcPose = livingEntity.getNpcPose();
        if (npcPose != null){
            models.computeIfAbsent(livingEntity.getId(), id -> {
                PlayerEntityModel<NpcEntity> playerEntityModel = new PlayerEntityModel<>(ctx.getPart(EntityModelLayers.PLAYER), false);
                playerEntityModel.head.scale(new Vector3f(-0.33f));
                return new PoseTicker(playerEntityModel);
            });
            hijackRendering(entityYaw, npcPose, livingEntity, matrixStack, vertexConsumerProvider, i);
            return;
        }
        setModelPose(livingEntity);
        super.render(livingEntity, entityYaw, delta, matrixStack, vertexConsumerProvider, i);
    }

    private void setModelPose(NpcEntity player) {
        PlayerEntityModel<NpcEntity> playerEntityModel = this.getModel();
        playerEntityModel.setVisible(true);
        playerEntityModel.sneaking = player.isSneaking();
    }

    private void hijackRendering(float yaw, NpcPose npcPose, NpcEntity livingEntity, MatrixStack matrixStack, VertexConsumerProvider vertexConsumerProvider, int i){
        PoseTicker ticker = models.get(livingEntity.getId());
        ticker.delta(npcPose, MinecraftClient.getInstance().getLastFrameDuration());
        matrixStack.push();
        matrixStack.scale(2, 2, 2);
        matrixStack.multiply(RotationAxis.POSITIVE_Z.rotationDegrees(180));
        matrixStack.multiply(RotationAxis.POSITIVE_Y.rotationDegrees(180 + yaw));
        matrixStack.translate(0, -1.5, 0);
        VertexConsumer vertexConsumer = vertexConsumerProvider.getBuffer(getRenderLayer(livingEntity, true, false, true));
        ticker.render(matrixStack, vertexConsumer, i);
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
