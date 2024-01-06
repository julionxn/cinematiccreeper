package me.julionxn.cinematiccreeper.core.notifications;

import me.julionxn.cinematiccreeper.CinematicCreeper;
import me.julionxn.cinematiccreeper.core.Easing;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.minecraft.util.Util;
import net.minecraft.util.math.MathHelper;

import java.util.LinkedList;
import java.util.Queue;

public class NotificationManager {

    private static final Identifier TEXTURE = new Identifier(CinematicCreeper.MOD_ID, "textures/gui/notification.png");
    private static final Identifier ERROR = new Identifier(CinematicCreeper.MOD_ID, "textures/gui/error.png");
    private static final Identifier WARNING = new Identifier(CinematicCreeper.MOD_ID, "textures/gui/warning.png");
    private static final Identifier OK = new Identifier(CinematicCreeper.MOD_ID, "textures/gui/ok.png");

    private final Queue<Notification> queue = new LinkedList<>();
    private static final float inDuration = 10;
    private static final float outDuration = 10;
    private float delta;

    private static class SingletonHolder{
        private static final NotificationManager INSTANCE = new NotificationManager();
    }

    public static NotificationManager getInstance(){
        return SingletonHolder.INSTANCE;
    }

    public void add(Notification.Type type, Text text){
        add(new Notification(type, text));
    }

    public void add(Notification notification){
        if (queue.size() > 5){
            queue.clear();
            delta = 0;
        }
        queue.add(notification);
    }

    public void draw(TextRenderer textRenderer, DrawContext context){
        if (queue.isEmpty()) return;
        delta += MinecraftClient.getInstance().getLastFrameDuration();
        float startingY = context.getScaledWindowHeight();
        float targetY = context.getScaledWindowHeight() - 20;
        float y = targetY;
        Notification notification = queue.peek();
        if (notification == null) return;
        float midDuration = (float) (35 * Math.pow(Math.E, -0.3 * queue.size()) + 5);
        if (delta <= inDuration){
            float t = delta / inDuration;
            y = Easing.EASE_OUT.interpolate(t, startingY, targetY);
        } else if (delta >= midDuration + inDuration) {
            y = Easing.NONE.interpolate((delta - midDuration - inDuration) / outDuration, targetY, startingY);
        }
        context.getMatrices().push();
        context.getMatrices().translate(0, 0, 800f);
        renderNotification(textRenderer, context, (int) y, notification.type(), notification.text().getString());
        context.getMatrices().pop();
        context.draw();
        if (delta >= midDuration + inDuration + outDuration){
            queue.remove();
            delta = 0;
        }
    }

    private void renderNotification(TextRenderer textRenderer, DrawContext context, int y, Notification.Type type, String text){
        int startingX = context.getScaledWindowWidth() - 140;
        Identifier typeTexture = type == Notification.Type.OK ? OK : type == Notification.Type.ERROR ? ERROR : WARNING;
        context.drawTexture(TEXTURE, startingX, y, 0, 0, 0, 140, 20, 140, 20);
        drawScrollableText(context, textRenderer, Text.of(text), startingX + 70, startingX + 25, y, startingX + 130, y + 20);
        context.drawTexture(typeTexture, startingX + 7, y + 4, 0, 0, 0, 12, 12, 12, 12);
    }

    private void drawScrollableText(DrawContext context, TextRenderer textRenderer, Text text, int centerX, int startX, int startY, int endX, int endY) {
        int i = textRenderer.getWidth(text);
        int j = (startY + endY - textRenderer.fontHeight) / 2 + 1;
        int k = endX - startX;
        if (i > k) {
            int l = i - k;
            double d = (double) Util.getMeasuringTimeMs() / 1000.0;
            double e = Math.max((double)l * 0.5, 3.0);
            double f = Math.sin(1.57079632 * Math.cos(Math.PI * 2 * d / e)) / 2.0 + 0.5;
            double g = MathHelper.lerp(f, 0.0, l);
            context.enableScissor(startX, startY, endX, endY);
            context.drawTextWithShadow(textRenderer, text, startX - (int)g, j, 16777215);
            context.disableScissor();
        } else {
            int l = MathHelper.clamp(centerX, startX + i / 2, endX - i / 2);
            context.drawCenteredTextWithShadow(textRenderer, text, l, j, 16777215);
        }
    }

}
