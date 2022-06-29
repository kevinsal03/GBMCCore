package me.kevsal.minecraft.gbmc.core.common.msg;

import me.kevsal.minecraft.gbmc.core.common.Core;
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
        try {
            return getJsonObject().get("host").getAsString();
        } catch (Exception e) {
            Core.getInstance().getLogger().warn("RabbitMQ hostname not found in config file! Using default hostname: localhost");
            return "localhost";
        }
    }

    /***
     * Get the RabbitMQ port
     * @return The RabbitMQ port
     */
    public int getPort() {
        try {
            return getJsonObject().get("port").getAsInt();
        } catch (Exception e) {
            Core.getInstance().getLogger().warn("RabbitMQ port not found in config file! Using default port: 5672");
            return 5672;
        }
    }

    /***
     * Get the RabbitMQ username
     * @return The RabbitMQ username
     */
    public String getUsername() {
        try{
            return getJsonObject().get("username").getAsString();
        } catch (Exception e) {
            Core.getInstance().getLogger().warn("RabbitMQ username not found in config file! Using default username: guest");
            return "guest";
        }
    }

    /***
     * Get the RabbitMQ password
     * @return The RabbitMQ password
     */
    public String getPassword() {
        try {
            return getJsonObject().get("password").getAsString();
        } catch (Exception e) {
            Core.getInstance().getLogger().warn("RabbitMQ password not found in config file! Using default password: guest");
            return "guest";
        }
    }

    /***
     * Get the RabbitMQ virtual host
     * @return The RabbitMQ virtual host
     */
    public String getVirtualHost() {
        try {
            return getJsonObject().get("vhost").getAsString();
        } catch (Exception e) {
            Core.getInstance().getLogger().warn("RabbitMQ virtual host not found in config file! Using default virtual host: /");
            return "/";
        }
    }

    /***
     * Debugging is enabled
     * @return true if debugging is enabled, false if not
     */
    public boolean debug() {
        try {
            return getJsonObject().get("debug").getAsBoolean();
        } catch (Exception e) {
            Core.getInstance().getLogger().warn("RabbitMQ debug not found in config file! Debugging is enabled as the configuration is likely corrupt.");
            return true;
        }

    }
}
