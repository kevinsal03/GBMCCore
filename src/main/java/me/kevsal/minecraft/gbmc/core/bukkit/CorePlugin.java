package me.kevsal.minecraft.gbmc.core.bukkit;

import lombok.Getter;
import me.kevsal.minecraft.gbmc.core.bukkit.util.LoggerWrapper;
import me.kevsal.minecraft.gbmc.core.common.Core;
import me.kevsal.minecraft.gbmc.core.common.PlatformIndependentCorePlugin;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;

public class CorePlugin extends JavaPlugin implements PlatformIndependentCorePlugin {

    @Getter
    private static CorePlugin instance;
    @Getter
    private Core coreInstance;

    @Getter
    private final LoggerWrapper wrappedLogger = new LoggerWrapper();

    @Override
    public void onEnable() {
        instance = this;
        coreInstance = new Core(this);
        super.onEnable();
    }

    @Override
    public void onDisable() {
        instance = null;
        coreInstance.destroy();
        super.onDisable();
    }


    @Override
    public File platformSpecificDataFolder() {
        return getInstance().getDataFolder();
    }

    @Override
    public void disablePlugin() {
        disablePlugin("unknown", null);
    }

    @Override
    public void disablePlugin(String reason) {
        disablePlugin(reason, null);
    }

    @Override
    public void disablePlugin(String reason, Class disabler) {
        String disablerClassName = "unknown class";

        if (disabler != null) {
            disablerClassName = disabler.getName();
        }

        getLogger().info("Plugin disable requested by %s for reason %s".formatted(disablerClassName, reason));
        getLogger().warning("This usually means something has gone wrong, issues **WILL** occur.");
        getInstance().setEnabled(false);
        getInstance().onDisable();

    }
}
