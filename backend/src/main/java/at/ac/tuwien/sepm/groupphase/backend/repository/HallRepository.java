package at.ac.tuwien.sepm.groupphase.backend.repository;

import at.ac.tuwien.sepm.groupphase.backend.entity.Hall;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface HallRepository extends JpaRepository<Hall, Long>, HallRepositoryCustom {

    /**
     * returns a list of all halls found in the system
     * @return a list of hall entities
     */
    List<Hall> findAll();
}
