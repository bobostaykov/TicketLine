package at.ac.tuwien.sepm.groupphase.backend.service.implementation;

import at.ac.tuwien.sepm.groupphase.backend.datatype.EventType;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.event.EventDTO;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.event.EventTicketsDTO;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.searchParameters.EventSearchParametersDTO;
import at.ac.tuwien.sepm.groupphase.backend.entity.EventTickets;
import at.ac.tuwien.sepm.groupphase.backend.entity.mapper.event.EventMapper;
import at.ac.tuwien.sepm.groupphase.backend.entity.mapper.event.EventTicketsMapper;
import at.ac.tuwien.sepm.groupphase.backend.repository.EventRepository;
import at.ac.tuwien.sepm.groupphase.backend.service.EventService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import java.util.ArrayList;

import java.util.List;
import java.util.Set;

@Service
public class EventServiceImpl implements EventService {


    private final EventRepository eventRepository;
    private final EventMapper eventMapper;
    private final EventTicketsMapper eventTicketsMapper;
    private final Logger LOGGER = LoggerFactory.getLogger(getClass());


    public EventServiceImpl(EventRepository eventRepository, EventMapper eventMapper, EventTicketsMapper eventTicketsMapper) {
        this.eventRepository = eventRepository;
        this.eventMapper = eventMapper;
        this.eventTicketsMapper = eventTicketsMapper;
    }


    @Override
    public List<EventTicketsDTO> findTopTenEvents(Set<String> monthsSet, Set<EventType> categoriesSet) {
        LOGGER.info("Event Service: findTopTenEvents");
        ArrayList<EventTicketsDTO> toReturn = new ArrayList<>();
        for (Object[] o: eventRepository.findTopTenEvents(monthsSet, categoriesSet)) {
            toReturn.add(eventTicketsMapper.eventTicketsToEventTicketsDTO(new EventTickets((String)o[0], (Long)o[1])));
        //toReturn.add(new EventTicketsDTO((String)o[0], (Long)o[1]));
        }
        return toReturn;
    }

    @Override
    public List<EventDTO> findAll() {
        LOGGER.info("Event Service: findAll");
        return eventMapper.eventToEventDTO(eventRepository.findAllByOrderByNameAsc());
    }
    @Override
    public List<EventDTO> findAllFiltered(EventSearchParametersDTO parameters) {
        return (eventMapper.eventToEventDTO(eventRepository.findAllEventsFiltered(parameters)));
    }

    @Override
    public List<EventDTO> findEventsFilteredByArtistID(Long id) {
        LOGGER.info("Event Service: findEventsFilteredByArtistID");
        return eventMapper.eventToEventDTO(eventRepository.findAllByArtist_Id(id));
    }


}
