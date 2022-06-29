package me.kevsal.minecraft.gbmc.core.common.msg;

import me.kevsal.minecraft.gbmc.core.common.util.GBMCEvent;

public interface MessageReceiveEvent extends GBMCEvent {
    public void onEvent(String message);

}
