package me.julionxn.cinematiccreeper.core.managers;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.annotations.Expose;
import me.julionxn.cinematiccreeper.CinematicCreeper;
import net.fabricmc.loader.api.FabricLoader;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.nio.file.Path;

public abstract class SerializableJsonManager<T extends SerializableJsonManager<T>> {

    private static final Gson GSON = new GsonBuilder()
            .setPrettyPrinting()
            .disableHtmlEscaping()
            .excludeFieldsWithoutExposeAnnotation()
            .serializeNulls()
            .create();
    private final File configFile;
    private final Class<T> clazz;
    @Expose
    public final float version;

    protected SerializableJsonManager(String path, float version, Class<T> clazz) {
        Path ccFolder = FabricLoader.getInstance().getConfigDir().resolve("cinematiccreeper");
        File file = ccFolder.toFile();
        if (!file.exists()){
            boolean made = file.mkdir();
            if (made) CinematicCreeper.LOGGER.info("CinematicCreeper config folder created.");
        }
        this.configFile = ccFolder.resolve(path).toFile();
        this.version = version;
        this.clazz = clazz;
    }

    public void load() {
        try {
            if (!configFile.exists()) {
                save();
                return;
            }
            T data = GSON.fromJson(new FileReader(configFile), clazz);
            if (data == null) return;
            if (data.version != version) {
                CinematicCreeper.LOGGER.error("Mismatching version with current version of file " + configFile.getPath() + ". Restarting file.");
                save();
            } else {
                CinematicCreeper.LOGGER.info("Loading " + configFile.getPath());
                setValues(data);
            }
            afterLoad();
        } catch (IOException | IllegalAccessException e) {
            CinematicCreeper.LOGGER.error("Something went wrong while loading the config.", e);
        }
    }

    private void setValues(T data) throws IllegalAccessException {
        T instance = getCurrentInstance();
        Field[] fields = clazz.getDeclaredFields();
        for (Field field : fields) {
            if (!field.isAnnotationPresent(Expose.class)) continue;
            int modifiers = field.getModifiers();
            if (Modifier.isStatic(modifiers) || Modifier.isTransient(modifiers)) continue;
            field.setAccessible(true);
            field.set(instance, field.get(data));
        }
    }

    protected abstract T getCurrentInstance();
    protected abstract void afterLoad();

    public void save() {
        try (FileWriter fileWriter = new FileWriter(configFile)) {
            GSON.toJson(this, fileWriter);
        } catch (IOException e) {
            CinematicCreeper.LOGGER.error("Something went wrong while saving the config.");
        }
    }

}
