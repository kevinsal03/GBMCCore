package me.kevsal.minecraft.gbmc.core.common.msg;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.DeliverCallback;
import me.kevsal.minecraft.gbmc.core.common.Core;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

public class MessageReceiver {

    /***
     * The RabbitMQ connection
     */
    private final Connection connection;
    /***
     * The RabbitMQ channel
     */
    private final Channel channel;

    /***
     * The RabbitMQ Exchange name
     */
    public final String EXCHANGE_NAME = RabbitMQManager.EXCHANGE_NAME;

    /***
     * Create a new instance of the MessageReceiver class, and setup all functionality required
     * @param connection The RabbitMQ connection
     * @param channel The RabbitMQ channel
     */
    public MessageReceiver(Connection connection, Channel channel) {
        this.connection = connection;
        this.channel = channel;

        // Setup the message receiver
        setupMessageReceiver();
    }

    private void setupMessageReceiver() {
        try {
            String queueName = channel.queueDeclare().getQueue();
            channel.queueBind(queueName, EXCHANGE_NAME, "");
            DeliverCallback deliverCallback = (consumerTag, delivery) -> {
                String message = new String(delivery.getBody(), StandardCharsets.UTF_8);
                if (RabbitMQManager.getInstance().DEBUG) {
                    Core.getInstance().getLogger().info("[GBMC] MessageReceiver: Received Message: \"%s\"".formatted(message));
                }
                // Prevent receiving own messages
                if (message.startsWith(RabbitMQManager.getInstance().getInstanceId() + "|")) {
                    if (RabbitMQManager.getInstance().DEBUG) {
                        Core.getInstance().getLogger().info("[GBMC] MessageReceiver: Skipping own message \"%s\"".formatted(message));
                    }
                    return;
                }
                // Strip instance ID
                message = message.substring(message.indexOf("|") + 1);
                RabbitMQManager.getInstance().passMessage(message);
            };
            channel.basicConsume(queueName, true, deliverCallback, consumerTag -> {});
        } catch (IOException e) {
            e.printStackTrace();
            Core.getInstance().getLogger().warn("Failed to setup message receiver");
        }
    }


}
