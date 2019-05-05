package at.ac.tuwien.sepm.groupphase.backend.repository;

import at.ac.tuwien.sepm.groupphase.backend.entity.Sector;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface SectorRepository extends JpaRepository<Sector, Integer> {

    /**
     * finds one sector by its id
     * @param id of the sector to be found
     * @return optional containing the sector if it was found
     */
    Optional<Sector> findOneById(Integer id);

    /**
     * finds all sectors belonging to hall with specified id
     * @param id of the hall to which the sectors belong
     * @return list of sectors belonging to hall with specified id
     */
    List<Sector> findAllByHallID(Long id);
}
