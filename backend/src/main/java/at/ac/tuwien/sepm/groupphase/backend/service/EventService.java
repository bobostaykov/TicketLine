package at.ac.tuwien.sepm.groupphase.backend.service;

import at.ac.tuwien.sepm.groupphase.backend.entity.Event;

import java.util.List;

public interface EventService {

    /**
     * Get top 10 events from backend
     *
     * @return top 10 events
     */
    List<Event> findTopTenEvents();

}
