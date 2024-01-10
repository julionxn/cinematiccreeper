package me.julionxn.cinematiccreeper.screen.hud;

import me.julionxn.cinematiccreeper.CinematicCreeper;
import me.julionxn.cinematiccreeper.core.camera.CameraRecording;
import me.julionxn.cinematiccreeper.core.managers.CameraManager;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.util.Identifier;

public class RecordingHud {

    private static final Identifier TIMELINE_TEXTURE = new Identifier(CinematicCreeper.MOD_ID, "textures/gui/timeline.png");

    public static void onHudRender(DrawContext context, float tickDelta) {
        CameraRecording recording = CameraManager.getInstance().getCurrentCameraRecording();
        if (recording == null) return;
        MinecraftClient client = MinecraftClient.getInstance();
        if (client == null) return;
        int currentTick = recording.getTick();
        int displayLength = recording.getDisplayLength();
        int centerX = context.getScaledWindowWidth()  / 2;
        context.drawCenteredTextWithShadow(client.textRenderer, "Tick: " + currentTick + " / " + displayLength,
                centerX, 8, 0xffffff);
        context.drawTexture(TIMELINE_TEXTURE, centerX - 150, 20, 10, 0, 0, 300, 20, 300, 20);
        int width = 296;
        int startingX = centerX - 148;
        float spacing = (float) width / displayLength;
        float remainingSpacing = width - (spacing * displayLength);
        for (int i = 0; i < displayLength + 1; i++) {
            boolean isSecond = i % 20 == 0;
            int x = (int) (startingX + spacing * i);
            if (i != 0 && remainingSpacing > 0) {
                x += 1;
                remainingSpacing -= 1;
            }
            int y = isSecond ? 22 : 26;
            int height = isSecond ? 16 : 7;
            int color = recording.containsSnap(i) ? 0xffff1717 : i == currentTick ? 0xff3af6eb : 0xff8d697a;
            context.fill(x, y, x + 1, y + height, 100, color);
        }

    }


}
