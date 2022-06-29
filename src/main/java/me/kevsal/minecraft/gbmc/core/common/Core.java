package me.kevsal.minecraft.gbmc.core.common;

import lombok.Getter;
import me.kevsal.minecraft.gbmc.core.common.msg.RabbitMQManager;

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

        // Startup the RabbitMQ Messaging system
        RabbitMQManager.init();
    }

    /***
     * Prepare the core class for shutdown.
     */
    public void destroy() {
        instance = null;
        // Add whatever else needs to be null-ed out or shutdown to properly shut the plugin down

        // Shutdown the RabbitMQ Messaging system
        RabbitMQManager.getInstance().destroy();
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
