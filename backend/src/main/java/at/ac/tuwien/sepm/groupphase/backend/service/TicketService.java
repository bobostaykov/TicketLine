package at.ac.tuwien.sepm.groupphase.backend.service;

import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.show.ShowDTO;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.ticket.TicketDTO;
import at.ac.tuwien.sepm.groupphase.backend.entity.Ticket;

import java.util.List;

public interface TicketService {

    /**
     * Save a single ticket entry
     *
     * @param ticket to be saved
     * @return saved ticket entry
     */
    Ticket postTicket(Ticket ticket);

    /**
     * Get all ticket entries
     *
     * @return list of all ticket entries
     */
    List<Ticket> findAll();

    /**
     * Get all ticket entries filtered by the corresponsing customer and/or event name
     *
     * @param customerName name of customer the ticket was issued for
     * @param eventName name of event the ticket was issued for
     * @return list of found customers
     */
    List<Ticket> findAllFilteredByCustomerAndEvent(String customerName, String eventName);

    /**
     * Find one ticket by the given reservation number (id)
     *
     * @param id reservation number of the ticket
     * @return found ticket
     */
    Ticket findOne(Long id);

    /**
     * Delete one ticket by the given ticket/reservation number (id)
     *
     * @param id ticket/reservation number of the ticket
     * @return deleted ticket
     */
    Ticket deleteOne(Long id);

    /**
     * Get all tickets filtered by customer and show
     *
     * @param customerName name of customer the ticket was issued for
     * @param show show the ticket was issued for
     * @return list of found tickets
     */
    List<TicketDTO> findByCustomerNameAndShow(String customerName, ShowDTO show);
}
