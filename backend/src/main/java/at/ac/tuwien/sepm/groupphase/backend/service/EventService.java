package at.ac.tuwien.sepm.groupphase.backend.service;

import at.ac.tuwien.sepm.groupphase.backend.datatype.EventType;
import at.ac.tuwien.sepm.groupphase.backend.entity.EventTickets;

import java.util.List;
import java.util.Set;

public interface EventService {

    /**
     * Get top 10 events from backend
     *
     * @param monthsSet a list with the months by which to filter (either 1 entry or 12, if no month is specified)
     * @param categoriesSet a list with the categories by which to filter
     * @return top 10 events
     */
    List<EventTickets> findTopTenEvents(Set<String> monthsSet, Set<EventType> categoriesSet);

}