package me.kevsal.minecraft.gbmc.core.common.msg;

import lombok.Getter;
import me.kevsal.minecraft.gbmc.core.common.Core;

import java.util.concurrent.CountDownLatch;

public class RabbitMQManagerThread extends Thread {

    @Getter
    protected boolean isReady = false;

    @Getter
    private final CountDownLatch latch = new CountDownLatch(1);

    public void run() {
        Core.getInstance().getLogger().info("Starting RabbitMQManagerThread");
        try {
            RabbitMQManager.init();
            latch.countDown();
        } catch (Exception e) {
            Core.getInstance().getLogger().warn("Failed to start RabbitMQ messaging system!");
            e.printStackTrace();
        }
    }

}
