package me.kevsal.minecraft.gbmc.core.common.util;

public interface GBMCEvent {

    /**
     * Called when the event is fired
     */
    public void onEvent(Object... args);

}
