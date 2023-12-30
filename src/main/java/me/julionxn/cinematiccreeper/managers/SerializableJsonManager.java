package me.julionxn.cinematiccreeper.managers;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import me.julionxn.cinematiccreeper.CinematicCreeper;
import net.fabricmc.loader.api.FabricLoader;
import org.jetbrains.annotations.Nullable;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public abstract class SerializableJsonManager<T> {

    private static final Gson GSON = new GsonBuilder()
            .setPrettyPrinting()
            .disableHtmlEscaping()
            .excludeFieldsWithoutExposeAnnotation()
            .create();
    private final String path;
    private final Class<T> clazz;

    protected SerializableJsonManager(String path, Class<T> clazz) {
        this.path = path;
        this.clazz = clazz;
    }

    public void load() {
        File configFile = FabricLoader.getInstance().getConfigDir().resolve(path).toFile();
        try {
            T data = configFile.exists() ? GSON.fromJson(new FileReader(configFile), clazz) : null;
            onLoad(data);
            if (!configFile.exists()) {
                save();
            }
        } catch (IOException e) {
            CinematicCreeper.LOGGER.error("Something went wrong while loading the config.", e);
        }
    }

    protected abstract void onLoad(@Nullable T data);

    public void save() {
        File configFile = FabricLoader.getInstance().getConfigDir().resolve(path).toFile();
        try (FileWriter fileWriter = new FileWriter(configFile)) {
            GSON.toJson(this, fileWriter);
        } catch (IOException e) {
            CinematicCreeper.LOGGER.error("Something went wrong while saving the config.");
        }
    }

}
