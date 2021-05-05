package fr.ekode.fabriclockette;

import fr.ekode.fabriclockette.events.*;
import fr.ekode.fabriclockette.utils.FabricLogger;
import net.fabricmc.api.ModInitializer;

import java.util.logging.Logger;

public class FabricLockette implements ModInitializer {

    EventRepository eventRepository;

    @Override
    public void onInitialize() {

        FabricLogger.logInfo("FabricLockette mod by Ekode (ekode.fr)");
        
        // Register events and implementations
        eventRepository = new EventRepository();
        eventRepository.registerEvents();

        FabricLogger.logInfo("Thank you connection_lost for the original concept !");

    }
}
