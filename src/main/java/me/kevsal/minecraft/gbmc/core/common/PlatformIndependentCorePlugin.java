package me.kevsal.minecraft.gbmc.core.common;

import java.io.File;

public interface PlatformIndependentCorePlugin {
    /***
     * The main plugin configuration.
     * @return the platform specific configuration as a File
     */
    File platformSpecificConfiguration();

    /***
     * Disable the plugin
     */
    void disablePlugin();

    /***
     * Disable the plugin with a reason.
     * @param reason The reason the plugin is being disabled. Logged for debugging.
     */
    void disablePlugin(String reason);

    /***
     * Disable the plugin with a reason and with a clas
     * @param reason The reason the plugin is being disabled. Logged for debugging.
     * @param disabler The class of the object disabling the plugin. Logged for debugging.
     */
     void disablePlugin(String reason, Class disabler);
}
