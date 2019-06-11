package at.ac.tuwien.sepm.groupphase.backend.entity.mapper.event;

import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.event.EventDTO;
import at.ac.tuwien.sepm.groupphase.backend.entity.Event;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface EventMapper {

    Event eventDTOToEvent(EventDTO eventDTO);

    EventDTO eventToEventDTO(Event event);

    List<EventDTO> eventToEventDTO(List<Event> events);
}
