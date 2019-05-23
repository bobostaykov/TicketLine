package at.ac.tuwien.sepm.groupphase.backend.repository;

import at.ac.tuwien.sepm.groupphase.backend.datatype.EventType;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.searchParameters.EventSearchParametersDTO;
import at.ac.tuwien.sepm.groupphase.backend.entity.Event;

import java.util.List;
import java.util.Set;

public interface EventRepositoryCustom {

    /**
     *
     * @param parameters A DTO of parameters that can be searched for (name, content, duration)
     * @return A list of Event ordered by Name ascending
     */
    List<Event> findAllEventsFiltered(EventSearchParametersDTO parameters);

    List<Object> findTopTenEvents2 (Set<String> monthsSet, Set<EventType> categoriesSet);
}
