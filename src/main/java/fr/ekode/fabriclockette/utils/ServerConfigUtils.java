package fr.ekode.fabriclockette.utils;

import fr.ekode.fabriclockette.core.FileResourcesUtils;

import java.io.IOException;
import java.util.Objects;

public class ServerConfigUtils {

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
