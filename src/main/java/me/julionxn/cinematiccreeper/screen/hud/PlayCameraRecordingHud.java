package me.julionxn.cinematiccreeper.screen.hud;

import me.julionxn.cinematiccreeper.CinematicCreeper;
import me.julionxn.cinematiccreeper.core.camera.CameraRecordingPlayer;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.util.Identifier;

public class PlayCameraRecordingHud {

    private final Identifier TEXTURE_3 = new Identifier(CinematicCreeper.MOD_ID, "textures/hud/3.png");
    private final Identifier TEXTURE_2 = new Identifier(CinematicCreeper.MOD_ID, "textures/hud/2.png");
    private final Identifier TEXTURE_1 = new Identifier(CinematicCreeper.MOD_ID, "textures/hud/1.png");
    private CameraRecordingPlayer cameraRecording;
    private float timeElapsed = -1;

    public void onHudRender(DrawContext context) {
        if (timeElapsed < 0) return;
        Identifier text = TEXTURE_3;
        if (timeElapsed >= 1 && timeElapsed < 2) text = TEXTURE_2;
        if (timeElapsed >= 2 && timeElapsed < 3) text = TEXTURE_1;
        context.drawTexture(text, context.getScaledWindowWidth() / 2 - 21, context.getScaledWindowHeight() / 2 - 33,
                0, 0, 0, 42, 66, 42, 66);
        timeElapsed += MinecraftClient.getInstance().getLastFrameDuration() / 20f;
        if (timeElapsed >= 3) {
            timeElapsed = -1;
            cameraRecording.play();
        }
    }

    public void setCameraRecording(CameraRecordingPlayer cameraRecording){
        this.cameraRecording = cameraRecording;
        timeElapsed = 0;
    }

}
