package fr.ekode.fabriclockette.utils;

import fr.ekode.fabriclockette.core.FileResourcesUtils;

import java.io.IOException;
import java.util.Objects;

public final class ServerConfigUtils {

    private ServerConfigUtils() {

    }

    /**
     * Check the current value of online-mode in server configuration.
     * @return the current value.
     */
    public static boolean isOnline() {
        String online = null;
        try {
            online = FileResourcesUtils.readPropertiesFile("server.properties").getProperty("online-mode");
        } catch (IOException e) {
            FabricLogger.logError("Cannot retrieve online-mode from server.properties");
        }
        return Objects.equals(online, "true");
    }
}
