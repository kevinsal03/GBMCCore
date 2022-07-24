package me.kevsal.minecraft.gbmc.core.common;

import lombok.Getter;
import me.kevsal.minecraft.gbmc.core.common.msg.RabbitMQManager;
import me.kevsal.minecraft.gbmc.core.common.msg.RabbitMQManagerThread;

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

    /***
     * A platform-independent way of accessing the platform-specific logger
     */
    @Getter
    private final PlatformIndependentLogger logger;

    /***
     * The RabbitMQ manager thread
     */
    public final RabbitMQManagerThread rabbitMQManagerThread = new RabbitMQManagerThread();


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

        // Startup the message manager thread
        rabbitMQManagerThread.start();

        /*
        // Startup the RabbitMQ Messaging system
        try {
            RabbitMQManager.init();
        } catch (Exception e) {
            getLogger().warn("Failed to start RabbitMQ messaging system!");
            e.printStackTrace();
        }*/


        // Wait for Async threads to be ready before letting the server continue loading
        int sleepTime = 300; //starts at checking every 300ms, maxes out at 5000ms
        while(!rabbitMQManagerThread.isReady()) {
            try {
                Core.getInstance().getLogger().info("Waiting for RabbitMQManagerThread to be ready... (" + sleepTime + "ms)");
                Thread.sleep(sleepTime); //TODO: Replace with a proper BlockingQueue setup or something
                sleepTime = sleepTime >= 5000 ? 5000 : (int) (sleepTime * 2); //Increase the sleep time if it's been too long
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
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
