package at.ac.tuwien.sepm.groupphase.backend.repository;

import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.searchParameters.EventSearchParametersDTO;
import at.ac.tuwien.sepm.groupphase.backend.entity.Event;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface EventRepositoryCustom {

    /**
     * Find all events filtered by some parameters
     *
     * @param parameters A DTO of parameters that can be searched for (name, content, duration)
     * @param page the pageRequest (size and number of the page)
     * @return a page of events ordered by Name ascending
     */
    Page<Event> findAllEventsFiltered(EventSearchParametersDTO parameters, Pageable page);
}
