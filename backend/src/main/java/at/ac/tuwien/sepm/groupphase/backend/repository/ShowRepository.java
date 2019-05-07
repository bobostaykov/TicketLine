package at.ac.tuwien.sepm.groupphase.backend.repository;

import at.ac.tuwien.sepm.groupphase.backend.entity.Show;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ShowRepository extends JpaRepository<Show, Long> {

    /**
     * Finds all shows filtered by event id
     *
     * @param eventID id of event
     * @return a list of found shows matching the criteria
     */
    @Query(value = "SELECT s FROM Show s WHERE event_id = :eventID")
    List<Show> findAllByEventID(@Param("eventID") Integer eventID);

    /*
     * Finds all shows filtered by location id
     *
     * @param locationID id of location by which to filter the shows
     * @return a list of found shows matching the criteria
     */
    //List<Show> findAllByLocationID(Integer locationID);

    /*
     * Finds all shows
     *
     * @return a list of all shows
     */
    //List<Show> findAll();
}
