package at.ac.tuwien.sepm.groupphase.backend.repository;

import at.ac.tuwien.sepm.groupphase.backend.datatype.TicketStatus;
import at.ac.tuwien.sepm.groupphase.backend.entity.Customer;
import at.ac.tuwien.sepm.groupphase.backend.entity.Show;
import at.ac.tuwien.sepm.groupphase.backend.entity.Ticket;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TicketRepository extends JpaRepository<Ticket, Long> {
    /**
     * Find all ticket entries ordered by their IDs (ascending).
     *
     * @return ordered list of all ticket entries
     */
    List<Ticket> findAllByOrderByIdAsc();

    // PINOS IMPLEMENTATION
    /**
     * Find tickets that are issued for the given customer name and show name with status RESERVATED.
     *
     * @param customer customer to search for
     * @param show show to search for
     * @return List of found tickets
     */
    /*
    List<Ticket> findAllByCustomerAndShowWithStatusReservated(Customer customer, Show show);
    */

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
    List<Ticket> findAllByCustomer(List<Customer> customers);

    /**
     * Find tickets for show given by a list of shows.
     *
     * @param shows list of shows
     * @return found tickets
     */
    List<Ticket> findAllByShow(List<Show> shows);

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
     */
    void deleteByIdIn(List<Long> id);

}
