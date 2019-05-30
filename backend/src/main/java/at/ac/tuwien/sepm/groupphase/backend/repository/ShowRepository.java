package at.ac.tuwien.sepm.groupphase.backend.repository;

import at.ac.tuwien.sepm.groupphase.backend.entity.Show;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

//TODO Class is unfinished
@Repository
public interface ShowRepository extends JpaRepository<Show, Long>, ShowRepositoryCustom {

    /**
     * Finds all shows filtered by event name
     *
     * @param eventName name of event
     * @return a list of found shows matching the criteria
     */
    @Query(value = "SELECT s FROM Show s JOIN Event e WHERE e.name = :eventName")
    List<Show> findAllShowsFilteredByEventName(@Param("eventName") String eventName);

    /*
     * Finds all shows filtered by location id
     *
     * @param locationID id of location by which to filter the shows
     * @return a list of found shows matching the criteria
     */
    //List<Show> findAllByLocationID(Integer locationID);

}
