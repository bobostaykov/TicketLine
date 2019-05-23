package at.ac.tuwien.sepm.groupphase.backend.service.implementation;

import at.ac.tuwien.sepm.groupphase.backend.datatype.EventType;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.event.EventDTO;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.event.EventTicketsDTO;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.searchParameters.EventSearchParametersDTO;
import at.ac.tuwien.sepm.groupphase.backend.entity.mapper.event.EventMapper;
import at.ac.tuwien.sepm.groupphase.backend.repository.EventRepository;
import at.ac.tuwien.sepm.groupphase.backend.service.EventService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;

@Service
public class EventServiceImpl implements EventService {


    private final EventMapper eventMapper;
    private final EventRepository eventRepository;

    public EventServiceImpl(EventMapper eventMapper, EventRepository eventRepository) {
        this.eventMapper = eventMapper;
        this.eventRepository = eventRepository;
    }

    @Override
    public List<EventTicketsDTO> findTopTenEvents(Set<String> monthsSet, Set<EventType> categoriesSet) {
        return null;
    }

    @Override
    public List<EventDTO> findAll() {
        return eventMapper.eventToEventDTO(eventRepository.findAll());
    }

    @Override
    public List<EventDTO> findAllFiltered(EventSearchParametersDTO parameters) {
        return (eventMapper.eventToEventDTO(eventRepository.findAllEventsFiltered(parameters)));
    }
}
/*
    @Override
    public List<EventTicketsDTO> findTopTenEvents(Set<String> monthsSet, Set<EventType> categoriesSet) {
        ArrayList<EventTicketsDTO> eventTicketsDTOList = new ArrayList<>();
        for (Object[] o: eventRepository.findTopTenEvents(monthsSet, categoriesSet)) {
            eventTicketsDTOList.add(new EventTicketsDTO((String)o[0], (Long)o[1]));
        }
        return eventTicketsDTOList;
    }

}

 */
