package me.kevsal.minecraft.gbmc.core.common;

import lombok.Getter;
import me.kevsal.minecraft.gbmc.core.common.msg.RabbitMQManager;

import java.io.File;

/***
 * Core platform-independent functionality of the plugin
 */
public class Core {

    @Getter
    private static Core instance;

    /***
     * A platform-independent way of accessing the platform-specific plugin
     */
    @Getter
    private final PlatformIndependentCorePlugin pluginInstance;

    @Getter
    private final PlatformIndependentLogger logger;

    /***
     * Create a new instance of the Core class, and setup all functionality required
     */
    public Core(PlatformIndependentCorePlugin pluginInstance) {
        if(!setInstance()) {
            throw new RuntimeException("An instance of Common Core already exists!");
        }
        this.pluginInstance = pluginInstance;

        // Get the logger from the underlying native plugin
        logger = getPluginInstance().getWrappedLogger();

        // Ensure that the config directory exists
        ensureFolderExists(getPluginInstance().platformSpecificDataFolder());

        // Startup the RabbitMQ Messaging system
        try {
            RabbitMQManager.init();
        } catch (Exception e) {
            getLogger().warn("Failed to start RabbitMQ messaging system!");
            e.printStackTrace();
        }
    }

    /***
     * Prepare the core class for shutdown.
     */
    public void destroy() {
        // Add whatever else needs to be null-ed out or shutdown to properly shut the plugin down

        // Shutdown the RabbitMQ Messaging system
        RabbitMQManager.getInstance().destroy();


        // FINALLY, null out the instance
        instance = null;
    }

    /***
     * Ensure that the given folder exists. If it doesn't, create it.
     * @param directory The folder to ensure exists
     */
    public void ensureFolderExists(File directory) {
        if(!directory.exists()) {
            directory.mkdirs();
        }
    }

    /***
     * Set and ensure there is one instance of the Core class.
     * @return true if a new instance, false if an instance already exists.
     */
    private boolean setInstance() {
        if (instance == null) {
            instance = this;
            return true;
        } else {
            return false;
        }
    }

}
