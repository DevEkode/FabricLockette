package fr.ekode.fabriclockette;

import fr.ekode.fabriclockette.events.*;
import net.fabricmc.api.ModInitializer;

public class FabricLockette implements ModInitializer {

    EventRepository eventRepository;

    @Override
    public void onInitialize() {

        // Register events and implementations
        eventRepository = new EventRepository();
        eventRepository.registerEvents();

    }
}
