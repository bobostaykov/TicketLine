package at.ac.tuwien.sepm.groupphase.backend.service;

import at.ac.tuwien.sepm.groupphase.backend.datatype.EventType;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.event.EventDTO;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.event.EventTicketsDTO;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.searchParameters.EventSearchParametersDTO;
import at.ac.tuwien.sepm.groupphase.backend.entity.EventTickets;
import at.ac.tuwien.sepm.groupphase.backend.exception.ServiceException;
import org.springframework.data.domain.Page;

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
    List<EventTicketsDTO> findTopTenEvents(Set<String> monthsSet, Set<EventType> categoriesSet) throws ServiceException;

    /**
     *
     * @param page the number of the specific requested page
     * @return a page of all EventDTOs
     * @throws ServiceException is thrown if something went wrong in the process
     */
    Page<EventDTO> findAll(Integer page) throws ServiceException;

    /**
     * Find all events filtered by eventName, eventType, content, description, duration, artistName
     *
     * @param parameters object containing eventName, eventType, content, description, duration, artistName
     * @param page the number of the specific requested page
     * @return a page of the found events
     * @throws ServiceException is thrown if something went wrong in the process
     */
    Page<EventDTO> findAllFiltered(EventSearchParametersDTO parameters, Integer page) throws ServiceException;

    /**
     * Find all events filtered by artist id
     *
     * @param id of the artist to filter by
     * @param page the number of the specific requested page
     * @return a page of the found events
     * @throws ServiceException is thrown if something went wrong in the process
     */
    Page<EventDTO> findEventsFilteredByArtistID(Long id, Integer page) throws ServiceException;
}
