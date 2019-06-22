package at.ac.tuwien.sepm.groupphase.backend.entity.mapper.ticket;

import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.ticket.TicketDTO;
import at.ac.tuwien.sepm.groupphase.backend.entity.Ticket;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface TicketMapper {

    TicketDTO ticketToTicketDTO(Ticket ticket);

    Ticket ticketDTOToTicket(TicketDTO ticketDTO);

    List<TicketDTO> ticketToTicketDTO(List<Ticket> all);

    List<Ticket> ticketDTOToTicket(List<TicketDTO> all);

}
