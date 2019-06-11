package at.ac.tuwien.sepm.groupphase.backend.service;

import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.ticket.TicketDTO;

import java.util.List;

public interface TicketService {

    /**
     * Save a single ticket entry
     *
     * @param ticket to be saved
     * @return saved ticket entry
     */
    TicketDTO postTicket(TicketDTO ticket);

    /**
     * Get all ticket entries
     *
     * @return list of all ticket entries
     */
    List<TicketDTO> findAll();

    /**
     * Get all ticket entries filtered by the corresponsing customer and/or event name
     *
     * @param customerName name of customer the ticket was issued for
     * @param eventName name of event the ticket was issued for
     * @return list of found customers
     */
    List<TicketDTO> findAllFilteredByCustomerAndEvent(String customerName, String eventName);

    /**
     * Find one ticket by the given reservation number (id)
     *
     * @param id reservation number of the ticket
     * @return found ticket
     */
    TicketDTO findOne(Long id);
}
