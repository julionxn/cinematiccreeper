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
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;

public class SkinHelper {

    public static void updateSkinOf(World world, NpcEntity entity){
        String npcId = entity.getNpcId();
        try {
            URL url = new URL(entity.getSkinUrl());
            //Identifier identifier = getSkinTextureFromUrl(url, npcId);
            getSkinTextureFromUrlAsync(url, npcId)
                    .thenAccept(entity::setSkin)
                    .orTimeout(30, TimeUnit.SECONDS)
                    .handle((identifier, throwable) -> {
                        if (throwable != null) {
                            CinematicCreeper.LOGGER.error("Something failed while retrieving a skin with Url: " + url, throwable);
                            return null;
                        }
                        return identifier;
                    });
        } catch (IOException e) {
            CinematicCreeper.LOGGER.error("Failed to load skin from Npc with id: " + npcId, e);
        }
    }

    public static CompletableFuture<Identifier> getSkinTextureFromUrlAsync(URL url, String npcId) {
        MinecraftClient client = MinecraftClient.getInstance();
        return CompletableFuture.supplyAsync(() -> {
            try {
                BufferedImage image = ImageIO.read(url);
                ByteArrayOutputStream out = new ByteArrayOutputStream();
                ImageIO.write(image, "png", out);
                byte[] bytes = out.toByteArray();
                ByteBuffer data = BufferUtils.createByteBuffer(bytes.length).put(bytes);
                data.flip();
                NativeImageBackedTexture tex = new NativeImageBackedTexture(NativeImage.read(data));
                Identifier identifier = new Identifier(CinematicCreeper.MOD_ID, npcId);
                MinecraftClient.getInstance().execute(() ->
                        client.getTextureManager().registerTexture(identifier, tex));
                out.close();
                return identifier;
            } catch (IOException e) {
                throw new RuntimeException("Failed to load skin texture", e);
            }
        }, client);
    }

}
