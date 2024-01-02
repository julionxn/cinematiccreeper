package me.julionxn.cinematiccreeper.entity;

import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.model.ModelPart;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.EntityRenderer;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.render.entity.LivingEntityRenderer;
import net.minecraft.client.render.entity.MobEntityRenderer;
import net.minecraft.client.render.entity.model.EntityModelLayer;
import net.minecraft.client.render.entity.model.EntityModelLayers;
import net.minecraft.client.render.entity.model.PlayerEntityModel;
import net.minecraft.client.util.DefaultSkinHelper;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.RotationAxis;

public class NpcEntityRenderer extends MobEntityRenderer<NpcEntity, PlayerEntityModel<NpcEntity>> {

    private final EntityRendererFactory.Context ctx;

    public NpcEntityRenderer(EntityRendererFactory.Context ctx) {
        super(ctx, new PlayerEntityModel<>(ctx.getPart(EntityModelLayers.PLAYER), false), 0.5f);
        this.ctx = ctx;
    }

    @Override
    public void render(NpcEntity livingEntity, float f, float g, MatrixStack matrixStack, VertexConsumerProvider vertexConsumerProvider, int i) {
        setModelPose(livingEntity);
        super.render(livingEntity, f, g, matrixStack, vertexConsumerProvider, i);
        /*matrixStack.push();
        matrixStack.translate(0, 1.5, 0);
        matrixStack.multiply(RotationAxis.POSITIVE_X.rotationDegrees(180));
        matrixStack.multiply(RotationAxis.POSITIVE_Y.rotationDegrees(f));

        //El yaw del ModelPart disminuye con el tiempo, por lo que seria una buena idea
        //al renderizar establecer el valor apartir del float yaw de la cabeza
        MobEntityRenderer<NpcEntity, PlayerEntityModel<NpcEntity>> renderer = (MobEntityRenderer<NpcEntity, PlayerEntityModel<NpcEntity>>) MinecraftClient.getInstance().getEntityRenderDispatcher().getRenderer(livingEntity);
        ModelPart head = renderer.getModel().getHead();
        ModelPart modelPart = ctx.getPart(EntityModelLayers.PLAYER);
        //Para quitar una parte que se ve fea
        modelPart.getChild("cloak").visible = false;
        modelPart.getChild("head").setAngles(head.pitch,
                head.yaw,
                head.roll);
        RenderSystem.disableCull();
        //RenderSystem.setShaderColor(1.0f, 0.0f, 0.0f, 1.0f);
        modelPart.render(matrixStack,
                vertexConsumerProvider.getBuffer(RenderLayer.getTextSeeThrough(getTexture(livingEntity))),
                getLight(livingEntity, g), i, 0.0f, 0.5f, 1.0f, 0.5f);
        RenderSystem.enableCull();
        matrixStack.pop();*/
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
