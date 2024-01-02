package me.julionxn.cinematiccreeper.managers.paths;

import com.mojang.blaze3d.systems.RenderSystem;
import me.julionxn.cinematiccreeper.CinematicCreeper;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.rendering.v1.WorldRenderContext;
import net.minecraft.client.render.*;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.RotationAxis;
import net.minecraft.util.math.Vec3d;
import org.joml.Matrix4f;
import org.lwjgl.opengl.GL11;

import java.util.List;

@Environment(EnvType.CLIENT)
public class PathRenderer {

    private static final Identifier TEXTURE = new Identifier(CinematicCreeper.MOD_ID, "point.png");

    public static void render(WorldRenderContext context, PlayerPathState playerPathState) {
        Path path = playerPathState.path();
        List<PathAction> pathActions = path.getActions();
        if (pathActions.size() > 1) {
            Vec3d firstPos = pathActions.get(0).getPos().add(0, 0.5, 0);
            for (PathAction action : pathActions) {
                Vec3d newPos = action.getPos().add(0, 0.5, 0);
                renderLine(context, firstPos, newPos);
                renderBillboardTexture(context, newPos, TEXTURE, 1f, 1f, 1f);
                firstPos = newPos;
            }
        } else if (pathActions.size() == 1) {
            renderBillboardTexture(context, pathActions.get(0).getPos().add(0, 0.5, 0), TEXTURE, 1f, 1f, 1f);
        }
    }

    public static void renderBillboardTexture(WorldRenderContext context, Vec3d targetPosition, Identifier texture, float r, float g, float b) {
        Camera camera = context.camera();
        Vec3d cameraPosition = camera.getPos();
        Vec3d transformedPosition = targetPosition.subtract(cameraPosition);
        MatrixStack matrixStack = new MatrixStack();
        matrixStack.multiply(RotationAxis.POSITIVE_X.rotationDegrees(camera.getPitch()));
        matrixStack.multiply(RotationAxis.POSITIVE_Y.rotationDegrees(camera.getYaw() + 180.0F));
        matrixStack.translate(transformedPosition.x, transformedPosition.y, transformedPosition.z);
        Vec3d dir = new Vec3d(cameraPosition.z - targetPosition.z, cameraPosition.y - targetPosition.y, cameraPosition.x - targetPosition.x);
        double angle = Math.atan2(dir.z, dir.x);
        double angle2 = Math.atan2(dir.y, Math.sqrt(dir.x * dir.x + dir.z * dir.z));
        matrixStack.multiply(RotationAxis.NEGATIVE_Y.rotation((float) -angle));
        matrixStack.multiply(RotationAxis.NEGATIVE_X.rotation((float) angle2));
        matrixStack.translate(-0.5, -0.5, 0);
        Matrix4f positionMatrix = matrixStack.peek().getPositionMatrix();
        Tessellator tessellator = Tessellator.getInstance();
        BufferBuilder buffer = tessellator.getBuffer();
        buffer.begin(VertexFormat.DrawMode.QUADS, VertexFormats.POSITION_TEXTURE_COLOR);
        buffer.vertex(positionMatrix, 0, 1, 0).texture(0f, 0f).color(r, g, b, 1.0f).next();
        buffer.vertex(positionMatrix, 0, 0, 0).texture(0f, 1f).color(r, g, b, 1.0f).next();
        buffer.vertex(positionMatrix, 1, 0, 0).texture(1f, 1f).color(r, g, b, 1.0f).next();
        buffer.vertex(positionMatrix, 1, 1, 0).texture(1f, 0f).color(r, g, b, 1.0f).next();
        RenderSystem.setShader(GameRenderer::getPositionTexColorProgram);
        RenderSystem.setShaderTexture(0, texture);
        RenderSystem.setShaderColor(r, g, b, 1.0f);
        RenderSystem.disableCull();
        tessellator.draw();
        RenderSystem.enableCull();
        matrixStack.pop();
    }

    public static void renderLine(WorldRenderContext context, Vec3d from, Vec3d to) {
        renderLine(context, from, to, 0f, 0f, 1f);
    }

    public static void renderLine(WorldRenderContext context, Vec3d from, Vec3d to, float r, float g, float b) {
        Camera camera = context.camera();
        Vec3d transformedPosition = to.subtract(camera.getPos());
        MatrixStack matrixStack = new MatrixStack();
        matrixStack.multiply(RotationAxis.POSITIVE_X.rotationDegrees(camera.getPitch()));
        matrixStack.multiply(RotationAxis.POSITIVE_Y.rotationDegrees(camera.getYaw() + 180.0f));
        matrixStack.translate(transformedPosition.x, transformedPosition.y, transformedPosition.z);
        Vec3d vector = to.subtract(from);
        double yaw = Math.atan2(vector.z, vector.x);
        double pitch = Math.atan2(vector.y, Math.sqrt(vector.x * vector.x + vector.z * vector.z));
        matrixStack.multiply(RotationAxis.NEGATIVE_Y.rotation((float) yaw));
        matrixStack.multiply(RotationAxis.NEGATIVE_Z.rotation((float) -pitch));
        matrixStack.scale((float) -to.distanceTo(from), 0.02f, 1);
        Matrix4f positionMatrix = matrixStack.peek().getPositionMatrix();
        Tessellator tessellator = Tessellator.getInstance();
        BufferBuilder buffer = tessellator.getBuffer();
        buffer.begin(VertexFormat.DrawMode.QUADS, VertexFormats.POSITION_COLOR);
        buffer.vertex(positionMatrix, 0, 1, 0).color(r, g, b, 1f).next();
        buffer.vertex(positionMatrix, 0, 0, 0).color(r, g, b, 1f).next();
        buffer.vertex(positionMatrix, 1, 0, 0).color(r, g, b, 1f).next();
        buffer.vertex(positionMatrix, 1, 1, 0).color(r, g, b, 1f).next();
        RenderSystem.setShader(GameRenderer::getPositionColorProgram);
        RenderSystem.setShaderColor(1f, 1f, 1f, 1f);
        RenderSystem.disableCull();
        RenderSystem.depthFunc(GL11.GL_ALWAYS);
        tessellator.draw();
        RenderSystem.depthFunc(GL11.GL_LEQUAL);
        RenderSystem.enableCull();
    }


}
