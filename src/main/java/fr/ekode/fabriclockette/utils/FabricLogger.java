package fr.ekode.fabriclockette.utils;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


public final class FabricLogger {

    private static final String PREFIX = "[FabricLockette]";

    private FabricLogger() {

    }

    /**
     * Instance of FabricLogger.
     */
    private static final Logger LOGGER = LogManager.getLogger();

    /**
     * Print error log.
     * @param error error message
     */
    public static void logError(final String error) {
        LOGGER.log(Level.ERROR,PREFIX+" {}",error);
    }

    /**
     * Print info log.
     * @param info info message
     */
    public static void logInfo(final String info) {
        LOGGER.log(Level.INFO,PREFIX+" {}",info);
    }
}
