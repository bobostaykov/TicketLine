package at.ac.tuwien.sepm.groupphase.backend.service;

import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.ticket.TicketDTO;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.ticket.TicketPostDTO;
import at.ac.tuwien.sepm.groupphase.backend.exception.TicketSoldOutException;
import com.itextpdf.text.DocumentException;
import org.springframework.data.domain.Page;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.util.List;

public interface TicketService {

    /**
     * Save a single ticket entry
     *
     * @param ticketPostDTOS to be saved
     * @return saved ticket entry
     */
    List<TicketDTO> postTicket(List<TicketPostDTO> ticketPostDTOS)  throws TicketSoldOutException;

    /**
     *
     * @param page the requested page
     * @param pageSize the requested page size
     * @return
     */
    Page<TicketDTO> findAll(Integer page, Integer pageSize);

    /**
     * Find one ticket by the given reservation number (id)
     *
     * @param id reservation number of the ticket
     * @return found ticket
     */
    TicketDTO findOne(Long id);

    /**
     * Find one ticket by the given reservation number (id) with status RESERVED and change it to SOLD
     *
     * @param id reservation number of the ticket
     * @return changed ticket
     */
    TicketDTO changeStatusToSold(Long id);

    /**
     * Find more than one ticket by the given reservation number (id) with status RESERVATED and change it to SOLD
     *
     * @param reservationIds reservation numbers of the tickets
     * @return changed tickets
     */
    List<TicketDTO> changeStatusToSold(List<Long> reservationIds) throws TicketSoldOutException;

    /**
     * Find one ticket with status rservated by the given reservation number (id)
     *
     * @param id reservation number of the ticket
     * @return found ticket
     */
    TicketDTO findOneReserved(Long id);

    /**
     * Delete one ticket by the given ticket/reservation number (id)
     *
     * @param id ticket/reservation number of the ticket
     * @return deleted ticket
     */
    TicketDTO deleteOne(Long id);


    /**
     * Get one receipt PDF for the list of ticket IDs
     *
     * @param ticketIDs String List containg ticket IDs
     * @return receipt PDF as MultipartFile
     */
    byte[] getReceipt(List<String> ticketIDs) throws DocumentException, IOException;

    /**
     * Delete ticket(s) by id and receive storno receipt
     *
     * @param tickets list of ticket ids
     * @return Cancellation PDF receipt for deleted tickets as MultipartFile
     */
    byte[] deleteAndGetCancellationReceipt(List<String> tickets) throws DocumentException, IOException;

    /**
     * Generate PDF for list of tickets
     *
     * @param ticketIDs list of ticket-IDs
     * @return PDF containing printable tickets
     */
    byte[] generateTicketPDF(List<String> ticketIDs) throws DocumentException, IOException, NoSuchAlgorithmException;

    /**
     * Get all ticket entries filtered by the corresponding customer and/or event name
     *
     * @param customerName name of customer the ticket was issued for
     * @param eventName name of event the ticket was issued for
     * @param page the requested page
     * @param pageSize the requested pagesize
     * @param reserved true to only get reserved tickets, false to get only sold tickets. null to get all tickets
     * @return list of found customers
     */
    Page<TicketDTO> findAllFilteredByCustomerAndEvent(@NotNull String customerName, @NotNull String eventName, Boolean reserved, @PositiveOrZero Integer page, @Positive Integer pageSize);

    /**
     * Get all ticket entries filtered by the corresponding reservationNumber
     *
     * @param reservationNumber the reservation number
     * @param page the requested page
     * @param pageSize the size of the requested page
     * @return
     */
    Page<TicketDTO> findAllFilteredByReservationNumber(String reservationNumber, Boolean reserved, Integer page, Integer pageSize);

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
     *
     * @param customerName the last name of the customer
     * @param eventName name of the event
     * @param page the requested page
     * @param pageSize the size of the page
     * @return
     */
    Page<TicketDTO> findAllReservedFilteredByCustomerAndEvent(String customerName, String eventName, Integer page, Integer pageSize);
}
