package me.julionxn.cinematiccreeper.blockentities.renderers;

import com.mojang.blaze3d.systems.RenderSystem;
import me.julionxn.cinematiccreeper.blockentities.GreenScreenBlockEntity;
import me.julionxn.cinematiccreeper.util.colors.Color;
import net.minecraft.client.render.*;
import net.minecraft.client.render.block.entity.BlockEntityRenderer;
import net.minecraft.client.render.block.entity.BlockEntityRendererFactory;
import net.minecraft.client.util.math.MatrixStack;
import org.joml.Matrix4f;

public class GreenScreenBlockEntityRenderer implements BlockEntityRenderer<GreenScreenBlockEntity> {

    private final BlockEntityRendererFactory.Context ctx;

    public GreenScreenBlockEntityRenderer(BlockEntityRendererFactory.Context ctx){
        this.ctx = ctx;
    }

    @Override
    public void render(GreenScreenBlockEntity entity, float tickDelta, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, int overlay) {
        matrices.push();
        Matrix4f positionMatrix = matrices.peek().getPositionMatrix();
        Tessellator tessellator = Tessellator.getInstance();
        BufferBuilder buffer = tessellator.getBuffer();
        Color color = entity.getColor();
        float r = color.getRed();
        float g = color.getGreen();
        float b = color.getBlue();
        buffer.begin(VertexFormat.DrawMode.QUADS, VertexFormats.POSITION_COLOR);
        buffer.vertex(positionMatrix, 1, 0, 0).color(r, g, b, 1f).next();
        buffer.vertex(positionMatrix, 1, 1, 0).color(r, g, b, 1f).next();
        buffer.vertex(positionMatrix, 0, 1, 0).color(r, g, b, 1f).next();
        buffer.vertex(positionMatrix, 0, 0, 0).color(r, g, b, 1f).next();
        buffer.vertex(positionMatrix, 0, 0, 0).color(r, g, b, 1f).next();
        buffer.vertex(positionMatrix, 0, 0, 1).color(r, g, b, 1f).next();
        buffer.vertex(positionMatrix, 0, 1, 1).color(r, g, b, 1f).next();
        buffer.vertex(positionMatrix, 0, 1, 0).color(r, g, b, 1f).next();
        buffer.vertex(positionMatrix, 0, 1, 0).color(r, g, b, 1f).next();
        buffer.vertex(positionMatrix, 0, 1, 1).color(r, g, b, 1f).next();
        buffer.vertex(positionMatrix, 1, 1, 1).color(r, g, b, 1f).next();
        buffer.vertex(positionMatrix, 1, 1, 0).color(r, g, b, 1f).next();
        buffer.vertex(positionMatrix, 1, 1, 0).color(r, g, b, 1f).next();
        buffer.vertex(positionMatrix, 1, 0, 0).color(r, g, b, 1f).next();
        buffer.vertex(positionMatrix, 1, 0, 1).color(r, g, b, 1f).next();
        buffer.vertex(positionMatrix, 1, 1, 1).color(r, g, b, 1f).next();
        buffer.vertex(positionMatrix, 1, 1, 1).color(r, g, b, 1f).next();
        buffer.vertex(positionMatrix, 0, 1, 1).color(r, g, b, 1f).next();
        buffer.vertex(positionMatrix, 0, 0, 1).color(r, g, b, 1f).next();
        buffer.vertex(positionMatrix, 1, 0, 1).color(r, g, b, 1f).next();
        buffer.vertex(positionMatrix, 1, 0, 1).color(r, g, b, 1f).next();
        buffer.vertex(positionMatrix, 0, 0, 1).color(r, g, b, 1f).next();
        buffer.vertex(positionMatrix, 0, 0, 0).color(r, g, b, 1f).next();
        buffer.vertex(positionMatrix, 1, 0, 0).color(r, g, b, 1f).next();
        RenderSystem.setShader(GameRenderer::getPositionColorProgram);
        RenderSystem.setShaderColor(1f, 1f, 1f, 1f);
        RenderSystem.enableDepthTest();
        RenderSystem.disableCull();
        tessellator.draw();
        RenderSystem.enableCull();
        matrices.pop();
    }

}
