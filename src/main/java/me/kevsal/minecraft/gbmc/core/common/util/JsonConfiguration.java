package me.kevsal.minecraft.gbmc.core.common.util;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import lombok.Getter;
import me.kevsal.minecraft.gbmc.core.common.Core;

import java.io.File;
import java.io.IOException;
import java.nio.file.*;
import java.util.Objects;
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

        // Copy the original file from resources to the disk if it doesn't exist
        try {
            // Do not overwrite
            Files.copy(Objects.requireNonNull(getClass().getResourceAsStream("/%s".formatted(configName))), file.toPath());
            Core.getInstance().getLogger().info("Copied default config %s to %s".formatted(configName, file.getAbsolutePath()));
        } catch (FileAlreadyExistsException ignored) {
            // File already exists, do nothing
        } catch (IOException e) {
            e.printStackTrace();
            Core.getInstance().getLogger().warn("Failed to copy default configuration file to %s".formatted(file.getAbsolutePath()));
            Core.getInstance().getLogger().warn("Please make sure you have the correct permissions to write to the directory");
            Core.getInstance().getLogger().warn("If default values are provided by the configuration manager, they will be used. Expect issues!");
        }

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
