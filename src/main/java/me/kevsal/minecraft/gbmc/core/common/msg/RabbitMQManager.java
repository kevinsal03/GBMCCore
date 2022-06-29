package me.kevsal.minecraft.gbmc.core.common.msg;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import lombok.Getter;
import me.kevsal.minecraft.gbmc.core.common.Core;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.UUID;

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

    // Fetch if this instance of RabitMQ manager should be in debug mode
    public final boolean DEBUG = rabbitMQConfig.debug();

    public final UUID instanceId = UUID.randomUUID();

    /***
     * Create a new instance of the RabbitMQManager class, and setup all functionality required
     * Ensures that only one instance exists
     */
    public RabbitMQManager() {
        // Set the instance
        if (instance == null) {
            instance = this;
        } else {
            throw new RuntimeException("An instance of RabbitMQManager already exists!");
        }

        if(DEBUG) {
            Core.getInstance().getLogger().info("[GBMC] RabbitMQManager: Initializing RabbitMQManager in DEBUG mode with ID: " + getInstance().instanceId);
        } else {
            Core.getInstance().getLogger().info("[GBMC] RabbitMQManager: Initializing RabbitMQManager with ID: " + getInstance().instanceId);
        }


        // Connect to RabbitMQ
        createRabbitMQConnection();
        // Create the RabbitMQ channel
        createRabbitMQChannel();

        // Create the exchange
        createExchange();

        // Create the message emitter
        messageEmitter = new MessageEmitter(connection, channel);

        // Create the message receiver
        messageReceiver = new MessageReceiver(connection, channel);

        // Send a HELLO message to the RabbitMQ exchange
        messageEmitter.sendMessage("HELLO this is %s".formatted(getInstance().instanceId.toString()));
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
        // Send a BYE message to the RabbitMQ exchange
        messageEmitter.sendMessage("This is %s. GOODBYE.".formatted(getInstance().instanceId.toString()));
        // Close the RabbitMQ channel
        closeRabbitMQChannel();
        // Close the RabbitMQ connection
        closeRabbitMQConnection();
        // Get rid of the message handlers
        messageHandlers.clear();
        // Get rid of the message emitter
        messageEmitter = null;
        // Get rid of the message receiver
        messageReceiver = null;
        // Get rid of the RabbitMQ config
        rabbitMQConfig = null;


        // FINALLY, null out the instance
        instance = null;
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
            if (getInstance().DEBUG) {
                Core.getInstance().getLogger().info("[GBMC] RabbitMQManager: Created RabbitMQ connection");
            }
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            Core.getInstance().getLogger().warn("[GBMC] RabbitMQManager: Failed to create RabbitMQ connection");
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
            if (getInstance().DEBUG) {
                Core.getInstance().getLogger().info("[GBMC] RabbitMQManager: Created RabbitMQ channel");
            }
        } catch (Exception e) {
            e.printStackTrace();
            Core.getInstance().getLogger().warn("[GBMC] RabbitMQManager: Failed to create RabbitMQ channel");
        }
    }

    /***
     * Create the RabbitMQ exchange, using the fanout format
     */
    private void createExchange() {
        try {
            channel.exchangeDeclare(EXCHANGE_NAME, "fanout");
            if (getInstance().DEBUG) {
                Core.getInstance().getLogger().info("[GBMC] RabbitMQManager: Created RabbitMQ exchange");
            }
        } catch (IOException e) {
            e.printStackTrace();
            Core.getInstance().getLogger().warn("[GBMC] RabbitMQManager: Failed to create RabbitMQ exchange");
        }
    }

    /***
     * Close the RabbitMQ channel
     */
    private void closeRabbitMQChannel() {
        // Close the channel
        try {
            channel.close();
            if (getInstance().DEBUG) {
                Core.getInstance().getLogger().info("[GBMC] RabbitMQManager: Closed RabbitMQ channel");
            }
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
            if (getInstance().DEBUG) {
                Core.getInstance().getLogger().info("[GBMC] RabbitMQManager: Closed RabbitMQ connection");
            }
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
        if (getInstance().DEBUG) {
            Core.getInstance().getLogger().info("[GBMC] RabbitMQManager: Registered message handler %s".formatted(messageHandler.getClass().getName()));
        }
    }

    /***
     * Unregister a message handler
     * @param messageHandler The message handler to unregister
     */
    public void removeMessageHandler(MessageReceiveEvent messageHandler) {
        messageHandlers.remove(messageHandler);
        if (getInstance().DEBUG) {
            Core.getInstance().getLogger().info("[GBMC] RabbitMQManager: Unregistered message handler %s".formatted(messageHandler.getClass().getName()));
        }
    }

    /***
     * Pass a message to all registered message handlers
     * @param message The message to pass
     */
    protected void passMessage(String message) {
        if (getInstance().DEBUG) {
            Core.getInstance().getLogger().info("[GBMC] RabbitMQManager: Passing message \"%s\" to message event handlers".formatted(message));
        }
        for (MessageReceiveEvent messageHandler : messageHandlers) {
            messageHandler.onEvent(message);
        }
    }

}
