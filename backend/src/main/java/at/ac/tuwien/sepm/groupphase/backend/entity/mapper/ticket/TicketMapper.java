package at.ac.tuwien.sepm.groupphase.backend.entity.mapper.ticket;

import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.ticket.TicketDTO;
import at.ac.tuwien.sepm.groupphase.backend.entity.Ticket;
import org.mapstruct.Mapper;
import org.springframework.data.domain.Page;

import java.util.List;

@Mapper(componentModel = "spring")
public interface TicketMapper {

    /**
     * Maps the Ticket object to TicketDTO object
     * @param ticket to map
     * @return the mapped TicketDTO object
     */
    TicketDTO ticketToTicketDTO(Ticket ticket);

    /**
     * Maps the TicketDTO object to Ticket object
     * @param ticketDTO to map
     * @return the mapped Ticket object
     */
    Ticket ticketDTOToTicket(TicketDTO ticketDTO);

    /**
     * Maps List<Ticket> to List<TicketDTO>
     * @param all to map
     * @return the mapped list of TicketDTO objects
     */
    List<TicketDTO> ticketToTicketDTO(List<Ticket> all);

    /**
     * Maps List<TicketDTO> to List<Ticket>
     * @param all to map
     * @return the mapped list of Ticket objects
     */
    List<Ticket> ticketDTOToTicket(List<TicketDTO> all);


}
