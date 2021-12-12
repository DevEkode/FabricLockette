package fr.ekode.fabriclockette.events;

import java.util.ArrayList;
import java.util.List;

/**
 * Class to handle every EventRegistrator classes.
 */
public class EventRepository {

    /**
     * List of events to register.
     */
    private List<EventRegistrator> registratorList;

    /**
     * Constructor of EventRepository.
     */
    public EventRepository() {
        this.registratorList = new ArrayList<>();

        // Register into list
        this.registratorList.add(new AttackBlock());
        this.registratorList.add(new CloseSignGui());
        this.registratorList.add(new OpenSignGui());
        this.registratorList.add(new ContainerOpen());
    }

    /**
     * Register every event in registratorList.
     */
    public void registerEvents() {
        for (EventRegistrator event : this.registratorList) {
            event.register();
        }
    }
}
