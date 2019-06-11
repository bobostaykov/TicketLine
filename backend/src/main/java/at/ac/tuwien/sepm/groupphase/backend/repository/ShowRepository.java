package at.ac.tuwien.sepm.groupphase.backend.repository;

import at.ac.tuwien.sepm.groupphase.backend.entity.Event;
import at.ac.tuwien.sepm.groupphase.backend.entity.Show;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ShowRepository extends JpaRepository<Show, Long>, ShowRepositoryCustom, PagingAndSortingRepository<Show, Long> {

    /**
     * Find all shows filtered by event
     *
     * @param events list of events
     * @return found shows
     */
    List<Show> findAllByEvent(List<Event> events);

    /**
     * Find all shows by location id
     *
     * @param locationID id of the location to search for
     * @param pageable a special parameter to apply pagination
     * @return a page of the found shows
     */
    Page<Show> findAllByHall_Location_Id(Long locationID, Pageable pageable);
}
