package at.ac.tuwien.sepm.groupphase.backend.service.ticketExpirationHandler;

import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.show.ShowDTO;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.ticket.TicketDTO;

import java.util.List;

public interface TicketExpirationHandler {

    /**
     * Searchs for expired reservated tickets and sets status to EXPIRED
     *
     * @param showDTO Show to check and set expired reservated tickets to expired
     */
    void setExpiredReservatedTicketsToStatusExpiredForSpecificShow(ShowDTO showDTO);

    /**
     * Check if reservated ticket is expired and sets status to EXPIRED
     *
     * @param ticket Ticket to check expiration status
     * @return Checked and maybe updated ticket
     */
    TicketDTO setExpiredReservedTicketsToStatusExpired(TicketDTO ticket);

    /**
     * Check if reservated tickets are expired and sets status to EXPIRED
     *
     * @param tickets List of tickets to check expiration status
     * @return Updated list of tickets
     */
    List<TicketDTO> setExpiredReservedTicketsToStatusExpired(List<TicketDTO> tickets);

    /**
     * Check if reservated tickets are expired and sets status to EXPIRED for all tickets
     *
     */
    void setAllExpiredReservedTicketsToStatusExpired();
}
