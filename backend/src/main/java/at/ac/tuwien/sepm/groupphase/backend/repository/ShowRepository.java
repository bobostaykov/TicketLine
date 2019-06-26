package at.ac.tuwien.sepm.groupphase.backend.repository;

import at.ac.tuwien.sepm.groupphase.backend.entity.Event;
import at.ac.tuwien.sepm.groupphase.backend.entity.Show;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Repository
public interface ShowRepository extends JpaRepository<Show, Long>, ShowRepositoryCustom, PagingAndSortingRepository<Show, Long> {

    /**
     * Find all shows filtered by event
     *
     * @param events list of events
     * @return found shows
     */
    List<Show> findAllByEventIn(List<Event> events);

    /**
     * Find all shows by location id
     *
     * @param locationID id of the location to search for
     * @param pageable   a special parameter to apply pagination
     * @return a page of the found shows
     */
    Page<Show> findAllByHall_Location_Id(Long locationID, Pageable pageable);

    /**
     * finds and returns Optional containing show with id matching param if found
     *
     * @param id of show to be found
     * @return optional containing show if found
     */
    Optional<Show> findOneById(Long id);

    /**
     * Delete the show with the given id
     *
     * @param showId of the show to delete
     */
    void deleteById(Long showId);

    /**
     * finds out if there is a show associated with the given hall id
     * @param hallId to check if an element exists for
     * @return boolean that declares existance or non-existance of the element
     */
    boolean existsByHallId(Long hallId);

    /**
     * Increment number of sold tickets for given show
     *
     * @param showId id of show to increment number of sold tickets
     */
    @Transactional
    @Modifying
    @Query("UPDATE Show SET tickets_sold = tickets_sold + 1 WHERE id = :showId")
    void incrementSoldTickets(@Param("showId") Long showId);

    /**
     * Decrement number of sold tickets for given show
     *
     * @param showId id of show to increment number of sold tickets
     */
    @Modifying
    @Query("UPDATE Show SET tickets_sold = tickets_sold - 1 WHERE id = :showId")
    void decrementSoldTickets(@Param("showId") Long showId);
}
