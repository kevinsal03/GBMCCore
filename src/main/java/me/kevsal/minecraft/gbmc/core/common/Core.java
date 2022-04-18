package me.kevsal.minecraft.gbmc.core.common;

import lombok.Getter;

public class Core {

    @Getter
    public static Core instance;

    /***
     * Create a new instance of the Core class, and setup all functionality required
     */
    public Core() {
        if(!setInstance()) {
            throw new RuntimeException("An instance of Common Core already exists!");
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
