package fr.ekode.fabriclockette.utils;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


public final class FabricLogger {

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
        LOGGER.error("[FabricLockette] " + error);
    }

    /**
     * Print info log.
     * @param info info message
     */
    public static void logInfo(final String info) {
        LOGGER.info("[FabricLockette] " + info);
    }
}
