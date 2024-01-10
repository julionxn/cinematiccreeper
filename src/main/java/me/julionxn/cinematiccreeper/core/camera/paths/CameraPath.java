package me.julionxn.cinematiccreeper.core.camera.paths;

import me.julionxn.cinematiccreeper.CinematicCreeper;
import me.julionxn.cinematiccreeper.core.camera.CameraRecording;
import me.julionxn.cinematiccreeper.core.camera.Snap;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;

public abstract class CameraPath{

    protected static final Identifier POINT_TEXTURE = new Identifier(CinematicCreeper.MOD_ID, "point.png");
    protected final CameraRecording cameraRecording;

    public CameraPath(CameraRecording recording){
        cameraRecording = recording;
        onPointsModified();
    }

    public abstract Snap getInterpolatedSnap(int tick);
    public abstract void render(MinecraftClient client, MatrixStack stack);
    public abstract void onPointsModified();

}
