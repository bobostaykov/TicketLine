package at.ac.tuwien.sepm.groupphase.backend.entity.mapper.event;

import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.event.EventTicketsDTO;
import at.ac.tuwien.sepm.groupphase.backend.entity.EventTickets;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface EventTicketsMapper {

    /**
     * Maps the EventTicket object to EventTicketDTO object
     * @param eventTickets to map
     * @return the mapped EventTicketDTO object
     */
    EventTicketsDTO eventTicketsToEventTicketsDTO(EventTickets eventTickets);
}
