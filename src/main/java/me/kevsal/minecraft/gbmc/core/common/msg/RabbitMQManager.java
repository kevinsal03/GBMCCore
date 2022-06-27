package me.kevsal.minecraft.gbmc.core.common.msg;

import lombok.Getter;
import me.kevsal.minecraft.gbmc.core.common.Core;

import java.io.File;

public class RabbitMQManager {

    /***
     * The RabbitMQ connection manager
     */
    @Getter
    private static RabbitMQManager instance;
    /***
     * A platform-independent way of accessing the platform-specific data folder
     */
    @Getter
    private final File dataFolder = Core.getInstance().getPluginInstance().platformSpecificDataFolder();
    /***
     * Json configuration for RabbitMQ connection
     */
    @Getter
    private final RabbitMQJsonConfig rabbitMQConfig = new RabbitMQJsonConfig(dataFolder);

    /***
     * Create a new instance of the RabbitMQManager class, and setup all functionality required
     * Ensures that only one instance exists
     */
    public RabbitMQManager() {
        // Set the instance
        if (instance != null) {
            instance = this;
        } else {
            throw new RuntimeException("An instance of RabbitMQManager already exists!");
        }
    }

}
