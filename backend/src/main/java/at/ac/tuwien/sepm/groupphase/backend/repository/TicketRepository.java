package at.ac.tuwien.sepm.groupphase.backend.repository;

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
     * @return found ticket
     */
     Optional<Ticket> findOneById(Long id);
    
}
