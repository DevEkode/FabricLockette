package fr.ekode.fabriclockette;

import fr.ekode.fabriclockette.events.EventRepository;
import fr.ekode.fabriclockette.utils.FabricLogger;
import net.fabricmc.api.ModInitializer;

public class FabricLockette implements ModInitializer {

    /**
     * FabricMC init method
     */
    @Override
    public void onInitialize() {

        FabricLogger.logInfo("FabricLockette mod by Ekode (ekode.fr)");
        
        // Register events and implementations
        EventRepository eventRepository = new EventRepository();
        eventRepository.registerEvents();

        FabricLogger.logInfo("Thank you connection_lost for the original concept !");
    }
}
