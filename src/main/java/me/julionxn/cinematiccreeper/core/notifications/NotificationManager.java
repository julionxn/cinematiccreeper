package me.julionxn.cinematiccreeper.core.notifications;

import com.google.common.collect.ImmutableMap;
import me.julionxn.cinematiccreeper.CinematicCreeper;
import me.julionxn.cinematiccreeper.core.Interpolation;
import me.julionxn.cinematiccreeper.util.RenderUtils;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

import java.util.LinkedList;
import java.util.Queue;

public class NotificationManager {

    private static final Identifier TEXTURE = new Identifier(CinematicCreeper.MOD_ID, "textures/gui/notification.png");
    private static final ImmutableMap<Notification.Type, Identifier> ICON_TEXTURES = ImmutableMap.of(
            Notification.Type.ERROR, new Identifier(CinematicCreeper.MOD_ID, "textures/gui/error.png"),
            Notification.Type.WARNING, new Identifier(CinematicCreeper.MOD_ID, "textures/gui/warning.png"),
            Notification.Type.OK, new Identifier(CinematicCreeper.MOD_ID, "textures/gui/ok.png")
    );
    private final Queue<Notification> queue = new LinkedList<>();
    private static final float inDuration = 10;
    private static final float outDuration = 8;
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
            y = Interpolation.EASE_OUT.interpolate(t, startingY, targetY);
        } else if (delta >= midDuration + inDuration) {
            float t = (delta - midDuration - inDuration) / outDuration;
            y = Interpolation.LINEAR.interpolate(t, targetY, startingY);
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
        Identifier iconTexture = ICON_TEXTURES.get(type);
        context.drawTexture(TEXTURE, startingX, y, 0, 0, 0, 140, 20, 140, 20);
        RenderUtils.drawScrollableText(context, textRenderer, Text.of(text), startingX + 70, startingX + 25, y, startingX + 130, y + 20);
        context.drawTexture(iconTexture, startingX + 7, y + 4, 0, 0, 0, 12, 12, 12, 12);
    }

}
