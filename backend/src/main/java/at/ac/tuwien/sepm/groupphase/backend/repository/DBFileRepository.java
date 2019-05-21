package at.ac.tuwien.sepm.groupphase.backend.repository;

import at.ac.tuwien.sepm.groupphase.backend.entity.DBFile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface DBFileRepository extends JpaRepository<DBFile, Long> {

    /**
     * Find a single file by id.
     *
     * @param id of the file
     * @return Optional containing the file
     */
    Optional<DBFile> findOneById(Long id);

}
