package at.ac.tuwien.sepm.groupphase.backend.entity.mapper.event;

import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.event.EventDTO;
import at.ac.tuwien.sepm.groupphase.backend.entity.Event;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface EventMapper {

    /**
     * Maps the EventDTO object to Event object
     * @param eventDTO to map
     * @return the mapped Event object
     */
    Event eventDTOToEvent(EventDTO eventDTO);

    /**
     * Maps the Event object to EventDTO object
     * @param event to map
     * @return the mapped EventDTO object
     */
    EventDTO eventToEventDTO(Event event);
}
