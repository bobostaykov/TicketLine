package at.ac.tuwien.sepm.groupphase.backend.repository;

import at.ac.tuwien.sepm.groupphase.backend.entity.Seat;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface SeatRepository extends JpaRepository<Seat, Integer> {

    /**
     * finds one seat by its id
     * @param id of the seat to be returned
     * @return java.util.Optional containing the seat if it was found
     */
    Optional<Seat> findOneById(Integer id);

    /**
     * finds all seats belonging to the hall with the specified id
     * @param id of hall containing the seats
     * @return a list of seats belonging to the hall with the specified id
     */
    List<Seat> findAllByHallID(Long id);
}
