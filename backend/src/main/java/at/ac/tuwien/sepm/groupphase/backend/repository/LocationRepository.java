package at.ac.tuwien.sepm.groupphase.backend.repository;

import at.ac.tuwien.sepm.groupphase.backend.entity.Location;
import at.ac.tuwien.sepm.groupphase.backend.repository.projections.SimpleLocation;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface LocationRepository extends JpaRepository<Location, Long>, LocationRepositoryCustom, PagingAndSortingRepository<Location, Long> {

    /**
     * Find all locations
     *
     * @param pageable special parameter to apply pagination
     * @return a page of the found locations
     */
    Page<Location> findAll(Pageable pageable);

    /**
     * Finds locations with names matching or containing param name
     * @param name substring of names that will be returned
     * @return list of location ids and names
     */
    List<SimpleLocation> findByLocationNameContainingIgnoreCase(String name);

    /**
     * Finds and returns a single location by its i
     * @param id of locaiton to be found
     * @return optional containing location if found
     */
    Optional<Location> findOneById(Long id);
}

