package at.ac.tuwien.sepm.groupphase.backend.repository;

import at.ac.tuwien.sepm.groupphase.backend.entity.Hall;
import at.ac.tuwien.sepm.groupphase.backend.entity.Location;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface HallRepository extends JpaRepository<Hall, Long>, HallRepositoryCustom {

    /**
     * returns a list of all halls found in the system
     * @return a list of all hall entities
     */
    List<Hall> findAll();

    /**
     * returns hall with specified id
     * @param hallId of hall to be found
     * @return hall with specified id
     */
    Optional<Hall> findById(Long hallId);

    /**
     * returns a list of all halls filtered by name and location of the hall and ordered by name ascending
     * @param name substring of the actual name of the hall
     * @param location matching the location of the hall
     * @return a list of halls filtered by hall name and location and ordered by name ascending
     */
    @Query(value = "SELECT h FROM Hall h WHERE (:name is null or lower(h.name) like lower(CONCAT('%',:name,'%'))) " +
        "and (:location is null or h.location = :location) ORDER BY h.name ASC")
    List<Hall> findByNameContainingAndLocation(@Param("name") String name, @Param("location") Location location);
}
