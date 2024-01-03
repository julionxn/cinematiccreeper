package me.julionxn.cinematiccreeper.managers;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import me.julionxn.cinematiccreeper.CinematicCreeper;
import net.fabricmc.loader.api.FabricLoader;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Optional;

public abstract class SerializableJsonManager<T extends SerializableJsonManager<T>> {

    private static final Gson GSON = new GsonBuilder()
            .setPrettyPrinting()
            .disableHtmlEscaping()
            .excludeFieldsWithoutExposeAnnotation()
            .serializeNulls()
            .create();
    private final File configFile;
    private final Class<T> clazz;

    protected SerializableJsonManager(String path, Class<T> clazz) {
        this.configFile = FabricLoader.getInstance().getConfigDir().resolve(path).toFile();
        this.clazz = clazz;
    }

    public void load() {
        try {
            Optional<T> data = configFile.exists() ? Optional.ofNullable(GSON.fromJson(new FileReader(configFile), clazz)) : Optional.empty();
            onLoad(data);
            if (!configFile.exists()) {
                save();
            }
        } catch (IOException e) {
            CinematicCreeper.LOGGER.error("Something went wrong while loading the config.", e);
        }
    }

    protected abstract void onLoad(Optional<T> dataOptional);

    public void save() {
        try (FileWriter fileWriter = new FileWriter(configFile)) {
            GSON.toJson(this, fileWriter);
        } catch (IOException e) {
            CinematicCreeper.LOGGER.error("Something went wrong while saving the config.");
        }
    }

}
