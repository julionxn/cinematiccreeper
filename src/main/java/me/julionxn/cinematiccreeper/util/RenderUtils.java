package me.julionxn.cinematiccreeper.util;


import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.render.*;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.minecraft.util.Util;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.RotationAxis;
import net.minecraft.util.math.Vec3d;
import org.joml.Matrix4f;

public class RenderUtils {

    public static void renderBlockOutline(MinecraftClient client, MatrixStack stack, BlockPos blockPos, int color){
        System.out.println("HOLA");
        color = 0xffffffff;
        Vec3d a = new Vec3d(blockPos.getX(), blockPos.getY(), blockPos.getZ());
        Vec3d b = a.add(0, 0, 1);
        Vec3d c = a.add(1, 0, 0);
        Vec3d d = a.add(1, 0, 1);
        Vec3d e = a.add(0, 1, 0);
        Vec3d f = b.add(0, 1, 0);
        Vec3d g = c.add(0, 1, 0);
        Vec3d h = d.add(0, 1, 0);
        renderLine(client, stack, a, c, color);
        renderLine(client, stack, a, b, color);
        renderLine(client, stack, c, d, color);
        renderLine(client, stack, b, d, color);
        renderLine(client, stack, a, e, color);
        renderLine(client, stack, c, g, color);
        renderLine(client, stack, d, h, color);
        renderLine(client, stack, b, f, color);
        renderLine(client, stack, e, f, color);
        renderLine(client, stack, e, g, color);
        renderLine(client, stack, h, f, color);
        renderLine(client, stack, h, g, color);
    }


    public static void renderLine(MinecraftClient client, MatrixStack stack, Vec3d start, Vec3d end, int color){
        Tessellator tessellator = Tessellator.getInstance();
        BufferBuilder buffer = tessellator.getBuffer();
        Camera camera = client.gameRenderer.getCamera();
        Vec3d camPos = camera.getPos();
        Vec3d vecStart = start.subtract(camPos);
        Vec3d vecEnd = vecStart.add(end.subtract(start));
        Matrix4f s = stack.peek().getPositionMatrix();
        float[] c = hexToFloatColor(color);
        buffer.begin(VertexFormat.DrawMode.DEBUG_LINES, VertexFormats.POSITION_COLOR);
        buffer.vertex(s, (float) vecStart.x, (float) vecStart.y, (float) vecStart.z).color(c[0], c[1], c[2], 1f).next();
        buffer.vertex(s, (float) vecEnd.x, (float) vecEnd.y, (float) vecEnd.z).color(c[0], c[1], c[2], 1f).next();
        RenderSystem.setShader(GameRenderer::getPositionColorProgram);
        RenderSystem.enableBlend();
        RenderSystem.setShaderColor(1f, 1f, 1f, 1f);
        RenderSystem.enableDepthTest();
        tessellator.draw();
        RenderSystem.enableCull();
        RenderSystem.disableBlend();
    }

    public static void drawScrollableText(DrawContext context, TextRenderer textRenderer, Text text, int centerX, int startX, int startY, int endX, int endY) {
        int i = textRenderer.getWidth(text);
        int j = (startY + endY - textRenderer.fontHeight) / 2 + 1;
        int k = endX - startX;
        if (i > k) {
            int l = i - k;
            double d = (double) Util.getMeasuringTimeMs() / 1000.0;
            double e = Math.max((double)l * 0.5, 3.0);
            double f = Math.sin(1.57079632 * Math.cos(Math.PI * 2 * d / e)) / 2.0 + 0.5;
            double g = net.minecraft.util.math.MathHelper.lerp(f, 0.0, l);
            context.enableScissor(startX, startY, endX, endY);
            context.drawTextWithShadow(textRenderer, text, startX - (int)g, j, 16777215);
            context.disableScissor();
        } else {
            int l = MathHelper.clamp(centerX, startX + i / 2, endX - i / 2);
            context.drawCenteredTextWithShadow(textRenderer, text, l, j, 16777215);
        }
    }

    public static float[] hexToFloatColor(int hexColor) {
        float red = ((hexColor >> 16) & 0xFF) / 255.0f;
        float green = ((hexColor >> 8) & 0xFF) / 255.0f;
        float blue = (hexColor & 0xFF) / 255.0f;

        return new float[]{red, green, blue};
    }

    public static void renderBillboardTexture(MinecraftClient client, Vec3d targetPosition, Identifier texture, float r, float g, float b) {
        Camera camera = client.gameRenderer.getCamera();
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
    }

}
