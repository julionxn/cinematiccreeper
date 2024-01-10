package me.julionxn.cinematiccreeper.core.paths;

import me.julionxn.cinematiccreeper.CinematicCreeper;
import me.julionxn.cinematiccreeper.util.RenderUtils;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.rendering.v1.WorldRenderContext;
import net.minecraft.client.MinecraftClient;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.Vec3d;

import java.util.List;

@Environment(EnvType.CLIENT)
public class PathRenderer {

    private static final Identifier TEXTURE = new Identifier(CinematicCreeper.MOD_ID, "point.png");

    public static void render(MinecraftClient client, WorldRenderContext context, PlayerPathHolder playerPathHolder) {
        Path path = playerPathHolder.path();
        List<PathAction> pathActions = path.getActions();
        if (pathActions.size() > 1) {
            Vec3d firstPos = pathActions.get(0).getPos().add(0, 0.5, 0);
            for (PathAction action : pathActions) {
                Vec3d endPos = action.getPos().add(0, 0.5, 0);
                RenderUtils.renderLine(client, context.matrixStack(), firstPos, endPos, 0xff0000);
                RenderUtils.renderBillboardTexture(client, endPos, TEXTURE, 1f, 1f, 1f);
                firstPos = endPos;
            }
        } else if (pathActions.size() == 1) {
            RenderUtils.renderBillboardTexture(client, pathActions.get(0).getPos().add(0, 0.5, 0), TEXTURE, 1f, 1f, 1f);
        }
    }


}
