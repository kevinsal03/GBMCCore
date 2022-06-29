package me.kevsal.minecraft.gbmc.core.common.msg;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.DeliverCallback;

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
            DeliverCallback deliverCallback = (consumerTag, delivery) -> {
                String message = new String(delivery.getBody(), StandardCharsets.UTF_8);
                RabbitMQManager.getInstance().passMessage(message);
            };
            channel.basicConsume(EXCHANGE_NAME, true, deliverCallback, consumerTag -> {});
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }


}
