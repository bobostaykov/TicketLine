package at.ac.tuwien.sepm.groupphase.backend.repository;

import at.ac.tuwien.sepm.groupphase.backend.datatype.EventType;
import at.ac.tuwien.sepm.groupphase.backend.entity.Event;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import javax.persistence.Tuple;
import java.util.List;
import java.util.Set;

@Repository
public interface EventRepository extends JpaRepository<Event, Long>, EventRepositoryCustom, PagingAndSortingRepository<Event, Long> {

    /**
     * Get top 10 events
     *
     * @return ordered list of top 10 events
    */
    @Query("SELECT e.name, SUM(s.ticketsSold) FROM Show s, Event e WHERE s.event = e.id AND MONTHNAME(s.date) IN :monthsSet AND e.eventType IN :categoriesSet GROUP BY s.event ORDER BY SUM(s.ticketsSold) DESC")
    List<Tuple> findTopTenEvents(@Param("monthsSet") Set<String> monthsSet, @Param("categoriesSet") Set<EventType> categoriesSet);

    /**
     * Get all events by name.
     *
     * @param name name to search for
     * @return list of found events
     */
    List<Event> findAllByNameContainsIgnoreCase(String name);

    /**
     * Find all events ordered by name
     *
     * @param pageable special parameter to apply pagination
     * @return a page of the found events
     */
    Page<Event> findAllByOrderByNameAsc(Pageable pageable);

    /**
     * @param id of the artist
     * @param pageable special parameter to apply pagination
     * @return a list of events in which the artist participates or had participated
     */
    Page<Event> findAllByArtist_Id(Long id, Pageable pageable);
}
