package at.ac.tuwien.sepm.groupphase.backend.service.implementation;

import at.ac.tuwien.sepm.groupphase.backend.datatype.EventType;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.event.EventDTO;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.event.EventTicketsDTO;
import at.ac.tuwien.sepm.groupphase.backend.entity.Event;
import at.ac.tuwien.sepm.groupphase.backend.entity.EventTickets;
import at.ac.tuwien.sepm.groupphase.backend.entity.mapper.event.EventMapper;
import at.ac.tuwien.sepm.groupphase.backend.exception.ServiceException;
import at.ac.tuwien.sepm.groupphase.backend.repository.EventRepository;
import at.ac.tuwien.sepm.groupphase.backend.service.EventService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.orm.jpa.JpaSystemException;
import org.springframework.stereotype.Service;

import javax.persistence.PersistenceException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Service
public class EventServiceImpl implements EventService {

    private final EventRepository eventRepository;
    private final EventMapper eventMapper;
    private final Logger LOGGER = LoggerFactory.getLogger(getClass());

    public EventServiceImpl(EventRepository eventRepository, EventMapper eventMapper) {
        this.eventRepository = eventRepository;
        this.eventMapper = eventMapper;
    }

    @Override
    public List<EventTicketsDTO> findTopTenEvents(Set<String> monthsSet, Set<EventType> categoriesSet) throws ServiceException {
        LOGGER.info("Find top 10 events");
        ArrayList<EventTicketsDTO> toReturn = new ArrayList<>();
        try {
            for (Object[] o : eventRepository.findTopTenEvents(monthsSet, categoriesSet)) {
                toReturn.add(new EventTicketsDTO((String) o[0], (Long) o[1]));
            }
        } catch (PersistenceException e) {
            throw new ServiceException(e.getMessage());
        }
        return toReturn;
    }

    @Override
    public List<EventDTO> findAll() throws ServiceException {
        LOGGER.info("Find all events");
        List<EventDTO> toReturn = new ArrayList<>();
        try {
            for (Event event: eventRepository.findAllByOrderByNameAsc()) {
                toReturn.add(eventMapper.eventToEventDTO(event));
            }
        } catch (PersistenceException e) {
            throw new ServiceException(e.getMessage());
        }
        return toReturn;
    }

}
