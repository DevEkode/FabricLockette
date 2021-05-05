package fr.ekode.fabriclockette.utils;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


public class FabricLogger {
    private static final Logger LOGGER = LogManager.getLogger();

    public static void logError(String error){
        LOGGER.error("[FabricLockette] " + error);
    }

    public static void logInfo(String info){
        LOGGER.info("[FabricLockette] " + info);
    }
}
