package at.ac.tuwien.sepm.groupphase.backend.service;

import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.show.ShowDTO;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.ticket.TicketDTO;
import at.ac.tuwien.sepm.groupphase.backend.entity.Ticket;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.util.List;

public interface TicketService {

    /**
     * Save a single ticket entry
     *
     * @param ticketDTO to be saved
     * @return saved ticket entry
     */
    TicketDTO postTicket(TicketDTO ticketDTO);

    /**
     * Get all ticket entries
     *
     * @return list of all ticket entries
     */
    List<TicketDTO> findAll();

    /**
     * Find one ticket by the given reservation number (id)
     *
     * @param id reservation number of the ticket
     * @return found ticket
     */
    TicketDTO findOne(Long id);

    /**
     * Find one ticket by the given reservation number (id) with status RESERVATED and change it to SOLD
     *
     * @param id reservation number of the ticket
     * @return changed ticket
     */
    TicketDTO changeStatusToSold(Long id);

    /**
     * Find one ticket with status rservated by the given reservation number (id)
     *
     * @param id reservation number of the ticket
     * @return found ticket
     */
    TicketDTO findOneReservated(Long id);

    /**
     * Delete one ticket by the given ticket/reservation number (id)
     *
     * @param id ticket/reservation number of the ticket
     * @return deleted ticket
     */
    TicketDTO deleteOne(Long id);

    /**
     * Get all ticket entries filtered by the corresponsing customer and/or event name
     *
     * @param customerName name of customer the ticket was issued for
     * @param eventName name of event the ticket was issued for
     * @return list of found customers
     */
    List<TicketDTO> findAllFilteredByCustomerAndEvent(String customerName, String eventName);

    // PINOS IMPLEMENTATION
    /**
     * Get all reservated tickets filtered by customer and show
     *
     * @param surname surname of customer the ticket was issued for
     * @param firstname firstname of customer the ticket was issued for
     * @param show show the ticket was issued for
     * @return list of found tickets
     */
    //List<TicketDTO> findByCustomerNameAndShowWithStatusReservated(String surname, String firstname, ShowDTO show);

    /**
     * Get one receipt PDF for the list of ticket IDs
     *
     * @param ticketIDs String List containg ticket IDs
     * @return receipt PDF
     */
    MultipartFile getReceipt(List<String> ticketIDs) throws Exception;
}
