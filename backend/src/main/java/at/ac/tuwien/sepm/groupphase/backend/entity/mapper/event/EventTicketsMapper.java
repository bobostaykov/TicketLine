package at.ac.tuwien.sepm.groupphase.backend.entity.mapper.event;

import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.event.EventTicketsDTO;
import at.ac.tuwien.sepm.groupphase.backend.entity.EventTickets;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface EventTicketsMapper {

    EventTickets eventTicketsDTOToEventTickets(EventTicketsDTO eventTicketsDTO);
    EventTicketsDTO eventTicketsToEventTicketsDTO(EventTickets eventTickets);

}
