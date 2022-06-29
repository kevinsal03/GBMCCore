package me.kevsal.minecraft.gbmc.core.common.msg;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import lombok.Getter;
import me.kevsal.minecraft.gbmc.core.common.Core;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class RabbitMQManager {

    /***
     * The RabbitMQ connection manager
     */
    @Getter
    private static RabbitMQManager instance;
    /***
     * A platform-independent way of accessing the platform-specific data folder
     */
    @Getter
    private final File dataFolder = Core.getInstance().getPluginInstance().platformSpecificDataFolder();
    /***
     * Json configuration for RabbitMQ connection
     */
    @Getter
    private RabbitMQJsonConfig rabbitMQConfig = new RabbitMQJsonConfig(dataFolder);

    /***
     * A RabbitMQ connection
     */
    @Getter
    protected Connection connection;
    /***
     * A RabbitMQ channel
     */
    @Getter
    protected Channel channel;

    /***
     * RabbitMQ message emitter
     */
    @Getter
    protected MessageEmitter messageEmitter;

    /***
     * RabbitMQ message receiver
     */
    @Getter
    protected MessageReceiver messageReceiver;

    /***
     * The name of the RabbitMQ exchange to be used by all classes created by this class
     */
    public static final String EXCHANGE_NAME = "gbmc";

    private static final ArrayList<MessageReceiveEvent> messageHandlers = new ArrayList<MessageReceiveEvent>();

    /***
     * Create a new instance of the RabbitMQManager class, and setup all functionality required
     * Ensures that only one instance exists
     */
    public RabbitMQManager() {
        // Set the instance
        if (instance != null) {
            instance = this;
        } else {
            throw new RuntimeException("An instance of RabbitMQManager already exists!");
        }

        // Connect to RabbitMQ
        createRabbitMQChannel();
        // Create the RabbitMQ channel
        createRabbitMQChannel();

        // Create the exchange
        createExchange();

        // Create the message emitter
        messageEmitter = new MessageEmitter(connection, channel);
    }

    /***
     * Initialize the RabbitMQ Manager
     */
    public static void init() {
        new RabbitMQManager();
    }

    /***
     * Destroy the RabbitMQManager instance
     */
    public void destroy() {
        // Close the RabbitMQ channel
        closeRabbitMQChannel();
        // Close the RabbitMQ connection
        closeRabbitMQConnection();
        // Null the instance
        instance = null;
        // Get rid of the message handlers
        messageHandlers.clear();
        // Get rid of the message emitter
        messageEmitter = null;
        // Get rid of the message receiver
        messageReceiver = null;
        // Get rid of the RabbitMQ config
        rabbitMQConfig = null;
    }

    /***
     * Create a new RabbitMQ connection
     * @return true if successful, false if not
     */
    private boolean createRabbitMQConnection() {
        // Create the connection
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost(rabbitMQConfig.getHost());
        factory.setPort(rabbitMQConfig.getPort());
        factory.setUsername(rabbitMQConfig.getUsername());
        factory.setPassword(rabbitMQConfig.getPassword());
        factory.setVirtualHost(rabbitMQConfig.getVirtualHost());
        try {
            connection = factory.newConnection("gbmc core version %s".formatted(Core.getInstance().getClass().getPackage().getImplementationVersion()));
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /***
     * Create a new RabbitMQ channel
     */
    private void createRabbitMQChannel() {
        // Create the channel
        try {
            channel = connection.createChannel();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /***
     * Create the RabbitMQ exchange, using the fanout format
     */
    private void createExchange() {
        try {
            channel.exchangeDeclare(EXCHANGE_NAME, "fanout");
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException("Failed to create the RabbitMQ exchange!");
        }
    }

    /***
     * Close the RabbitMQ channel
     */
    private void closeRabbitMQChannel() {
        // Close the channel
        try {
            channel.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /***
     * Close the RabbitMQ connection
     */
    private void closeRabbitMQConnection() {
        // Close the connection
        try {
            connection.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /***
     * Register a message handler
     * @param messageHandler The message handler to register
     */
    public void addMessageHandler(MessageReceiveEvent messageHandler) {
        messageHandlers.add(messageHandler);
    }

    /***
     * Unregister a message handler
     * @param messageHandler The message handler to unregister
     */
    public void removeMessageHandler(MessageReceiveEvent messageHandler) {
        messageHandlers.remove(messageHandler);
    }

    /***
     * Pass a message to all registered message handlers
     * @param message The message to pass
     */
    protected void passMessage(String message) {
        for (MessageReceiveEvent messageHandler : messageHandlers) {
            messageHandler.onEvent(message);
        }
    }

}
