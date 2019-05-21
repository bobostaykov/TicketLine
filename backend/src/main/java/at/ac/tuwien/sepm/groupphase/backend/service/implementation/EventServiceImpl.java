package at.ac.tuwien.sepm.groupphase.backend.service.implementation;

import at.ac.tuwien.sepm.groupphase.backend.datatype.EventType;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.event.EventDTO;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.event.EventTicketsDTO;
import at.ac.tuwien.sepm.groupphase.backend.entity.Event;
import at.ac.tuwien.sepm.groupphase.backend.entity.EventTickets;
import at.ac.tuwien.sepm.groupphase.backend.entity.mapper.event.EventMapper;
import at.ac.tuwien.sepm.groupphase.backend.entity.mapper.event.EventTicketsMapper;
import at.ac.tuwien.sepm.groupphase.backend.repository.EventRepository;
import at.ac.tuwien.sepm.groupphase.backend.service.EventService;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Service
public class EventServiceImpl implements EventService {

    private final EventRepository eventRepository;
    private final EventMapper eventMapper;
    private final EventTicketsMapper eventTicketsMapper;

    public EventServiceImpl(EventRepository eventRepository, EventMapper eventMapper, EventTicketsMapper eventTicketsMapper) {
        this.eventRepository = eventRepository;
        this.eventMapper = eventMapper;
        this.eventTicketsMapper = eventTicketsMapper;
    }

    @Override
    public List<EventTicketsDTO> findTopTenEvents(Set<String> monthsSet, Set<EventType> categoriesSet) {
        ArrayList<EventTicketsDTO> toReturn = new ArrayList<>();
        for (Object[] o: eventRepository.findTopTenEvents(monthsSet, categoriesSet)) {
            toReturn.add(eventTicketsMapper.eventTicketsToEventTicketsDTO(new EventTickets((String)o[0], (Long)o[1])));
        }
        return toReturn;
    }

    @Override
    public List<EventDTO> findAll() {
        List<EventDTO> toReturn = new ArrayList<>();
        for (Event event: eventRepository.findAllByOrderByNameAsc()) {
            toReturn.add(eventMapper.eventToEventDTO(event));
        }
        return toReturn;
    }

}
