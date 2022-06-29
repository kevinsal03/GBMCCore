package me.kevsal.minecraft.gbmc.core.common;

/***
 * Core platform-independent logger provider
 */
public interface PlatformIndependentLogger {
    /***
     * Log a message to the console.
     * @param message The message to log.
     */
    public void log(String message);

    /***
     * Log a message to the console with a warning.
     * @param message The message to log.
     */
    public void warn(String message);

    /***
     * Log a message to the console with as information.
     * @param message The message to log.
     */
    public void info(String message);

    /***
     * Log a message to the console with a debug message.
     * @param message The message to log.
     */
    public void debug(String message);
}
