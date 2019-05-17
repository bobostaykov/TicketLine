package at.ac.tuwien.sepm.groupphase.backend.service.implementation;

import at.ac.tuwien.sepm.groupphase.backend.datatype.EventType;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.event.EventTicketsDTO;
import at.ac.tuwien.sepm.groupphase.backend.repository.EventRepository;
import at.ac.tuwien.sepm.groupphase.backend.service.EventService;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Service
public class EventServiceImpl implements EventService {

    private final EventRepository eventRepository;

    public EventServiceImpl(EventRepository eventRepository) {
        this.eventRepository = eventRepository;
    }

    @Override
    public List<EventTicketsDTO> findTopTenEvents(Set<String> monthsSet, Set<EventType> categoriesSet) {
        ArrayList<EventTicketsDTO> eventTicketsDTOList = new ArrayList<>();
        for (Object[] o: eventRepository.findTopTenEvents(monthsSet, categoriesSet)) {
            eventTicketsDTOList.add(new EventTicketsDTO((String)o[0], (Long)o[1]));
        }
        return eventTicketsDTOList;
    }

}
