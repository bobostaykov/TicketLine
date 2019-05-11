package at.ac.tuwien.sepm.groupphase.backend.repository;

import at.ac.tuwien.sepm.groupphase.backend.entity.LoginAttempts;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.Optional;


@Repository
public interface LoginAttemptsRepository extends JpaRepository<LoginAttempts, Long> {
    /**
     *
     * @param id
     */
    @Modifying
    @Transactional
    @Query(value = "UPDATE attempts SET blocked = true WHERE id = :id", nativeQuery = true)
    void blockUser(@Param("id") Long id);

    /**
     *
     * @param id
     */
    @Modifying
    @Transactional
    @Query(value = "UPDATE attempts SET blocked = false WHERE id = :id", nativeQuery = true)
    void unblockUser(@Param("id") Long id);

    /**
     *
     * @param attempts
     * @param id
     */
    @Modifying
    @Transactional
    @Query(value = "UPDATE attempts SET attemps = :attempts WHERE id = :id", nativeQuery = true)
    void setAttemps(@Param("attempts") Integer attempts, @Param("id") Long id);

    /**
     *
     * @param id
     * @return
     */
    @Modifying
    @Transactional
    @Query(value = "SELECT attempts  WHERE id = :id", nativeQuery = true)
    int getAttempts(@Param("id") Long id);


    Optional<LoginAttempts> findById(Long id);

}
