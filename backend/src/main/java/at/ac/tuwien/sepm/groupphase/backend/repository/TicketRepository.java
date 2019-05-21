package at.ac.tuwien.sepm.groupphase.backend.repository;

import at.ac.tuwien.sepm.groupphase.backend.entity.Customer;
import at.ac.tuwien.sepm.groupphase.backend.entity.Event;
import at.ac.tuwien.sepm.groupphase.backend.entity.Show;
import at.ac.tuwien.sepm.groupphase.backend.entity.Ticket;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
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
    List<Ticket> findAllByOrderByReservationNumberAsc();


    /**
     * Find tickets that are issued for the given customer name and show name.
     *
     * @param customers list of customers to search for
     * @param shows list of shows to search for
     * @return List of found tickets
     */
    @Query("SELECT t from Ticket t " +
        "WHERE (customer_id in :customers)" +
        "JOIN Customer c ON t.customer = c " +
        "JOIN Show s ON t.show = s " +
        "JOIN Event e ON e = s.event " +
        "WHERE (c.name LIKE CONCAT('%',:customerName,'%') OR :customerName IS NULL) " +
        "AND (e.name LIKE CONCAT('%',:showName,'%') OR :showName IS NULL)")
    List<Ticket> findAllFilteredByCustomerAndEvent(@Param("customers") List<Customer> customers, @Param("shows") List<Show> shows);

    /**
     * Find ticket by given reservation number
     *
     * @return found ticket
     */
     Optional<Ticket> findOneByReservationNumber(Long reservationNumber);
    
}
