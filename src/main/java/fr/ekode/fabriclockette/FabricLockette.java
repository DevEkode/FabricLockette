package fr.ekode.fabriclockette;

import fr.ekode.fabriclockette.core.Config;
import fr.ekode.fabriclockette.events.EventHooks;
import fr.ekode.fabriclockette.utils.FabricLogger;
import net.fabricmc.api.ModInitializer;

import java.io.IOException;

public class FabricLockette implements ModInitializer {
    /**
     * FabricMC init method.
     */
    @Override
    public void onInitialize() {
        FabricLogger.logInfo("FabricLockette mod by Ekode (ekode.fr)");
        // Register events and implementations
        try {
            Config.getInstance();
        } catch (IOException e) {
            e.printStackTrace();
        }

        EventHooks.register();

        FabricLogger.logInfo("Thank you connection_lost for the original concept !");
    }
}
