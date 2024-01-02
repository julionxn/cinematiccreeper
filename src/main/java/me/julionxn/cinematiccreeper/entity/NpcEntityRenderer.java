package me.julionxn.cinematiccreeper.entity;

import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.render.entity.MobEntityRenderer;
import net.minecraft.client.render.entity.model.EntityModelLayers;
import net.minecraft.client.render.entity.model.PlayerEntityModel;
import net.minecraft.client.util.DefaultSkinHelper;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

public class NpcEntityRenderer extends MobEntityRenderer<NpcEntity, PlayerEntityModel<NpcEntity>> {


    public NpcEntityRenderer(EntityRendererFactory.Context ctx) {
        super(ctx, new PlayerEntityModel<>(ctx.getPart(EntityModelLayers.PLAYER), false), 0.5f);
    }

    @Override
    public void render(NpcEntity livingEntity, float f, float g, MatrixStack matrixStack, VertexConsumerProvider vertexConsumerProvider, int i) {
        setModelPose(livingEntity);
        super.render(livingEntity, f, g, matrixStack, vertexConsumerProvider, i);
    }

    private void setModelPose(NpcEntity player) {
        PlayerEntityModel<NpcEntity> playerEntityModel = this.getModel();
        if (player.isSpectator()) {
            playerEntityModel.setVisible(false);
            playerEntityModel.head.visible = true;
            playerEntityModel.hat.visible = true;
        } else {
            playerEntityModel.setVisible(true);
            playerEntityModel.sneaking = player.isSneaking();
        }
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
