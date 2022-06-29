package me.kevsal.minecraft.gbmc.core.bungee.util;

import me.kevsal.minecraft.gbmc.core.bungee.CorePlugin;
import me.kevsal.minecraft.gbmc.core.common.PlatformIndependentLogger;

import java.util.logging.Level;

public class LoggerWrapper implements PlatformIndependentLogger {

    @Override
    public void log(String message) {
        CorePlugin.getInstance().getLogger().log(Level.ALL, message);
    }

    @Override
    public void warn(String message) {
        CorePlugin.getInstance().getLogger().warning(message);
    }

    @Override
    public void info(String message) {
        CorePlugin.getInstance().getLogger().info(message);
    }

    @Override
    public void debug(String message) {
        CorePlugin.getInstance().getLogger().log(Level.FINE, "[DEBUG] " + message);
    }
}
