package at.ac.tuwien.sepm.groupphase.backend.repository;

import at.ac.tuwien.sepm.groupphase.backend.entity.Seat;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface SeatRepository extends JpaRepository<Seat, Long> {

    /**
     * finds one seat by its id
     * @param id of the seat to be returned
     * @return java.util.Optional containing the seat if it was found
     */
    Optional<Seat> findOneById(Long id);

}
