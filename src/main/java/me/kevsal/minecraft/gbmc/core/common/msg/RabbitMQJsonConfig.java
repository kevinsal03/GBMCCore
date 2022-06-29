package me.kevsal.minecraft.gbmc.core.common.msg;

import me.kevsal.minecraft.gbmc.core.common.util.JsonConfiguration;

import java.io.File;

/**
 * RabbitMQ Json configuration class
 */
public class RabbitMQJsonConfig extends JsonConfiguration {

    /***
     * Create the RabbitMQ Json Config Java object and setup all functionality required
     */
    public RabbitMQJsonConfig(File dataFolder) {
        super("RabbitMQ", new File(dataFolder, "rabbitmq-config.json"));
    }

    /***
     * Get the RabbitMQ hostname
     * @return The RabbitMQ hostname
     */
    public String getHost() {
        String host = getJsonObject().get("host").getAsString();
        if (host == null) {
            host = "localhost";
        }
        return host;
    }

    /***
     * Get the RabbitMQ port
     * @return The RabbitMQ port
     */
    public int getPort() {
        int port = getJsonObject().get("port").getAsInt();
        if (port == 0 || port < 0) {
            port = 5672;
        }
        return port;
    }

    /***
     * Get the RabbitMQ username
     * @return The RabbitMQ username
     */
    public String getUsername() {
        String username = getJsonObject().get("username").getAsString();
        if (username == null) {
            username = "guest";
        }
        return username;
    }

    /***
     * Get the RabbitMQ password
     * @return The RabbitMQ password
     */
    public String getPassword() {
        String password = getJsonObject().get("password").getAsString();
        if (password == null) {
            password = "guest";
        }
        return password;
    }

    /***
     * Get the RabbitMQ virtual host
     * @return The RabbitMQ virtual host
     */
    public String getVirtualHost() {
        String virtualHost = getJsonObject().get("vhost").getAsString();
        if (virtualHost == null) {
            virtualHost = "/";
        }
        return virtualHost;
    }

    /***
     * Debugging is enabled
     * @return true if debugging is enabled, false if not
     */
    public boolean debug() {
        return getJsonObject().get("debug").getAsBoolean();
    }
}
