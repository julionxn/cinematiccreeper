package me.julionxn.cinematiccreeper.core.managers;

import com.google.gson.annotations.Expose;
import me.julionxn.cinematiccreeper.CinematicCreeper;
import me.julionxn.cinematiccreeper.core.skins.CachedSkin;
import me.julionxn.cinematiccreeper.entity.NpcEntity;
import me.julionxn.cinematiccreeper.util.mixins.PlayerData;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.Identifier;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Optional;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Environment(EnvType.CLIENT)
public class NpcSkinManager extends SerializableJsonManager<NpcSkinManager> {

    private final HashMap<Integer, CachedSkin> cachedSkins = new HashMap<>();
    private final File cachedSkinsFolder = FabricLoader.getInstance().getConfigDir().resolve("cinematiccreeper/cc_cache").toFile();
    @Expose
    private final HashMap<String, Integer> savedSkins = new HashMap<>();

    private NpcSkinManager() {
        super("cc_skins.json", 1.0f, NpcSkinManager.class);
    }

    public static NpcSkinManager getInstance() {
        return SingletonHolder.INSTANCE;
    }

    @Override
    protected void afterLoad() {
        if (cachedSkinsFolder.mkdir()) return;
        File[] skinFiles = cachedSkinsFolder.listFiles();
        if (skinFiles == null) return;
        for (File skinFile : skinFiles) {
            Optional<CachedSkin> cachedSkinOptional = CachedSkin.read(skinFile);
            if (cachedSkinOptional.isEmpty()) {
                try {
                    Files.delete(skinFile.toPath());
                } catch (IOException e) { /*empty catch*/ }
                continue;
            }
            CachedSkin cachedSkin = cachedSkinOptional.get();
            cachedSkin.register(texture -> cachedSkins.put(cachedSkin.getId(), cachedSkin));
        }
    }

    @Override
    public void save() {
        super.save();
        if (!cachedSkinsFolder.exists()){
            if (!cachedSkinsFolder.mkdir()){
                CinematicCreeper.LOGGER.error("Cache folder cannot be created.");
                return;
            }
        }
        Path path = cachedSkinsFolder.toPath();
        for (CachedSkin cachedSkin : cachedSkins.values()) {
            CachedSkin.save(cachedSkin, path.resolve(skinFile(cachedSkin)).toFile());
        }
    }

    private String skinFile(CachedSkin cachedSkin){
        return cachedSkin.getId() + ".skin";
    }

    public void updateSkinOf(NpcEntity entity) {
        String stringUrl = entity.getSkinUrl();
        if (stringUrl.isEmpty()) return;
        try {
            URL url = new URL(stringUrl);
            int id = getIdFromUrl(url);
            if (cachedSkins.containsKey(id)){
                Identifier texture = cachedSkins.get(id).getTexture();
                entity.setSkin(texture);
            } else {
                CachedSkin skin = new CachedSkin(id, url);
                skin.register(texture -> {
                    cachedSkins.put(id, skin);
                    entity.setSkin(texture);
                });
            }
        } catch (IOException e) {
            CinematicCreeper.LOGGER.error("Failed to load skin of Npc with UUID: " + entity.getUuid(), e);
        }
    }

    public void updateSkinOf(PlayerEntity entity) {
        PlayerData data = (PlayerData) entity;
        String stringUrl = data.cinematiccreeper$getSkinUrl();
        System.out.println("UPDATING: " + stringUrl);
        if (stringUrl.isEmpty()) {
            data.cinematiccreeper$setSkin(null);
            return;
        }
        try {
            URL url = new URL(stringUrl);
            int id = getIdFromUrl(url);
            if (cachedSkins.containsKey(id)){
                Identifier texture = cachedSkins.get(id).getTexture();
                if (texture == null) return;
                data.cinematiccreeper$setSkin(texture);
            } else {
                CachedSkin skin = new CachedSkin(id, url);
                skin.register(texture -> {
                    cachedSkins.put(id, skin);
                    data.cinematiccreeper$setSkin(texture);
                });
            }
        } catch (IOException e) {
            CinematicCreeper.LOGGER.error("Failed to load skin of Npc with UUID: " + entity.getUuid(), e);
        }
    }

    private int getIdFromUrl(URL url){
        return getIdFromUrl(url.toString());
    }

    private int getIdFromUrl(String url) {
        Pattern pattern = Pattern.compile("[^a-z0-9_]");
        Matcher matcher = pattern.matcher(url.toLowerCase());
        return matcher.replaceAll("_").hashCode();
    }

    public void addSavedSkin(String name, CachedSkin skin, Runnable onDone){
        skin.register(texture -> {
            cachedSkins.put(skin.getId(), skin);
            CachedSkin.save(skin, cachedSkinsFolder.toPath().resolve(skinFile(skin)).toFile());
            savedSkins.put(name, skin.getId());
            onDone.run();
        });
    }

    public void addSavedSkin(String name, String url, Runnable onDone) throws MalformedURLException {
        int id = getIdFromUrl(url);
        CachedSkin skin = new CachedSkin(id, new URL(url));
        addSavedSkin(name, skin, onDone);
    }

    public void removeSavedSkin(String name){
        removeCachedSkin(savedSkins.get(name));
        savedSkins.remove(name);
    }

    public Set<String> getSavedSkins(){
        return savedSkins.keySet();
    }

    public CachedSkin getSkinFromName(String name){
        return cachedSkins.get(savedSkins.get(name));
    }

    public void removeCachedSkin(int id){
        CachedSkin cachedSkin = cachedSkins.get(id);
        if (cachedSkin == null) return;
        try {
            Files.delete(cachedSkinsFolder.toPath().resolve(skinFile(cachedSkin)));
        } catch (IOException e) {
            CinematicCreeper.LOGGER.error("Something went wrong file trying to delete a cached skin with id: " + id, e);
        }
        cachedSkins.remove(id);
    }

    @Override
    protected NpcSkinManager getCurrentInstance() {
        return this;
    }

    private static class SingletonHolder {
        private static final NpcSkinManager INSTANCE = new NpcSkinManager();
    }

}
