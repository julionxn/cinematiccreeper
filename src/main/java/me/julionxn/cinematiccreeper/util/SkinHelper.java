package me.julionxn.cinematiccreeper.util;

import me.julionxn.cinematiccreeper.CinematicCreeper;
import me.julionxn.cinematiccreeper.entity.NpcEntity;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.texture.NativeImage;
import net.minecraft.client.texture.NativeImageBackedTexture;
import net.minecraft.util.Identifier;
import net.minecraft.world.World;
import org.lwjgl.BufferUtils;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.URL;
import java.nio.ByteBuffer;

public class SkinHelper {

    public static void updateSkinOf(World world, NpcEntity entity){
        String npcId = entity.getNpcId();
        try {
            URL url = new URL(entity.getSkinUrl());
            Identifier identifier = getSkinTextureFromUrl(url, npcId);
            entity.setSkin(identifier);
        } catch (IOException e) {
            CinematicCreeper.LOGGER.error("Failed to load skin from Npc with id: " + npcId, e);
        }
    }

    private static Identifier getSkinTextureFromUrl(URL url, String npcId) throws IOException {
        BufferedImage image = ImageIO.read(url);
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        ImageIO.write(image, "png", out);
        byte[] bytes = out.toByteArray();
        ByteBuffer data = BufferUtils.createByteBuffer(bytes.length).put(bytes);
        data.flip();
        NativeImageBackedTexture tex = new NativeImageBackedTexture(NativeImage.read(data));
        Identifier identifier = new Identifier(CinematicCreeper.MOD_ID, npcId);
        MinecraftClient.getInstance().execute(() -> MinecraftClient.getInstance().getTextureManager().registerTexture(identifier, tex));
        out.close();
        return identifier;
    }

}
