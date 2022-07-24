package me.kevsal.minecraft.gbmc.core.common.msg;

import lombok.Getter;
import me.kevsal.minecraft.gbmc.core.common.Core;

public class RabbitMQManagerThread extends Thread {

    @Getter
    protected boolean isReady = false;

    public void run() {
        System.out.println("Starting RabbitMQManagerThread");
        Core.getInstance().getLogger().info("Starting RabbitMQManagerThread");
        try {
            RabbitMQManager.init();
        } catch (Exception e) {
            Core.getInstance().getLogger().warn("Failed to start RabbitMQ messaging system!");
            e.printStackTrace();
        }
    }

}
