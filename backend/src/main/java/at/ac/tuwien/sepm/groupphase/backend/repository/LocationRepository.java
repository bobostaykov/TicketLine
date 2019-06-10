package at.ac.tuwien.sepm.groupphase.backend.repository;

import at.ac.tuwien.sepm.groupphase.backend.entity.Location;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface LocationRepository extends JpaRepository<Location, Long>, LocationRepositoryCustom, PagingAndSortingRepository<Location, Long> {

    /**
     * Find all locations
     *
     * @param pageable special parameter to apply pagination
     * @return a page of the found locations
     */
    Page<Location> findAll(Pageable pageable);
}

