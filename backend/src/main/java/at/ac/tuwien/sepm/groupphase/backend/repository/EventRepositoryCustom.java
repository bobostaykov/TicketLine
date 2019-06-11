package at.ac.tuwien.sepm.groupphase.backend.repository;

import at.ac.tuwien.sepm.groupphase.backend.datatype.EventType;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.searchParameters.EventSearchParametersDTO;
import at.ac.tuwien.sepm.groupphase.backend.entity.Event;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.Set;

public interface EventRepositoryCustom {

    /**
     * Find all events filtered by some parameters
     *
     * @param parameters A DTO of parameters that can be searched for (name, content, duration)
     * @param page the number of the requested page
     * @return a page of events ordered by Name ascending
     */
    Page<Event> findAllEventsFiltered(EventSearchParametersDTO parameters, Integer page);

    List<Object> findTopTenEvents2 (Set<String> monthsSet, Set<EventType> categoriesSet);
}
