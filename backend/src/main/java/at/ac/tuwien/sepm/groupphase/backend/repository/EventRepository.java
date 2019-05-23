package at.ac.tuwien.sepm.groupphase.backend.repository;

import at.ac.tuwien.sepm.groupphase.backend.datatype.EventType;
import at.ac.tuwien.sepm.groupphase.backend.entity.Event;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Set;

@Repository
public interface EventRepository extends JpaRepository<Event, Long>, EventRepositoryCustom {

    /**
     * Get top 10 events
     *
     * @return ordered list of top 10 events
    */
    @Query("SELECT e.name, SUM(s.ticketsSold) FROM Show s, Event e WHERE s.event = e.id AND MONTHNAME(s.date) IN :monthsSet AND e.eventType IN :categoriesSet GROUP BY s.event ORDER BY SUM(s.ticketsSold) DESC")
    List<Object[]> findTopTenEvents(@Param("monthsSet") Set<String> monthsSet, @Param("categoriesSet") Set<EventType> categoriesSet);

    /**
     * Get all events sorted by name
     * @return a list with all events sorted by name
    */
    List<Event> findAllByOrderByNameAsc();

}
