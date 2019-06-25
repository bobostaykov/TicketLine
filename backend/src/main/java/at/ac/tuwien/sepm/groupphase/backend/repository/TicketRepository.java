package at.ac.tuwien.sepm.groupphase.backend.repository;

import at.ac.tuwien.sepm.groupphase.backend.datatype.TicketStatus;
import at.ac.tuwien.sepm.groupphase.backend.entity.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TicketRepository extends JpaRepository<Ticket, Long>, PagingAndSortingRepository<Ticket, Long> {
    /**
     * Find all ticket entries ordered by their IDs (ascending).
     * @param pageable the pagerequest
     * @return ordered list of all ticket entries
     */
    Page<Ticket> findAllByOrderByIdAsc(Pageable pageable);

    /**
     * Find ticket by given reservation number
     *
     * @param id of Ticket to be found
     * @return found ticket
     */
    Optional<Ticket> findOneById(Long id);

    /**
     * Find tickets for customers given by a list of customers.
     *
     * @param customers list of customers
     * @return found tickets
     */
    List<Ticket> findAllByCustomerIn(List<Customer> customers);

    /**
     * Find tickets for show given by a list of shows.
     *
     * @param shows list of shows
     * @return found tickets
     */
    List<Ticket> findAllByShowIn(List<Show> shows);

    /**
     * Finds tickets for a single show by its id
     * @param showId id of the show for which tickets should be found
     * @return list of tickets associated with the show
     */
    List<Ticket> findAllByShowId(Long showId);

    /**
     * Find ticket by given reservation number
     *
     * @param id of Ticket to be found
     * @param status of Ticket to be found
     * @return found ticket
     */
    Optional<Ticket> findOneByIdAndStatus(Long id, TicketStatus status);

    /**
     * Delete Ticket by reservation number
     *
     * @param id of Ticket to be deleted
     */
    void deleteById(Long id);

    /**
     * Finds a list of tickets with the given list of ids
     *
     * @param id list of ids of Tickets to find
     * @return a list of the found tickets
     */
    List<Ticket> findByIdIn(List<Long> id);

    /**
     * Delete a list of tickets with the given list of ids
     *
     * @param id list of ids of Tickets to delete
     * @return returns number of deleted tickets
     */
    int deleteByIdIn(List<Long> id);

    /**
     * Find all Tickets filtered by a given show and seat.
     *
     * @param show show to search tickets for
     * @param seat seat to search tickets for
     * @return
     */
    List<Ticket> findAllByShowAndSeat(Show show, Seat seat);

    /**
     * Find all Tickets filtered by a given show and sector.
     *
     * @param show show to search tickets for
     * @param sector sector to search tickets for
     * @return
     */
    List<Ticket> findAllByShowAndSector(Show show, Sector sector);

    /**
     * Find all Tickets filtered by a given show and status.
     *
     * @param show show to search tickets for
     * @param status status to serach tickets for
     * @return
     */
    List<Ticket> findAllByShowAndStatus(Show show, TicketStatus status);

    /**
     * Find all tickets filtred by a given reservationNumber and status
     * @param reservationNo the string with (parts of) the reservation naumber
     * @param status status to search tickets for
     * @return a list of the found tickets
     */
    Page<Ticket> findAllByStatusAndReservationNoContainsIgnoreCaseOrderByCustomer_Firstname(TicketStatus status,String reservationNo, Pageable pageable);

    /**
     *Returns a Page of (reserved, possibly other stati) tickets filtered by CustomerName and EventName
     * @param customerName the customer name
     * @param EventName the event name
     * @param ticketStatus the ticket status
     * @param pageable pagerequest
     * @return a page
     */
    Page<Ticket> findAllByCustomer_NameContainsIgnoreCaseAndShow_Event_NameContainsIgnoreCaseAndStatusOrderByCustomer_Firstname(String customerName, String EventName, TicketStatus ticketStatus, Pageable pageable);

}
