package me.julionxn.cinematiccreeper.core.skins;

import me.julionxn.cinematiccreeper.CinematicCreeper;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.texture.NativeImage;
import net.minecraft.client.texture.NativeImageBackedTexture;
import net.minecraft.client.util.DefaultSkinHelper;
import net.minecraft.util.Identifier;
import org.jetbrains.annotations.Nullable;
import org.lwjgl.BufferUtils;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.*;
import java.net.URL;
import java.nio.ByteBuffer;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;

@Environment(EnvType.CLIENT)
public class CachedSkin implements Serializable {

    private final int id;
    private final URL url;
    private transient boolean loaded;
    private transient Identifier texture = DefaultSkinHelper.getTexture();

    public CachedSkin(int id, URL url) {
        this.id = id;
        this.url = url;
    }

    public static void save(CachedSkin cachedSkin, File file) {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(file))) {
            oos.writeObject(cachedSkin);
        } catch (Exception e) {
            CinematicCreeper.LOGGER.error("Failed to save cached skin with id: " + cachedSkin.id, e);
        }
    }

    public static Optional<CachedSkin> read(File file) {
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(file))) {
            CachedSkin cachedSkin = (CachedSkin) ois.readObject();
            return Optional.of(cachedSkin);
        } catch (Exception e) {
            CinematicCreeper.LOGGER.error("Failed to load cached skin with path: " + file.toPath(), e);
            return Optional.empty();
        }
    }

    public int getId() {
        return id;
    }

    public URL getUrl(){
        return url;
    }

    public @Nullable Identifier getTexture(){
        if (!loaded) return null;
        return texture;
    }

    public void register(@Nullable Consumer<Identifier> afterRegister) {
        getSkinTextureFromUrlAsync(url)
                .orTimeout(10, TimeUnit.SECONDS)
                .handle((identifier, throwable) -> {
                    if (throwable != null) {
                        texture = DefaultSkinHelper.getTexture();
                        CinematicCreeper.LOGGER.error("Failed to load skin texture from Url: " + url, throwable);
                        return null;
                    }
                    return identifier;
                })
                .thenAccept(idText -> {
                    loaded = true;
                    texture = idText;
                    if (afterRegister != null){
                        afterRegister.accept(idText);
                    }
                    CinematicCreeper.LOGGER.info("Skin " + url + " cached successfully.");
                });
    }

    private CompletableFuture<Identifier> getSkinTextureFromUrlAsync(URL url) {
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
                Identifier identifier = new Identifier(CinematicCreeper.MOD_ID, String.valueOf(id));
                client.execute(() -> client.getTextureManager().registerTexture(identifier, tex));
                out.close();
                return identifier;
            } catch (IOException e) {
                throw new RuntimeException("Something failed while retrieving a skin with Url: " + url, e);
            }
        }, client);
    }

}
