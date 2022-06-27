package me.kevsal.minecraft.gbmc.core.common.util;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import lombok.Getter;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.logging.Logger;

/***
 * Generic Json configuration class
 */
public class JsonConfiguration {

    /***
     * The configuration on the disk
     */
    @Getter
    private final File file;

    /***
     * The parsed Gson Json object
     */
    @Getter
    private final JsonObject jsonObject;

    /***
     * Name of this configuration
     */
    @Getter
    private final String configName;

    /***
     * Create a new instance of the JsonConfiguration class, and setup all functionality required
     * @param configName Name of this configuration
     * @param file The configuration on the disk
     */
    public JsonConfiguration(String configName, File file) {
        this.file = file;
        this.jsonObject = getJsonFromFile(this.file);
        this.configName = configName;
    }

    /***
     * Get the JsonObject from the file
     * @param file The file to get the JsonObject from
     * @return The JsonObject from the file
     */
    private JsonObject getJsonFromFile(File file) {
        Path path = Paths.get(file.toString());
        String content;
        try {
            content = Files.readString(path);
        } catch (IOException e) {
            content = "{}";
            e.printStackTrace();
            Logger.getGlobal().warning("[GBMCCore - JsonConfiguration] [%s] Failed to read file %s".formatted(configName, file.toString()));
        }

        return JsonParser.parseString(content).getAsJsonObject();
    }

}
