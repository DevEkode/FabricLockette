package fr.ekode.fabriclockette;

import fr.ekode.fabriclockette.events.EventRepository;
import fr.ekode.fabriclockette.utils.FabricLogger;
import fr.ekode.fabriclockette.utils.ServerConfigUtils;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;
import net.fabricmc.fabric.api.event.server.ServerStartCallback;

public class FabricLockette implements ModInitializer {

    /**
     * FabricMC init method.
     */
    @Override
    public void onInitialize() {

        FabricLogger.logInfo("FabricLockette mod by Ekode (ekode.fr)");
        // Register events and implementations
        EventRepository eventRepository = new EventRepository();
        eventRepository.registerEvents();

        FabricLogger.logInfo("Thank you connection_lost for the original concept !");

        ServerLifecycleEvents.SERVER_STARTED.register(server -> {
            if (!ServerConfigUtils.isOnline()) {
                FabricLogger.logInfo("[ONLINE-MODE OFF] FabricLockette will not "
                        + "use UUID but usernames to lock containers !");
            }
        });

    }
}
