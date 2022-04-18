package me.kevsal.minecraft.gbmc.core.bungee;

import lombok.Getter;
import me.kevsal.minecraft.gbmc.core.common.Core;
import me.kevsal.minecraft.gbmc.core.common.PlatformIndependentCorePlugin;
import net.md_5.bungee.api.plugin.Plugin;

import java.io.File;

public class CorePlugin extends Plugin implements PlatformIndependentCorePlugin {

    @Getter
    private CorePlugin instance;
    @Getter
    private Core coreInstance;

    @Override
    public void onEnable(){
        coreInstance = new Core(getInstance());
        instance = this;
        super.onEnable();
    }

    @Override
    public void onDisable(){
        getProxy().getPluginManager().unregisterListeners(getInstance());
        getProxy().getPluginManager().unregisterCommands(getInstance());
        instance = null;
        super.onDisable();
    }


    @Override
    public File platformSpecificConfiguration() {
        return null;
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
        getInstance().onDisable();

    }

}
