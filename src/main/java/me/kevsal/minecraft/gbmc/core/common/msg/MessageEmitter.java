package me.kevsal.minecraft.gbmc.core.common.msg;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

public class MessageEmitter {

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
     * Create a new instance of the MessageEmitter class, and setup all functionality required
     * @param connection The RabbitMQ connection
     * @param channel The RabbitMQ channel
     */
    public MessageEmitter(Connection connection, Channel channel) {
        this.connection = connection;
        this.channel = channel;
    }

    /***
     * Send a message to the RabbitMQ exchange
     * @param message The message to send
     */
    public void sendMessage(String message) {
        try {
            channel.basicPublish(EXCHANGE_NAME, "", null, message.getBytes(StandardCharsets.UTF_8));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

}