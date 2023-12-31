package me.julionxn.cinematiccreeper.core.managers;

import me.julionxn.cinematiccreeper.CinematicCreeper;
import me.julionxn.cinematiccreeper.core.skins.CachedSkin;
import me.julionxn.cinematiccreeper.entity.NpcEntity;
import me.julionxn.cinematiccreeper.util.mixins.NpcData;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.util.Identifier;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Environment(EnvType.CLIENT)
public class NpcSkinManager {

    private final HashMap<String, CachedSkin> cachedSkins = new HashMap<>();
    private File cachedSkinsFolder;

    private NpcSkinManager() {
    }

    public static NpcSkinManager getInstance() {
        return SingletonHolder.INSTANCE;
    }

    public void load() {
        cachedSkinsFolder = FabricLoader.getInstance().getConfigDir().resolve("cinematiccreeper/cc_cache").toFile();
        if (cachedSkinsFolder.mkdir()) return;
        File[] skinFiles = cachedSkinsFolder.listFiles();
        if (skinFiles == null) return;
        for (File skinFile : skinFiles) {
            Optional<CachedSkin> cachedSkinOptional = CachedSkin.load(skinFile);
            if (cachedSkinOptional.isEmpty()) continue;
            CachedSkin cachedSkin = cachedSkinOptional.get();
            cachedSkin.loadAndGetTexture();
            cachedSkins.put(cachedSkin.getId(), cachedSkin);
        }
    }

    public void save() {
        if (!cachedSkinsFolder.exists()){
            if (!cachedSkinsFolder.mkdir()){
                CinematicCreeper.LOGGER.error("Cache folder cannot be created.");
                return;
            }
        }
        Path path = cachedSkinsFolder.toPath();
        for (CachedSkin cachedSkin : cachedSkins.values()) {
            CachedSkin.save(cachedSkin, path.resolve(cachedSkin.getId().hashCode() + ".skin").toFile());
        }
    }

    public void updateSkinOf(NpcEntity entity) {
        String npcId = ((NpcData) entity).cinematiccreeper$getId();
        try {
            URL url = new URL(entity.getSkinUrl());
            String id = getIdFromUrl(url);
            Identifier texture = cachedSkins.computeIfAbsent(id, key -> new CachedSkin(id, url)).loadAndGetTexture();
            entity.setSkin(texture);
        } catch (IOException e) {
            CinematicCreeper.LOGGER.error("Failed to load skin from Npc with id: " + npcId, e);
        }
    }

    public String getIdFromUrl(URL url){
        return getIdFromUrl(url.toString());
    }

    public String getIdFromUrl(String url) {
        Pattern pattern = Pattern.compile("[^a-z0-9_]");
        Matcher matcher = pattern.matcher(url.toLowerCase());
        return matcher.replaceAll("_");
    }

    private static class SingletonHolder {
        private static final NpcSkinManager INSTANCE = new NpcSkinManager();
    }

}
